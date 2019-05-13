package com.br.vobis.fragments

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.br.vobis.R
import com.br.vobis.helper.DatePickerFragment
import com.br.vobis.model.Doavel
import com.br.vobis.utils.ImageUtils
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_doar.*
import java.text.DateFormat
import java.util.*


class DoarFragment : androidx.fragment.app.Fragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var itemDoavel: Doavel
    private lateinit var imageUri: Uri
    var storage: FirebaseStorage? = FirebaseStorage.getInstance()
    var reference: StorageReference? = storage!!.reference

    val category = arrayOf("Defina uma categoria", "Alimentos", "Remedios", "Roupas", "Eletrodomesticos", "Outros")


    companion object {
        private const val PICK_IMAGE_REQUEST = 7
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_doar, container, false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == AppCompatActivity.RESULT_OK) {
            if (data != null) {
                imageUri = data.data!!

                val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, imageUri)
                ImageUtils.compressImage(bitmap)

                imageView.setImageBitmap(bitmap)

            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (spinner_type != null) {
            val arrayAdapter = ArrayAdapter(activity!!, android.R.layout.simple_spinner_item, category)
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner_type.adapter = arrayAdapter

            spinner_type.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    Toast.makeText(activity, category[position], Toast.LENGTH_SHORT).show()

                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }


            }
        }

        btn_add.setOnClickListener {
            ImageUtils.selectByGallery(activity!!, PICK_IMAGE_REQUEST)
        }

        btn_submit.setOnClickListener {
            val name = edt_name.text.toString().trim()
            val phone = edt_phone.text.toString().trim()
            val location = edt_location.text.toString().trim()
            val type = spinner_type?.prompt.toString().trim()
            val validity = edt_validity.text.toString().trim()
            val description = edt_description.text.toString().trim()

            if (arrayOf(name, phone, location, type, validity, description).contains("")) {
                alertError("Preencha os Campos!")
            } else {
                itemDoavel = Doavel(name, description, validity, phone, type, location)
                itemDoavel.save()
                uploadimage()
                Toast.makeText(activity, "Cadastro realizado com sucesso", Toast.LENGTH_LONG).show()

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

    private fun alertError(error: String) {
        Toast.makeText(activity, error, Toast.LENGTH_LONG).show()

    }

    private fun uploadimage() {

        val imagemref = reference!!.child("images/" + UUID.randomUUID().toString())
        imagemref.putFile(imageUri).addOnSuccessListener { Toast.makeText(activity, "upload concluido", Toast.LENGTH_LONG).show() }


    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        val c = Calendar.getInstance()
        c.set(Calendar.YEAR, year)
        c.set(Calendar.MONTH, month)
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        val dateStr = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(c.time)
        edt_validity.setText(dateStr)
    }
}// Required empty public constructor

