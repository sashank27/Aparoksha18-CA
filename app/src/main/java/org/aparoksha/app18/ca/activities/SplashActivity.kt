package org.aparoksha.app18.ca.activities

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.constraint.ConstraintSet
import android.support.transition.ChangeBounds
import android.support.transition.TransitionManager
import android.support.v7.app.AppCompatActivity
import android.view.animation.AnticipateOvershootInterpolator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_splash.*
import org.aparoksha.app18.ca.R
import org.aparoksha.app18.ca.utils.fetchDBCurrentUser
import org.jetbrains.anko.startActivity
import android.support.v7.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig


class SplashActivity : AppCompatActivity() {

    companion object {
        private val SPLASH_DISPLAY_LENGTH: Long = 2400
        private val TAG = SplashActivity::class.java.simpleName

        const val KEY_UPDATE_REQUIRED = "force_update_required"
        const val KEY_CURRENT_VERSION = "force_update_current_version"
        const val KEY_UPDATE_URL = "force_update_store_url"
    }

    private fun redirectStore(updateUrl: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun check() : Boolean {
        val remoteConfig = FirebaseRemoteConfig.getInstance()

        return if (remoteConfig.getBoolean(KEY_UPDATE_REQUIRED)) {
            val currentVersion = remoteConfig.getString(KEY_CURRENT_VERSION)
            val appVersion = getAppVersion(this)
            val updateUrl = remoteConfig.getString(KEY_UPDATE_URL)

            return if (!TextUtils.equals(currentVersion, appVersion)) {
                val dialog = AlertDialog.Builder(this)
                        .setTitle("New version available")
                        .setMessage("Please update app to new version to continue.")
                        .setPositiveButton("Update") { _, _ -> redirectStore(updateUrl) }
                        .setNegativeButton("No, thanks") { _, _ -> finish() }
                        .create()
                dialog.show()
                true
            } else false
        } else false
    }

    private fun getAppVersion(context: Context): String {
        var result = ""
        try {
            result = context.packageManager
                    .getPackageInfo(context.packageName, 0)
                    .versionName
            result = result.replace("[a-zA-Z]|-".toRegex(), "")
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, e.message)
        }
        return result
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            val constraintSet = ConstraintSet()
            constraintSet.clone(this@SplashActivity, R.layout.secondary_splash)

            val transition = ChangeBounds()
            transition.interpolator = AnticipateOvershootInterpolator(1.0f)
            transition.duration = 1200

            TransitionManager.beginDelayedTransition(splash, transition)
            constraintSet.applyTo(splash)

        }, 100)

        Handler().postDelayed({
            if(!check()) {
                val mFirebaseAuth = FirebaseAuth.getInstance()

                if (mFirebaseAuth.currentUser != null) {
                    val myRef = fetchDBCurrentUser()!!
                    myRef.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {
                                startActivity<MainActivity>()
                                myRef.removeEventListener(this)
                                finish()
                            } else {
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
            }
        }, SPLASH_DISPLAY_LENGTH)

    }
}

