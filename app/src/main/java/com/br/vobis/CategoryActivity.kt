package com.br.vobis

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.br.vobis.adapters.CategoryAdapter
import com.br.vobis.model.Category
import com.br.vobis.model.Expandable
import com.br.vobis.services.CategoryService
import kotlinx.android.synthetic.main.activity_category.*


class CategoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        setupRecycleView()

        val pai = mutableListOf<Expandable>()
        val categories = mutableListOf<Category>()
        CategoryService().getAll().addOnSuccessListener {

            it.documents.forEach { document ->
                val category = document.toObject(Category::class.java)!!
                categories.add(category)

            }
            recyclerviewCategoty.adapter = CategoryAdapter(pai)
        }
        val categoria = Expandable("ola mundo", categories)
        Log.i("olamundo", categories.toString())

        pai.add(categoria)


    }

    private fun setupRecycleView() {
        // Set Recycle Layout
        recyclerviewCategoty.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(applicationContext)
        recyclerviewCategoty.setHasFixedSize(true)
        recyclerviewCategoty.setItemViewCacheSize(20)
    }

}
