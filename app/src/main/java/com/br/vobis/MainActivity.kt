package com.br.vobis

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.br.vobis.fragments.DoarFragment
import com.br.vobis.fragments.MinhasDoacoesFragment
import com.br.vobis.fragments.NecessidadesFragment
import com.ogaclejapan.smarttablayout.SmartTabLayout
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems

class MainActivity : AppCompatActivity() {
    private var viewPager: ViewPager? = null
    private var smartTabLayout: SmartTabLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.viewpage)
        smartTabLayout = findViewById(R.id.viewpagertab)
        supportActionBar!!.elevation = 0f
        // configurar action bar

        supportActionBar!!.title = "Doações"
        val adapter = FragmentPagerItemAdapter(supportFragmentManager,
                FragmentPagerItems.with(this).add("Doar", DoarFragment::class.java).add("Minhas Doações", MinhasDoacoesFragment::class.java).add("Necessidades", NecessidadesFragment::class.java
                )
                        .create())
        viewPager!!.adapter = adapter
        smartTabLayout!!.setViewPager(viewPager)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val fragment = supportFragmentManager.fragments
        fragment.forEach {
            it.onActivityResult(requestCode, resultCode, data)
        }
    }
}
