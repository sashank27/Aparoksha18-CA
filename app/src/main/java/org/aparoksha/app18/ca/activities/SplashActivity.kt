package org.aparoksha.app18.ca.activities

import android.os.Bundle
import android.os.Handler
import android.support.constraint.ConstraintSet
import android.support.v7.app.AppCompatActivity
import android.transition.TransitionManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_splash.*
import org.aparoksha.app18.ca.R
import org.jetbrains.anko.startActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var mFirebaseAuth: FirebaseAuth
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
            mFirebaseAuth = FirebaseAuth.getInstance()

            if (mFirebaseAuth.currentUser != null) {
                startActivity<MainActivity>()
                finish()
            } else {
                startActivity<WelcomeActivity>()
                finish()
            }
        }, SPLASH_DISPLAY_LENGTH)

    }
}

