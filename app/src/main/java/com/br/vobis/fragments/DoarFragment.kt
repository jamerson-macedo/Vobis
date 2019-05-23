package com.br.vobis.fragments

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.br.vobis.R
import com.br.vobis.helper.DatePickerFragment
import com.br.vobis.model.Category
import com.br.vobis.model.Doavel
import com.br.vobis.services.CategoryService
import com.br.vobis.services.DoacaoService
import com.br.vobis.utils.ImageUtils
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.fragment_doar.*
import java.text.DateFormat
import java.util.*


class DoarFragment : androidx.fragment.app.Fragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var itemDoavel: Doavel
    private lateinit var imageUri: Uri
    private val categories = arrayListOf<String>()
    private var storage: FirebaseStorage? = FirebaseStorage.getInstance()


    companion object {
        private const val PICK_IMAGE_REQUEST = 7
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_doar, container, false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            imageUri = data.data!!

            val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, imageUri)
            ImageUtils.compressImage(bitmap)
            imageView.setImageBitmap(bitmap)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        this.fetchCategories()

        // Set options of Spinner
        val arrayAdapter = ArrayAdapter(activity!!, android.R.layout.simple_spinner_item, categories)
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        spinner_type.prompt = "Selecione a categoria"
        spinner_type.adapter = arrayAdapter

        btn_add.setOnClickListener {
            ImageUtils.selectByGallery(activity!!, PICK_IMAGE_REQUEST)
        }

        btn_submit.setOnClickListener {
            val name = edt_name.text.toString().trim()
            val phone = edt_phone.text.toString().trim()
            val location = edt_location.text.toString().trim()
            val type = spinner_type?.selectedItem.toString().trim()
            val validity = edt_validity.text.toString().trim()
            val description = edt_description.text.toString().trim()

            if (arrayOf(name, phone, location, type, validity, description).contains("")) {
                Toast.makeText(activity, "Preencha os Campos!", Toast.LENGTH_LONG).show()
            } else {
                itemDoavel = Doavel(name, description, validity, phone, type, location)

                uploadImage().addOnCompleteListener {
                    if (it.isSuccessful) {

                        val urlFile = it.result!!.result
                        this.itemDoavel.addAttach(urlFile.toString())

                        DoacaoService().add(itemDoavel).addOnSuccessListener { document ->
                            itemDoavel.id = document.id
                            Toast.makeText(activity, "Cadastro realizado com sucesso", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }

        edt_validity.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val datePicker = DatePickerFragment()
                datePicker.setTargetFragment(this, 0)
                datePicker.show(fragmentManager!!, "date picker")
            }
        }
    }

    private fun fetchCategories() {
        CategoryService().getAll().addOnSuccessListener {
            it.documents.forEach { document ->
                val category = document.toObject(Category::class.java)!!
                categories.add(category.name)
            }
        }
    }

    private fun uploadImage(): Task<Task<Uri>> {
        val storageReference: StorageReference? = storage!!.reference
        val imagemref = storageReference!!.child("doacoes/" + UUID.randomUUID().toString())

        return imagemref.putFile(imageUri).continueWith { task: Task<UploadTask.TaskSnapshot> ->
            if (!task.isSuccessful) {
                throw task.exception!!
            }

            return@continueWith imagemref.downloadUrl
        }
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        val c = Calendar.getInstance()
        c.set(Calendar.YEAR, year)
        c.set(Calendar.MONTH, month)
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        val dateStr = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(c.time)
        edt_validity.setText(dateStr)
    }
}

