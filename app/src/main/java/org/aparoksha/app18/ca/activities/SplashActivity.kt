package org.aparoksha.app18.ca.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import org.aparoksha.app18.ca.R

class SplashActivity : AppCompatActivity() {

    private lateinit var mFirebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        mFirebaseAuth = FirebaseAuth.getInstance()

        if(mFirebaseAuth.currentUser != null) {
            val i = Intent(this,MainActivity::class.java)
            startActivity(i)
            finish()
        } else {
            val i = Intent(this,WelcomeActivity::class.java)
            startActivity(i)
            finish()
        }
    }
}
