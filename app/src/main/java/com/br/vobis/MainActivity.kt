package com.br.vobis

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.br.vobis.fragments.DonationNeedsFragment
import com.br.vobis.fragments.DonationsFragment
import com.br.vobis.fragments.MyDonationsFragment
import com.google.firebase.auth.FirebaseAuth
import com.ogaclejapan.smarttablayout.SmartTabLayout
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems

class MainActivity : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    private var viewPager: ViewPager? = null
    private var smartTabLayout: SmartTabLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance()

        viewPager = findViewById(R.id.viewpage)
        smartTabLayout = findViewById(R.id.viewpagertab)
        supportActionBar!!.elevation = 0f
        // configurar action bar

        supportActionBar!!.title = "Doações"
        val adapter = FragmentPagerItemAdapter(supportFragmentManager,
                FragmentPagerItems.with(this)
                        .add("Doar", DonationsFragment::class.java)
                        .add("Minhas Doações", MyDonationsFragment::class.java)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater

        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.logout -> {
                mAuth.signOut()
                val intent = Intent(this, PhoneAuthActivity::class.java)

                startActivity(intent)
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
