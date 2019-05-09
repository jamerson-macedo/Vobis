package com.br.vobis.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.br.vobis.helper.DatePickerFragment
import com.br.vobis.R
import com.br.vobis.model.Doavel
import com.br.vobis.utils.ImageUtils
import kotlinx.android.synthetic.main.fragment_doar.*
import java.text.DateFormat
import java.util.*


class DoarFragment : androidx.fragment.app.Fragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var itemDoavel: Doavel

    companion object {
        private const val PICK_IMAGE_REQUEST = 7
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_doar, container, false)

        // Events
        btn_add_photo.setOnClickListener {
            ImageUtils.selectByGallery(activity!!, PICK_IMAGE_REQUEST)
        }

        btn_submit.setOnClickListener {
            val name = edt_name.text.toString().trim()
            val phone = edt_phone.text.toString().trim()
            val location = edt_location.text.toString().trim()
            val type = spinner_type.prompt.toString().trim()
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
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        val dateStr = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(calendar.time)
        edt_validity.setText(dateStr)
    }
}
