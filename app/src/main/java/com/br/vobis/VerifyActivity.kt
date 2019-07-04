package com.br.vobis

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.activity_verify.*
import kotlinx.android.synthetic.main.sucess_donation.view.*
import java.util.concurrent.TimeUnit

class VerifyActivity : AppCompatActivity() {
    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private lateinit var mcallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    var verificaionId = ""
    var numberphone = ""
    var nome_user = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify)

        val extras = intent.extras
        if (extras != null) {
            numberphone = extras.getString("phone")
            Log.d("numberfone", numberphone)
            nome_user = extras.getString("name")
        }

        verify()

        btn_verificar.setOnClickListener {
            showpopupdone(R.layout.loading)
            authenticate()
        }
    }

    private fun authenticate() {

        val verifyCode = verify_phone.text.toString()
        if (!verifyCode.isEmpty()) {

            val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(verificaionId, verifyCode)
            signIn(credential)
        } else {
            Toast.makeText(this, "insira o codigo", Toast.LENGTH_LONG).show()
        }
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

    @SuppressLint("InflateParams", "ResourceAsColor")
    private fun showpopupdone(layout: Int) {
        val dialog = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(layout, null)
        dialog.setView(view)

        val alertDialog = dialog.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
        view.close_dialog.setOnClickListener {
            alertDialog.dismiss()
        }

    }

    private fun signIn(credential: PhoneAuthCredential?) {
        if (credential != null) {
            mAuth.signInWithCredential(credential).addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName(nome_user).build()
                    Toast.makeText(this, "Login realizado com Sucesso !", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
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



