package com.br.vobis.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.br.vobis.CategoryActivity
import com.br.vobis.MapsActivity
import com.br.vobis.R
import com.br.vobis.model.Author
import com.br.vobis.model.Donation
import com.br.vobis.model.InfoLocation
import com.br.vobis.model.Location
import com.br.vobis.services.DonationService
import com.br.vobis.utils.ImageUtils
import com.br.vobis.utils.ImageUtils.generateImage
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_doar.*
import kotlinx.android.synthetic.main.sucess_donation.view.*
import java.util.*


class DonationsFragment : androidx.fragment.app.Fragment() {

    private var donation = Donation()
    private var autor = Author()
    private var imageMap = hashMapOf<ImageView, Uri>()

    private val storage: FirebaseStorage by lazy { FirebaseStorage.getInstance() }
    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    var alertDialog: AlertDialog? = null


    companion object {
        private const val PICK_IMAGE_REQUEST = 7
        private const val CATEGORY_CODE = 22
        private const val LOCATION_CODE = 31

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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

        edt_location.setOnClickListener {
            startActivityForResult(Intent(activity!!, MapsActivity::class.java), LOCATION_CODE)
        }



        btn_add_photos.setOnClickListener {
            ImageUtils.selectImageByGallery(activity!!, PICK_IMAGE_REQUEST)
        }

        btn_submit.setOnClickListener {


            onDonation()
        }
    }

    private fun isValidDonation(name: String, description: String): Boolean {
        val errorInput = when {
            name.isEmpty() -> "Preencha o campo do nome do item"
            description.isEmpty() -> "Preencha o campo da Descrição"
            // donation.validity == null -> "Preencha o campo da Validade"
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

    private fun resolveResultImage(uri: Uri) {
        if (imageMap.size == 5) {
            return showSnackbar("Você antigiu o máximo de imagens permitidas")
        } else if (imageMap.values.contains(uri)) {
            return showSnackbar("Essa imagem já existe")
        }

        val bitmap = MediaStore.Images.Media.getBitmap(activity!!.contentResolver, uri)

        ImageUtils.compressImage(bitmap)

        val newImageView = generateImage(context!!, bitmap, 70, 70, 4)

        newImageView.setOnClickListener {
            container_photos.removeView(newImageView)
            imageMap.remove(newImageView)
        }

        container_photos.addView(newImageView)
        imageMap[newImageView] = uri
    }

    private fun resolveResultCategory(category: String, subCategory: String) {
        donation.category = category
        donation.subCategory = subCategory

        edt_category.setText(category)
        edt_subcategory.setText(subCategory)
    }

    private fun resolveResultLocation(lat: Double, long: Double) {
        donation.location = getLocationByLatLong(lat, long)
        donation.location?.description?.address?.let {
            edt_location.setText(it)
        }
    }

    private fun onDonation() {
        btn_submit.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE
        // olhe se é isso mesmo
        val id = mAuth.currentUser?.uid
        val nomeuser = mAuth.currentUser?.displayName

        val phoneAuthor = mAuth.currentUser?.phoneNumber!!
        autor.name = nomeuser
        autor.id = id
        autor.telefone = phoneAuthor


        val name = edt_name.text.toString().trim()
        val description = edt_description.text.toString().trim()

        if (isValidDonation(name, description)) {
            donation.name = name
            donation.phoneAuthor = phoneAuthor
            donation.description = description

            if (imageMap.size > 0) {
                submitWithImages()
            } else {
                submitData()

            }
        }
    }

    private fun submitWithImages() {
        val storageReference: StorageReference? = storage.reference

        try {
            imageMap.values.forEach { uriFileLocal ->
                val ref = storageReference!!.child("donations/" + UUID.randomUUID().toString())

                ref.putFile(uriFileLocal).addOnCompleteListener { task ->
                    task.result!!.storage.downloadUrl.addOnSuccessListener {
                        donation.addAttach(it.toString())

                        if (uriFileLocal == imageMap[imageMap.keys.last()]) {
                            submitData()
                        }
                    }
                }
            }
        } catch (e: Exception) {
            showSnackbar("Falha ao doar, Verifique sua internet")

            btn_submit.visibility = View.VISIBLE
            progressBar.visibility = View.INVISIBLE
        }
    }

    private fun submitData() {

        try {
            DonationService().add(donation)

            btn_submit.visibility = View.VISIBLE
            progressBar.visibility = View.INVISIBLE

            showpopupdone(R.layout.sucess_donation)
            resetDataInputs()
            //showSnackbar("Obrigado, doação realizada com sucesso!")
        } catch (e: Exception) {
            showSnackbar("Verifique sua conexão e tente novamente")
        }

    }

    @SuppressLint("InflateParams", "ResourceAsColor")
    private fun showpopupdone(layout: Int) {
        val dialog = AlertDialog.Builder(activity)
        val view = layoutInflater.inflate(layout, null)
        dialog.setView(view)

        val alertDialog = dialog.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
        view.close_dialog.setOnClickListener {
            alertDialog.dismiss()
        }

    }


    private fun getLocationByLatLong(latitude: Double, longitude: Double): Location? {

        val geoCoder = Geocoder(activity!!, Locale.getDefault())
        val addresses: List<Address> = geoCoder.getFromLocation(latitude, longitude, 1)

        if (addresses.isNotEmpty()) {
            val address = addresses[0].getAddressLine(0)

            val cityName = addresses[0].subAdminArea
            val stateName = addresses[0].adminArea
            val countryName = addresses[0].countryName

            val infoLocation = InfoLocation(address, cityName, stateName, countryName)

            return Location(latitude, longitude, infoLocation)
        }

        return null
    }

    private fun resetDataInputs() {
        donation = Donation()

        imageMap.keys.forEach {
            container_photos.removeView(it)
            imageMap.remove(it)
        }

        edt_name.setText("")
        edt_category.setText("")
        edt_subcategory.setText("")
        edt_description.setText("")
        edt_location.setText("")
    }

    private fun showSnackbar(message: String) {

        Snackbar.make(view!!, message, Snackbar.LENGTH_LONG).show()
    }

}

