package org.aparoksha.app18.ca.utils

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import org.aparoksha.app18.ca.models.Image
import org.jetbrains.anko.toast
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.InputStream

/**
 * Created by betterclever on 18/12/17.
 */

fun isUserSignedIn() = FirebaseAuth.getInstance().currentUser != null

fun fetchDBCurrentUser(): DatabaseReference? {
    val mFirebaseDB = FirebaseDatabase.getInstance()
    val mFirebaseAuth = FirebaseAuth.getInstance()

    return if (isUserSignedIn()) mFirebaseDB.getReference("users").child(mFirebaseAuth.currentUser!!.uid) else null
}

fun setIntentDetails(intent: Intent, fullName: String, collegeName: String, userName: String, gender: String,uri: Uri?,refer: String): Intent {
    intent.putExtra("fullName", fullName)
    intent.putExtra("collegeName", collegeName)
    intent.putExtra("userName", userName)
    intent.putExtra("gender",gender)
    intent.putExtra("uri",uri)
    intent.putExtra("refer",refer)
    return intent
}

fun uploadFile(selectedImageUri: Uri,mFirebaseAuth: FirebaseAuth,mDBReference: DatabaseReference,pd: ProgressDialog,mActivity: Activity){
    val mFirebaseStorage = FirebaseStorage.getInstance()
    val mStorageReference = mFirebaseStorage.reference
    val userStorage = mStorageReference.child("user")
    val idReference = userStorage.child(mFirebaseAuth.currentUser!!.uid)
    val photoRef = idReference.child(System.currentTimeMillis().toString())
    photoRef.putFile(selectedImageUri).addOnSuccessListener(mActivity) {
        pd.dismiss()
        mDBReference.child("images").push().setValue(Image(photoRef.path, false))
        mActivity.toast("Successfully Uploaded")
    }.addOnFailureListener(mActivity) {
        pd.dismiss()
        mActivity.toast("Failed")
    }
}

fun getImageUri(inContext: Context, inImage: Bitmap, title: String): Uri {
    val bytes = ByteArrayOutputStream()
    inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, title, null)
    return Uri.parse(path)
}

fun compressImage(context: Context, imageUri: Uri?, title: String): Uri? {
    try {
        val imageStream = context.contentResolver.openInputStream(imageUri)
        val options = BitmapFactory.Options()
        val size = imageStream?.available()
        if (size != null) {
            when {
                size >= (1 * 1024 * 1024) -> options.inSampleSize = 8
                size >= (512 * 1024) -> options.inSampleSize = 4
                size >= (64 * 1024) -> options.inSampleSize = 2
                else -> options.inSampleSize = 1
            }
        }
        val compressedImage = BitmapFactory.decodeStream(imageStream, null, options)
        return getImageUri(context, compressedImage, title)
    } catch (e : Exception){
        e.printStackTrace()
        return null
    }
}