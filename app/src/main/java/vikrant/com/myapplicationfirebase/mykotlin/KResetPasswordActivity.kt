package vikrant.com.myapplicationfirebase.mykotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import vikrant.com.myapplicationfirebase.R

class KResetPasswordActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

       var inputEmail = findViewById(R.id.email) as EditText
        var btnReset = findViewById(R.id.btn_reset_password) as Button
        var btnBack = findViewById(R.id.btn_back) as Button
        var progressBar = findViewById(R.id.progressBar) as ProgressBar

        var auth = FirebaseAuth.getInstance()

        btnBack.setOnClickListener(View.OnClickListener { finish() })

        btnReset.setOnClickListener(View.OnClickListener {
            val email = inputEmail.getText().toString().trim({ it <= ' ' })

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(application, "Enter your registered email id", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            progressBar.setVisibility(View.VISIBLE)
            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(OnCompleteListener<Void> { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this@KResetPasswordActivity, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@KResetPasswordActivity, "Failed to send reset email!", Toast.LENGTH_SHORT).show()
                        }

                        progressBar.setVisibility(View.GONE)
                    })
        })
    }

}