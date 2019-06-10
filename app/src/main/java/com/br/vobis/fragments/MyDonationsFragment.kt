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
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.synthetic.main.fragment_minhas_doacoes.*


class MyDonationsFragment : Fragment() {

    val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_minhas_doacoes, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupRecycleView()

        val user = mAuth.currentUser
        val phone = user?.phoneNumber

        DonationService().collectionReference.whereEqualTo("telefone", phone).get().addOnSuccessListener {
            val items = mutableListOf<Donation>()

            it.documents.forEach { documentSnapshot: DocumentSnapshot? ->
                val item = documentSnapshot?.toObject(Donation::class.java)
                if (item != null) {
                    items.add(item)
                }
            }

            recyclerView.adapter = DonationAdapter(items)
        }
    }
    private fun setupRecycleView() {
        // Set Recycle Layout
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        recyclerView.setItemViewCacheSize(20)
    }


}
