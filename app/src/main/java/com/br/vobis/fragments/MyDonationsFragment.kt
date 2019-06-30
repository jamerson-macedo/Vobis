package com.br.vobis.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.br.vobis.R
import com.br.vobis.adapters.DonationAdapter
import com.br.vobis.model.Donation
import com.br.vobis.services.DonationService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_minhas_doacoes.*


class MyDonationsFragment : Fragment() {

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_minhas_doacoes, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupRecycleView()
    }

    override fun onStart() {
        super.onStart()

        val user = mAuth.currentUser
        val phone = user?.phoneNumber

        DonationService().collectionReference.whereEqualTo("phoneAuthor", phone).addSnapshotListener { querySnapshot, _ ->
            val items = mutableListOf<Donation>()

            querySnapshot?.documents?.forEach { documentSnapshot ->
                documentSnapshot?.toObject(Donation::class.java)?.let { donation ->
                    items.add(donation)
                }
            }

            recyclerView.adapter = DonationAdapter(items)
        }
    }

    private fun setupRecycleView() {
        recyclerView.apply {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
            setHasFixedSize(true)
            setItemViewCacheSize(20)
        }
    }


}
