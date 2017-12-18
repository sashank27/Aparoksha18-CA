package org.aparoksha.app18.ca

import android.app.Application
import com.google.firebase.database.FirebaseDatabase

/**
 * Created by betterclever on 18/12/17.
 */

class AparokshaCA : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}