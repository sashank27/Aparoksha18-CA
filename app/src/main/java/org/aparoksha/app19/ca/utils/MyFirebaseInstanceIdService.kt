package org.aparoksha.app19.ca.utils

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

/**
 * Created by sashank on 20/12/17.
 */
class MyFirebaseInstanceIdService : FirebaseInstanceIdService(){

    override fun onTokenRefresh() {
        super.onTokenRefresh()
        val token = FirebaseInstanceId.getInstance().token

        fetchDBCurrentUser()?.child("tokenFCM")?.setValue(token)
        Log.d("FCMToken",token)
    }
}