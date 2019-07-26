package com.madebyk.android.mdchatapp.UserLogin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.madebyk.android.mdchatapp.R
import kotlinx.android.synthetic.main.activity_password_reset.*

class ResetPasswordActivity: AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_reset)
        password_reset_progressbar.setVisibility(View.GONE)
        resetPassword()
    }

    private fun resetPassword() {
        password_reset_button.setOnClickListener(View.OnClickListener {
            if (!password_reset_text.getText().toString().isEmpty()) {
                password_reset_progressbar.setVisibility(View.VISIBLE)
                val email = password_reset_text.getText().toString()
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnSuccessListener {
                        Toast.makeText(
                            this@ResetPasswordActivity,
                            "Email to reset password has been sent. Please check your emails.",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(this@ResetPasswordActivity, LogInActivity::class.java))
                        password_reset_progressbar.setVisibility(View.GONE)
                        finishAffinity()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            this@ResetPasswordActivity,
                            e.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } else {
                Toast.makeText(this@ResetPasswordActivity, "Please insert a valid email address", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

}