package com.br.vobis.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.br.vobis.R
import com.br.vobis.model.Category
import com.br.vobis.model.ExpandableCategory
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder

class CategoryAdapter(categories: List<ExpandableCategory>) : ExpandableRecyclerViewAdapter<CategoryViewHolder, SubCategoryView>(categories) {

    lateinit var context: Context

    override fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        context = parent.context

        val view = LayoutInflater.from(context).inflate(R.layout.list_item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onCreateChildViewHolder(parent: ViewGroup, viewType: Int): SubCategoryView {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_subcategory, parent, false)
        return SubCategoryView(view)
    }

    override fun onBindGroupViewHolder(holder: CategoryViewHolder, flatPosition: Int, group: ExpandableGroup<*>) {
        val item = group.items[flatPosition]

        holder.bind(item as Category)
    }

    override fun onBindChildViewHolder(holder: SubCategoryView, flatPosition: Int, group: ExpandableGroup<*>, childIndex: Int) {
        val subCategories = group.items[childIndex]
        holder.bind(subCategories as Category)
    }
}

class CategoryViewHolder(itemView: View) : GroupViewHolder(itemView) {
    private val title: TextView = itemView.findViewById(R.id.name)

    fun bind(item: Category) {
        title.text = item.name
    }
}


class SubCategoryView(itemView: View) : ChildViewHolder(itemView) {
    private val title: TextView = itemView.findViewById(R.id.name)

    fun bind(item: Category) {
        title.text = item.name
    }
}
