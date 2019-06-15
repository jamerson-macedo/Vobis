package com.br.vobis

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.br.vobis.adapters.CategoryAdapter
import com.br.vobis.model.Category
import com.br.vobis.model.ExpandableCategory
import com.br.vobis.services.CategoryService
import kotlinx.android.synthetic.main.activity_category.*


class CategoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        setupRecycleView()

        CategoryService().getAll().addOnSuccessListener { querySnapshot ->
            val categories = mutableListOf<ExpandableCategory>()

            querySnapshot.documents.forEach { document ->
                document.toObject(Category::class.java)?.let { category ->
                    if (category.subCategories.size > 0) {
                        categories.add(ExpandableCategory(category.name, category.subCategories))
                    }
                }
            }

            recyclerviewCategoty.adapter = CategoryAdapter(categories)
        }
    }

    private fun setupRecycleView() {
        // Set Recycle Layout
        recyclerviewCategoty.layoutManager = LinearLayoutManager(applicationContext)
        recyclerviewCategoty.setHasFixedSize(true)
        recyclerviewCategoty.setItemViewCacheSize(20)
    }

}
