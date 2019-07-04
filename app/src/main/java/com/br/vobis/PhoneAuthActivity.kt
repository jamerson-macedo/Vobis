package com.br.vobis

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_phone_auth.*


class PhoneAuthActivity : AppCompatActivity() {
    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_auth)

        btn_cadastrar.setOnClickListener {
            val phone = cadas_phone.rawText.toString()
            val nome = cadas_nome.text.toString()
            val i = Intent(this@PhoneAuthActivity, VerifyActivity::class.java)
            i.putExtra("phone", phone)
            i.putExtra("name", nome)
            startActivity(i)
        }

    }


    override fun onStart() {
        super.onStart()

        if (mAuth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}