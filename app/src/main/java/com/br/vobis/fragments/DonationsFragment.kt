package com.br.vobis.fragments

import android.Manifest.permission
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import com.br.vobis.CategoryActivity
import com.br.vobis.R
import com.br.vobis.model.Donation
import com.br.vobis.model.InfoLocation
import com.br.vobis.model.LocationVobis
import com.br.vobis.services.DonationService
import com.br.vobis.utils.ImageUtils
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.fragment_doar.*
import java.util.*


class DonationsFragment : androidx.fragment.app.Fragment(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private var donation = Donation()
    private lateinit var imageUri: Uri

    private val googleApiClient: GoogleApiClient by lazy { buildApiLocation() }
    private val fusedLocationProviderClient: FusedLocationProviderClient by lazy { LocationServices.getFusedLocationProviderClient(activity!!) }

    private val storage: FirebaseStorage by lazy { FirebaseStorage.getInstance() }
    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }


    companion object {
        private const val PICK_IMAGE_REQUEST = 7
        private const val LOCATION_CODE = 1
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_doar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_category.setOnClickListener {
            intentCategory()
        }

        btn_add.setOnClickListener {
            ImageUtils.selectByGallery(activity!!, PICK_IMAGE_REQUEST)
        }

        btn_submit.setOnClickListener {
            btn_submit.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE

            val phoneAuthor = mAuth.currentUser?.phoneNumber!!
            val name = edt_name.text.toString().trim()
            val description = edt_description.text.toString().trim()

            if (isValidDonation()) {
                donation.name = name
                donation.phoneAuthor = phoneAuthor
                donation.description = description

                onSubmit(donation)
            }
        }
    }

    private fun isValidDonation(): Boolean {
        val errorInput = when {
            donation.name.isNullOrEmpty() -> "Preencha o campo do nome do item"
            donation.description.isNullOrEmpty() -> "Preencha o campo da Descrição"
            donation.validity == null -> "Preencha o campo da Validade"
            else -> null
        }

        if (errorInput != null) {
            Toast.makeText(activity, errorInput, Toast.LENGTH_LONG).show()

            btn_submit.visibility = View.VISIBLE
            progressBar.visibility = View.INVISIBLE
        }

        return errorInput == null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            imageUri = data.data!!

            val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, imageUri)
            ImageUtils.compressImage(bitmap)
            imageView.setImageBitmap(bitmap)
        }
    }

    override fun onStart() {
        super.onStart()

        googleApiClient.connect()
    }

    override fun onStop() {
        super.onStop()

        if (googleApiClient.isConnected) {
            googleApiClient.disconnect()
        }
    }

    override fun onConnected(p0: Bundle?) {
        if (ActivityCompat.checkSelfPermission(activity!!, permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission()
        }

        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                val latitude = it.latitude
                val longitude = it.longitude

                val locationVobis = getLocationByLatLong(latitude, longitude)

                donation.location = locationVobis
                edt_location.setText(locationVobis?.infors?.adress)
            }
        }
    }

    private fun requestPermission() {
        requestPermissions(activity!!, arrayOf(permission.ACCESS_FINE_LOCATION), LOCATION_CODE)
    }

    override fun onConnectionSuspended(p0: Int) { return }

    override fun onConnectionFailed(p0: ConnectionResult) { return }

    private fun buildApiLocation(): GoogleApiClient {
        return GoogleApiClient.Builder(activity!!)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
    }

    private fun uploadImage(): UploadTask {
        val storageReference: StorageReference? = storage.reference
        val imageRef = storageReference!!.child("doacoes/" + UUID.randomUUID().toString())

        return imageRef.putFile(imageUri)
    }

    private fun onSubmit(item: Donation) {
        uploadImage().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                task.result?.uploadSessionUri?.let {
                    // Add photo
                    item.addAttach(it.toString())

                    DonationService().add(item)
                    btn_submit.visibility = View.VISIBLE
                    progressBar.visibility = View.INVISIBLE

                    showSneaker("Obrigado, doação realizada com sucesso!")
                }
            }
        }.addOnFailureListener {
            showSneaker("Falha ao doar, Verifique sua internet")
            btn_submit.visibility = View.VISIBLE
            progressBar.visibility = View.INVISIBLE
        }
    }

    private fun getLocationByLatLong(latitude: Double, longitude: Double): LocationVobis? {

        val geoCoder = Geocoder(activity!!, Locale.getDefault())
        val addresses: List<Address> = geoCoder.getFromLocation(latitude, longitude, 1)

        if (addresses.isNotEmpty()) {
            val address = addresses[0].getAddressLine(0)

            val cityName = addresses[0].locality
            val stateName = addresses[0].adminArea
            val countryName = addresses[0].countryName
            val cep = addresses[0].postalCode

            val infoLocation = InfoLocation(address, cep, cityName, stateName, countryName)

            return LocationVobis(latitude, longitude, infoLocation)
        }

        return null
    }

    private fun intentCategory() {
        val intent = Intent(activity, CategoryActivity::class.java)
        activity?.startActivity(intent)
    }

    private fun showSneaker(message: String) {
        Snackbar.make(view!!, message, Snackbar.LENGTH_LONG).show()
    }
}

