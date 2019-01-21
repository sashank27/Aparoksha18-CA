package org.aparoksha.app18.ca

import android.app.Application
import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import org.aparoksha.app18.ca.activities.SplashActivity
import java.util.HashMap

/**
 * Created by betterclever on 18/12/17.
 */

class AparokshaCA : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        FirebaseMessaging.getInstance().subscribeToTopic("all")

        val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

        // set in-app defaults
        val remoteConfigDefaults: HashMap<String, Any> = HashMap()
        remoteConfigDefaults[SplashActivity.KEY_UPDATE_REQUIRED] = false
        remoteConfigDefaults[SplashActivity.KEY_CURRENT_VERSION] = "1.0.0"
        remoteConfigDefaults[SplashActivity.KEY_UPDATE_URL] = "https://play.google.com/store/apps/details?id=org.aparoksha.app18.ca"

        firebaseRemoteConfig.setDefaults(remoteConfigDefaults)
        firebaseRemoteConfig.fetch(60)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d("Remote Config", "remote config is fetched.")
                        firebaseRemoteConfig.activateFetched()
                    }
                }
    }
}