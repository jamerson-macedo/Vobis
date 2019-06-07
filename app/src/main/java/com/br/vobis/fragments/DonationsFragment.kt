package com.br.vobis.fragments

import android.Manifest.permission
import android.app.DatePickerDialog
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
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import com.br.vobis.R
import com.br.vobis.helper.DatePickerFragment
import com.br.vobis.model.Category
import com.br.vobis.model.Donation
import com.br.vobis.services.CategoryService
import com.br.vobis.services.DonationService
import com.br.vobis.utils.ImageUtils
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.fragment_doar.*
import java.text.DateFormat
import java.util.*


class DonationsFragment : androidx.fragment.app.Fragment(), DatePickerDialog.OnDateSetListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
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


        checkBox.setOnClickListener {
            edt_validity.isEnabled = !checkBox.isChecked
        }

        btn_add.setOnClickListener {
            ImageUtils.selectByGallery(activity!!, PICK_IMAGE_REQUEST)
        }

        btn_submit.setOnClickListener {


            val user = mAuth.currentUser

            val phone = user?.phoneNumber!!
            val name = edt_name.text.toString().trim()
            val location = edt_location.text.toString().trim()
            val type = spinner_type?.selectedItem.toString().trim()
            val validity = edt_validity.text.toString().trim()
            val description = edt_description.text.toString().trim()

            if (arrayOf(name, phone, location, type, validity, description).contains("")) {
                Toast.makeText(activity, "Preencha os Campos!", Toast.LENGTH_LONG).show()
            } else {
                val newItem = Donation(name, description, validity, phone, type, location)

                onSubmit(newItem)
            }
        }

        edt_validity.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                this.showCalendar()
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
            it.metadata?.reference?.downloadUrl?.let { urlDoc ->
                {
                    item.addAttach(urlDoc.toString())

                    DonationService().add(item).addOnSuccessListener { document ->
                        item.id = document.id
                        Toast.makeText(activity, "Cadastro realizado com sucesso", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }.addOnFailureListener {
            Toast.makeText(activity, "Falha ao enviar imagem", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        val cInstance = Calendar.getInstance()
        cInstance.set(Calendar.YEAR, year)
        cInstance.set(Calendar.MONTH, month)
        cInstance.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        val dateStr = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(cInstance.time)
        edt_validity.setText(dateStr)
    }

    private fun showCalendar() {
        val datePicker = DatePickerFragment()
        datePicker.setTargetFragment(this, 0)
        datePicker.show(fragmentManager!!, "date picker")
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
                val returnedAddress = addresses[0]
                val strReturnedAddress = StringBuilder("")
                for (i in returnedAddress.maxAddressLineIndex.rangeTo(addresses.size)) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n")
                    strReturnedAddress.append(returnedAddress.locale).append("\n")
                    strReturnedAddress.append(returnedAddress.postalCode).append("\n")
                    // nome da rua
                    strReturnedAddress.append(returnedAddress.thoroughfare)
                    strReturnedAddress.append(returnedAddress.subThoroughfare)

                }
                strAdd = strReturnedAddress.toString()
                Log.w("My", strReturnedAddress.toString())
            } else {
                Log.w("My", "No Address returned!")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.w("M", "Canont get Address!")
        }
        return strAdd
    }



}

