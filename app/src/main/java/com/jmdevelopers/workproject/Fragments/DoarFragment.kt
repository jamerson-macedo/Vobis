package com.jmdevelopers.workproject.Fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.jmdevelopers.workproject.Helper.DatePickerFragment
import com.jmdevelopers.workproject.R
import com.jmdevelopers.workproject.model.Doavel
import com.jmdevelopers.workproject.utils.ImageUtils
import java.text.DateFormat
import java.util.*


class DoarFragment : androidx.fragment.app.Fragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var itemDoavel: Doavel

    companion object {
        private const val PICK_IMAGE_REQUEST = 7
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_doar, container, false)

        // Components
        val edt_name = view.findViewById<EditText>(R.id.edt_name)
        val edt_description = view.findViewById<EditText>(R.id.edt_description)
        val edt_location = view.findViewById<EditText>(R.id.edt_location)
        val edt_phone = view.findViewById<EditText>(R.id.edt_phone)
        val edt_validity = view.findViewById<EditText>(R.id.edt_validity)
        val edt_type = view.findViewById<Spinner>(R.id.spinner_type)

        val btn_add_photo = view.findViewById<ImageView>(R.id.btn_add_photo)
        val btn_submit = view.findViewById<Button>(R.id.btn_submit)


        // Events
        btn_add_photo.setOnClickListener {
            ImageUtils.selectByGallery(activity!!, PICK_IMAGE_REQUEST)
        }

        btn_submit.setOnClickListener {
            val name = edt_name.text.toString().trim()
            val phone = edt_phone.text.toString().trim()
            val location = edt_location.text.toString().trim()
            val type = edt_type.prompt.toString().trim()
            val validity = edt_validity.text.toString().trim()
            val description = edt_description.text.toString().trim()

            if (arrayOf(name, phone, location, type, validity, description).contains("")) {
                alertError("Preencha os Campos!")
            } else {
                itemDoavel = Doavel(name, description, validity, phone, type, location)
                itemDoavel.save()
            }
        }

        edt_validity.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val datePicker = DatePickerFragment()
                datePicker.setTargetFragment(this, 0)
                datePicker.show(fragmentManager!!, "date picker")
            }
        }

        return view

    }

    private fun alertError(error: String) {
        Toast.makeText(activity, error, Toast.LENGTH_LONG).show()

    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        val c = Calendar.getInstance()
        c.set(Calendar.YEAR, year)
        c.set(Calendar.MONTH, month)
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        val dateStr = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(c.time)
        val edt_validity = view.findViewById<EditText>(R.id.edt_validity)

        edt_validity.setText(dateStr)
    }
}// Required empty public constructor

