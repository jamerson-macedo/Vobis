package com.br.vobis.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.br.vobis.DoavelDetails
import com.br.vobis.R
import com.br.vobis.model.Donation
import com.br.vobis.utils.DateUtils.Companion.formatDate
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.card_item_doacao.view.*

class DonationAdapter(private var reports: MutableList<Donation>) : androidx.recyclerview.widget.RecyclerView.Adapter<DonationAdapter.CustomViewHolder>() {
    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        val itemView = layoutInflater.inflate(R.layout.card_item_doacao, parent, false)

        return CustomViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return reports.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val report = reports[position]

        holder.bind(report)
        holder.card.setOnClickListener {
            goToDetails(report)
        }
    }

    private fun goToDetails(item: Donation) {
        val intentDetails = Intent(context, DoavelDetails::class.java)
        intentDetails.putExtra("id", item.id)
        context.startActivity(intentDetails)
    }

    class CustomViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        val card: CardView = itemView.card!!
        private val thumbnail: ImageView = itemView.thumbnail!!
        private val title: TextView = itemView.title!!
        private val date: TextView = itemView.date!!
        private val state: TextView = itemView.status!!

        fun bind(item: Donation) {
            Glide
                    .with(itemView.context)
                    .load(item.photo?.first())
                    .centerCrop()
                    .into(thumbnail)

            title.text = item.author
            date.text = item.updatedOn?.let { formatDate(it) }
            state.text = item.status.toString()
        }
    }
}