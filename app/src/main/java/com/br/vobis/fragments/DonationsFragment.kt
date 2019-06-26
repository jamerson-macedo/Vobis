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
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import com.br.vobis.CategoryActivity
import com.br.vobis.MapsActivity
import com.br.vobis.R
import com.br.vobis.model.Donation
import com.br.vobis.model.InfoLocation
import com.br.vobis.model.LocationVobis
import com.br.vobis.services.DonationService
import com.br.vobis.utils.ImageUtils
import com.br.vobis.utils.ImageUtils.generateImage
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_doar.*
import java.util.*


class DonationsFragment : androidx.fragment.app.Fragment(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private var donation = Donation()
    private var imageMap = hashMapOf<ImageView, Uri>()

    private val googleApiClient: GoogleApiClient by lazy { buildApiLocation() }
    private val fusedLocationProviderClient: FusedLocationProviderClient by lazy { LocationServices.getFusedLocationProviderClient(activity!!) }

    private val storage: FirebaseStorage by lazy { FirebaseStorage.getInstance() }
    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }


    companion object {
        private const val PICK_IMAGE_REQUEST = 7
        private const val CATEGORY_CODE = 22
        private const val LOCATION_CODE = 31
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        googleApiClient.connect()
        return inflater.inflate(R.layout.fragment_doar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        edt_category.setOnClickListener {
            startActivityForResult(Intent(activity, CategoryActivity::class.java), CATEGORY_CODE)
        }

        edt_subcategory.setOnClickListener {
            startActivityForResult(Intent(activity, CategoryActivity::class.java), CATEGORY_CODE)
        }

        openlocationmap.setOnClickListener {
            startActivityForResult(Intent(activity!!, MapsActivity::class.java), LOCATION_CODE)
        }

        btn_add_photos.setOnClickListener {
            ImageUtils.selectImageByGallery(activity!!, PICK_IMAGE_REQUEST)
        }

        btn_submit.setOnClickListener {
            btn_submit.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE

            val phoneAuthor = mAuth.currentUser?.phoneNumber!!
            val name = edt_name.text.toString().trim()
            val description = edt_description.text.toString().trim()

            donation.name = name
            donation.description = description
            donation.phoneAuthor = phoneAuthor

            if (isValidDonation()) {
                donation.name = name
                donation.phoneAuthor = phoneAuthor
                donation.description = description

                submitData()
            }
        }
    }

    private fun isValidDonation(): Boolean {
        val errorInput = when {
            donation.name.isNullOrEmpty() -> "Preencha o campo do nome do item"
            donation.description.isNullOrEmpty() -> "Preencha o campo da Descrição"
//            donation.validity == null -> "Preencha o campo da Validade"
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

        if (resultCode == AppCompatActivity.RESULT_OK && data != null) {
            when (requestCode) {
                PICK_IMAGE_REQUEST -> {
                    resolveResultImage(data.data!!)
                }
                CATEGORY_CODE -> {
                    data.extras?.let {
                        val category = it.getString("category")!!
                        val subcategory = it.getString("subcategory")!!

                        resolveResultCategory(category, subcategory)
                    }
                }
                LOCATION_CODE -> {
                    data.extras?.let {
                        val lat = it.getDouble("lat")
                        val long = it.getDouble("long")

                        resolveResultLocation(lat, long)
                    }
                }
            }
        }
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

    override fun onConnectionSuspended(p0: Int) {
        return
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        return
    }

    private fun resolveResultImage(uri: Uri) {
        if (imageMap.size != 3) {
            val bitmap = MediaStore.Images.Media.getBitmap(activity!!.contentResolver, uri)

            ImageUtils.compressImage(bitmap)

            val newImageView = generateImage(context!!, bitmap, 70, 70, 4)

            newImageView.setOnClickListener {
                container_photos.removeView(newImageView)
                imageMap.remove(newImageView)
            }

            container_photos.addView(newImageView)
            imageMap[newImageView] = uri
        } else {
            showSnackbar("Você antigiu o máximo de imagens permitidas!")
        }
    }

    private fun resolveResultCategory(category: String, subCategory: String) {
        donation.category = category
        donation.subCategory = subCategory

        edt_category.setText(category)
        edt_subcategory.setText(subCategory)
    }

    private fun resolveResultLocation(lat: Double, long: Double) {
        donation.location = getLocationByLatLong(lat, long)
        donation.location?.infors?.adress?.let {
            edt_location.setText(it)
        }
    }

    private fun buildApiLocation(): GoogleApiClient {
        return GoogleApiClient.Builder(activity!!)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
    }

    private fun submitData() {
        val storageReference: StorageReference? = storage.reference

        btn_submit.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE

        imageMap.values.forEach {
            val ref = storageReference!!.child("donations/" + UUID.randomUUID().toString())

            ref.putFile(it).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.uploadSessionUri.let { uri ->
                        donation.addAttach(uri.toString())
                    }
                }

                if (it == imageMap[imageMap.keys.last()]) {
                    DonationService().add(donation)

                    btn_submit.visibility = View.VISIBLE
                    progressBar.visibility = View.INVISIBLE

                    showSnackbar("Obrigado, doação realizada com sucesso!")
                }
            }.addOnFailureListener {
                showSnackbar("Falha ao doar, Verifique sua internet")

                btn_submit.visibility = View.VISIBLE
                progressBar.visibility = View.INVISIBLE
            }
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

    private fun showSnackbar(message: String) {
        Snackbar.make(view!!, message, Snackbar.LENGTH_LONG).show()
    }

}

