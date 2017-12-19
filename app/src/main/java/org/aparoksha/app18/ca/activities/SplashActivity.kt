package org.aparoksha.app18.ca.activities

import android.os.Bundle
import android.os.Handler
import android.support.constraint.ConstraintSet
import android.support.v7.app.AppCompatActivity
import android.transition.TransitionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_splash.*
import org.aparoksha.app18.ca.R
import org.aparoksha.app18.ca.fetchDBCurrentUser
import org.jetbrains.anko.startActivity

class SplashActivity : AppCompatActivity() {

    private val SPLASH_DISPLAY_LENGTH: Long = 2500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            val constraintSet = ConstraintSet()

            constraintSet.clone(this@SplashActivity, R.layout.secondary_splash)

            TransitionManager.beginDelayedTransition(splash)
            constraintSet.applyTo(splash)
        }, 200)

        Handler().postDelayed({
            val mFirebaseAuth = FirebaseAuth.getInstance()

            if (mFirebaseAuth.currentUser != null) {
                val myRef = fetchDBCurrentUser()!!
                myRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if(dataSnapshot.exists()){
                            startActivity<MainActivity>()
                            myRef.removeEventListener(this)
                            finish()
                        }
                        else{
                            startActivity<WelcomeActivity>()
                            myRef.removeEventListener(this)
                            finish()
                        }
                    }
                    override fun onCancelled(error: DatabaseError?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }
                })
            } else {
                startActivity<WelcomeActivity>()
                finish()
            }
        }, SPLASH_DISPLAY_LENGTH)

    }
}

