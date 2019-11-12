package com.br.vobis.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.br.vobis.R
import com.br.vobis.model.Donation
import com.br.vobis.utils.DateUtils.formatDate
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_donation.view.*

class DonationAdapter(private var items: MutableList<Donation>) : RecyclerView.Adapter<DonationAdapter.CustomViewHolder>() {
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        val itemView = layoutInflater.inflate(R.layout.item_donation, parent, false)

        return CustomViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val item = items[position]

        holder.bind(item)
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val thumbnail: ImageView = itemView.thumbnail!!
        private val title: TextView = itemView.title!!
        private val date: TextView = itemView.date!!
        private val state: TextView = itemView.status!!

        fun bind(item: Donation) {

            val image: String = if (item.attach.size > 0) {
                item.attach.first()
            } else {
                "https://mtypks.org/wp-content/uploads/2018/10/mtyp-name-your-own-price-donation-image.png"
            }

            Glide
                    .with(itemView.context)
                    .load(image)
                    .centerCrop()
                    .into(thumbnail)

            title.text = item.name
            date.text = item.updatedOn?.let { formatDate(it) }
            state.text = when (item.status) {
                Donation.STATUS.WAITING -> "Aguardando um Boas"
                Donation.STATUS.PENDENT -> "Em Andamento"
                Donation.STATUS.RESOLVED -> "Resolvido"
            }
        }
    }
}