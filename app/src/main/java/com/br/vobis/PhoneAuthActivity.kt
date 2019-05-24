package com.br.vobis

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_phone_auth.*
import java.util.concurrent.TimeUnit

class PhoneAuthActivity : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    lateinit var mcallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    var verificaionid = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_auth)
        this.mAuth = FirebaseAuth.getInstance()
        btn_cadastrar.setOnClickListener {
            verify()


        }
        btn_verificar.setOnClickListener {

            autenticate()

        }

    }

    private fun autenticate() {
        val verifycode = verify_phone.text.toString()
        val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(verificaionid, verifycode)
        signin(credential)

    }

    private fun verifyCallbacks() {
        mcallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential?) {
                signin(p0)
            }

            override fun onVerificationFailed(p0: FirebaseException?) {
                Toast.makeText(this@PhoneAuthActivity, p0?.message.toString(), Toast.LENGTH_LONG).show()
            }

            override fun onCodeSent(p0: String?, p1: PhoneAuthProvider.ForceResendingToken?) {
                super.onCodeSent(p0, p1)
                verificaionid = p0.toString()
            }


        }

    }

    private fun signin(p0: PhoneAuthCredential?) {

        if (p0 != null) {
            mAuth.signInWithCredential(p0).addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Conta criada com sucesso", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)

                } else {
                    Toast.makeText(this, "sem codigo", Toast.LENGTH_LONG).show()


                }
            }


        }
    }


    private fun verify() {
        verifyCallbacks()
        val phone = cadas_phone.text.toString()
        Log.d("numberhone", phone)
        PhoneAuthProvider.getInstance().verifyPhoneNumber("+55 " + phone, 60, TimeUnit.SECONDS, this, mcallbacks)

    }

    override fun onStart() {
        super.onStart()
        if (mAuth.currentUser != null) {

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }


    }

}