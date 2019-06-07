package com.br.vobis.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.br.vobis.R


class DonationNeedsFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_necessidades, container, false)
    }


}
