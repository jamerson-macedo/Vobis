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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import com.br.vobis.CategoryActivity
import com.br.vobis.R
import com.br.vobis.model.Category
import com.br.vobis.model.Donation
import com.br.vobis.services.CategoryService
import com.br.vobis.services.DonationService
import com.br.vobis.utils.ImageUtils
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.irozon.sneaker.Sneaker
import kotlinx.android.synthetic.main.fragment_doar.*
import java.util.*


class DonationsFragment : androidx.fragment.app.Fragment(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    val LOCATION_CODE = 1


    override fun onConnected(p0: Bundle?) {
        if (ActivityCompat.checkSelfPermission(activity!!, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestpermission()

        }

        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                it.latitude
                it.longitude
                var latitude = it.latitude
                var longitude = it.longitude
                edt_location.setText(latitude.toString())
                var valor = getCompleteAddressString(latitude, longitude)
                edt_location.setText(valor)

            }

        }
    }

    private fun requestpermission() {
        requestPermissions(activity!!, arrayOf(permission.ACCESS_FINE_LOCATION), LOCATION_CODE)

    }

    override fun onConnectionSuspended(p0: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    // google
    private lateinit var googleApiClient: GoogleApiClient
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var imageUri: Uri

    private val storage: FirebaseStorage by lazy { FirebaseStorage.getInstance() }
    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }


    companion object {
        private const val PICK_IMAGE_REQUEST = 7
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_doar, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.fetchCategories()
        this.initComponents()


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


    private fun initComponents() {
        googleApiClient = GoogleApiClient.Builder(activity!!).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(LocationServices.API).build()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity!!)


        btn_category.setOnClickListener {
            intentcategory()


        }




        btn_add.setOnClickListener {
            ImageUtils.selectByGallery(activity!!, PICK_IMAGE_REQUEST)
        }

        btn_submit.setOnClickListener {
            btn_submit.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE


            val user = mAuth.currentUser

            val phone = user?.phoneNumber!!
            val name = edt_name.text.toString().trim()

            val location = edt_location.text.toString().trim()
            val type = spinner_type?.selectedItem.toString().trim()

            val description = edt_description.text.toString().trim()

            if (arrayOf(name, phone, location, type, description).contains("")) {
                Toast.makeText(activity, "Preencha os Campos!", Toast.LENGTH_LONG).show()
                btn_submit.visibility = View.VISIBLE
                progressBar.visibility = View.INVISIBLE

            } else {

                val newItem = Donation(name, description, phone, type, location)

                onSubmit(newItem)


            }
        }


    }

    private fun fetchCategories() {
        CategoryService().getAll().addOnSuccessListener {
            val categories = mutableListOf<String>()

            it.documents.forEach { document ->
                val category = document.toObject(Category::class.java)!!
                categories.add(category.name)
            }

            val adapter = ArrayAdapter<String>(activity!!, android.R.layout.simple_spinner_dropdown_item, categories)
            spinner_type.adapter = adapter
        }
    }

    private fun uploadImage(): UploadTask {
        val storageReference: StorageReference? = storage.reference
        val imageRef = storageReference!!.child("doacoes/" + UUID.randomUUID().toString())

        return imageRef.putFile(imageUri)
    }

    private fun onSubmit(item: Donation) {
        uploadImage().addOnSuccessListener {
            it.metadata?.reference?.downloadUrl.let { urlDoc ->
                {
                    item.addAttach(urlDoc.toString())

                    DonationService().add(item).addOnSuccessListener { document ->
                        item.id = document.id
                        btn_submit.visibility = View.VISIBLE
                        progressBar.visibility = View.INVISIBLE
                        showsneakerpositive()

                    }
                }
            }
        }.addOnFailureListener {
            showsneakerfailled()
            btn_submit.visibility = View.VISIBLE
            progressBar.visibility = View.INVISIBLE
        }

    }


    override fun onStart() {
        super.onStart()
        googleApiClient.connect()
    }

    override fun onStop() {
        if (googleApiClient.isConnected) {
            googleApiClient.disconnect()
        }
        super.onStop()
    }

    private fun getCompleteAddressString(LATITUDE: Double, LONGITUDE: Double): String {
        var strAdd = ""
        val geocoder = Geocoder(activity!!, Locale.getDefault())
        try {
            val addresses: List<Address> = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1)
            if (addresses != null && addresses.size > 0) {
                val address = addresses.get(0).getAddressLine(0)

                val strReturnedAddress = StringBuilder("")
                val cityName = addresses.get(0).locality
                val stateName = addresses.get(0).adminArea
                val countryName = addresses.get(0).countryName

                strAdd = address
                Log.w("My", strAdd)

            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.w("M", "Canont get Address!")
        }
        return strAdd
    }

    private fun intentcategory() {
        val intent = Intent(activity, CategoryActivity::class.java)
        activity?.startActivity(intent)


    }

    private fun showsneakerpositive() {
        Sneaker.with(this).setTitle("Obrigado por Ajudar").setMessage("Sua doação foi realizada com sucesso!")
                .setDuration(4000)
                .autoHide(true).setCornerRadius(10, 10).sneakSuccess()
    }

    private fun showsneakerfailled() {
        Sneaker.with(this).setTitle("Falha ao doar").setMessage("Verifique sua internet")
                .setDuration(4000)
                .autoHide(true).setCornerRadius(10, 10).sneakWarning()
    }

}

