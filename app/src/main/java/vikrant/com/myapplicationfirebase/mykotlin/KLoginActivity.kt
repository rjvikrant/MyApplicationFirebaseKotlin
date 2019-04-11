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
import com.google.firebase.database.*
import vikrant.com.myapplicationfirebase.R

class KLoginActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


         var inputEmail = findViewById(R.id.email) as EditText
       var inputPassword = findViewById(R.id.password) as EditText
       var progressBar = findViewById(R.id.progressBar) as ProgressBar
       var btnSignup = findViewById(R.id.btn_signup) as Button
       var btnLogin = findViewById(R.id.btn_login) as Button
       var btnReset = findViewById(R.id.btn_reset_password) as Button

        var auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null) {
            startActivity(Intent(this@KLoginActivity, KMain2Activity::class.java))
            finish()
        }


        btnReset.setOnClickListener { startActivity(Intent(this@KLoginActivity, KResetPasswordActivity::class.java)) }


        btnSignup.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                startActivity(Intent(this@KLoginActivity, KSignupActivity::class.java))
            }
        })


        btnLogin.setOnClickListener(View.OnClickListener {
            val email = inputEmail.text.toString()
            val password = inputPassword.text.toString()

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(applicationContext, "Enter email address!", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(applicationContext, "Enter password!", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            progressBar.visibility = View.VISIBLE

            //authenticate user
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this@KLoginActivity, OnCompleteListener<AuthResult> { task ->
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        progressBar.visibility = View.GONE
                        if (!task.isSuccessful) {
                            // there was an error
                            if (password.length < 6) {
                                inputPassword.error = getString(R.string.minimum_password)
                            } else {
                                Toast.makeText(this@KLoginActivity, getString(R.string.auth_failed), Toast.LENGTH_LONG).show()
                            }
                        } else {

                            val database = FirebaseDatabase.getInstance()
                            val myConnectionsRef = database.getReference("User/" + auth.currentUser!!.uid + "/connections")

                            // Stores the timestamp of my last disconnect (the last time I was seen online)
                            val lastOnlineRef = database.getReference("User/" + auth.currentUser!!.uid + "/lastOnline")

                            val connectedRef = database.getReference(".info/connected")
                            connectedRef.addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val connected = snapshot.getValue(Boolean::class.java)!!
                                    if (connected) {
                                        val con = myConnectionsRef.push()

                                        // When this device disconnects, remove it
                                        con.onDisconnect().removeValue()

                                        // When I disconnect, update the last time I was seen online
                                        lastOnlineRef.onDisconnect().setValue(ServerValue.TIMESTAMP)

                                        // Add this device to my connections list
                                        // this value could contain info about the device or a timestamp too
                                        con.setValue(java.lang.Boolean.TRUE)
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    // Log.w(TAG, "Listener was cancelled at .info/connected");
                                }
                            })


                            val intent = Intent(this@KLoginActivity, KMain2Activity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    })
        })
    }
}