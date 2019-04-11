package vikrant.com.myapplicationfirebase.mykotlin

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_signup.*
import vikrant.com.myapplicationfirebase.R

class KSignupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        var auth = FirebaseAuth.getInstance()
        var database = FirebaseDatabase.getInstance()
        var mDatabase = database.getReference("User")

       var btnSignIn = findViewById(R.id.sign_in_button) as Button
        var btnSignUp = findViewById(R.id.sign_up_button) as Button
        var inputEmail = findViewById(R.id.email) as EditText
        var inputPassword = findViewById(R.id.password) as EditText
        var  progressBar = findViewById(R.id.progressBar) as ProgressBar
        var btnResetPassword = findViewById(R.id.btn_reset_password) as Button


        btnResetPassword.setOnClickListener { startActivity(Intent(this@KSignupActivity, KResetPasswordActivity::class.java)) }

        btnSignIn.setOnClickListener { finish() }



        btnSignUp.setOnClickListener(View.OnClickListener {
            val email = inputEmail.text.toString().trim { it <= ' ' }
            val password = inputPassword.text.toString().trim { it <= ' ' }

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(applicationContext, "Enter email address!", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(applicationContext, "Enter password!", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(applicationContext, "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            progressBar.visibility = View.VISIBLE
            //create user
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this@KSignupActivity, OnCompleteListener<AuthResult> { task ->
                        Toast.makeText(this@KSignupActivity, "createUserWithEmail:onComplete:" + task.isSuccessful, Toast.LENGTH_SHORT).show()
                        progressBar.visibility = View.GONE
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        val userId = auth.getCurrentUser()!!.getUid()
                        val currentUser = mDatabase.child(userId)
                        currentUser.child("name").setValue(email)
                        currentUser.child("posts").setValue(email)

                        if (!task.isSuccessful) {
                            Toast.makeText(this@KSignupActivity, "Authentication failed." + task.exception!!,
                                    Toast.LENGTH_SHORT).show()
                        } else {
                            startActivity(Intent(this@KSignupActivity, KMain2Activity::class.java))
                            finish()
                        }
                    })
        })
    }

    override fun onResume() {
        super.onResume()
        progressBar.setVisibility(View.GONE)
    }

}