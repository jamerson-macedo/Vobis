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
import kotlinx.android.synthetic.main.activity_verify.*
import java.util.concurrent.TimeUnit

class VerifyActivity : AppCompatActivity() {
    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private lateinit var mcallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    var verificaionId = ""
    var numberphone = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify)

        val extras = intent.extras
        if (extras != null) {
            numberphone = extras.getString("phone")
            Log.d("numberfone", numberphone)
        }

        verify()

        btn_verificar.setOnClickListener {

            authenticate()

        }

    }

    private fun authenticate() {
        val verifyCode = verify_phone.text.toString()
        val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(verificaionId, verifyCode)

        signIn(credential)
    }

    private fun verifyCallbacks() {
        mcallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential?) {
                signIn(credential)
            }

            override fun onVerificationFailed(p0: FirebaseException?) {
                Toast.makeText(this@VerifyActivity, p0?.message.toString(), Toast.LENGTH_LONG).show()
            }

            override fun onCodeSent(p0: String?, p1: PhoneAuthProvider.ForceResendingToken?) {
                super.onCodeSent(p0, p1)

                verificaionId = p0.toString()
                Log.d("verificacao", verificaionId)
            }
        }

    }

    private fun signIn(credential: PhoneAuthCredential?) {
        if (credential != null) {
            mAuth.signInWithCredential(credential).addOnCompleteListener { task: Task<AuthResult> ->
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
        val phone = numberphone
        Log.d("lofone", phone)
        PhoneAuthProvider.getInstance().verifyPhoneNumber("+55 $phone", 60, TimeUnit.SECONDS, this, mcallbacks)
    }
}



