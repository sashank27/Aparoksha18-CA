package org.aparoksha.app18.ca

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

/**
 * Created by betterclever on 18/12/17.
 */

fun isUserSignedIn() = FirebaseAuth.getInstance().currentUser != null

fun doDetailsExist(id: String) {

}

fun fetchDBCurrentUser(): DatabaseReference? {
    val mFirebaseDB = FirebaseDatabase.getInstance()
    val mFirebaseAuth = FirebaseAuth.getInstance()

    return if (isUserSignedIn()) mFirebaseDB.getReference("users").child(mFirebaseAuth.currentUser!!.uid) else null
}