package org.aparoksha.app18.ca.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore.Images
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_main.*
import org.aparoksha.app18.ca.R
import org.aparoksha.app18.ca.fetchDBCurrentUser
import org.aparoksha.app18.ca.isUserSignedIn
import org.aparoksha.app18.ca.models.User
import org.aparoksha.app18.ca.models.Image
import org.aparoksha.app18.ca.models.LeaderboardData
import org.jetbrains.anko.*
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity(), AnkoLogger {

    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var mStorageReference: StorageReference
    private lateinit var mFirebaseStorage: FirebaseStorage
    private lateinit var mFirebaseDB: FirebaseDatabase
    private lateinit var mDBReference: DatabaseReference
    private lateinit var dbData: User
    private lateinit var mLeaderboardRef: DatabaseReference
    private var list: MutableList<LeaderboardData> = mutableListOf()
    private lateinit var dialog: ProgressDialog

    private val RC_PHOTO_PICKER = 1
    private val CAMERA_REQUEST = 2

    private fun initDB() {
        dbData = User()
        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseStorage = FirebaseStorage.getInstance()
        mStorageReference = mFirebaseStorage.reference
        mFirebaseDB = FirebaseDatabase.getInstance()
    }

    private fun setListeners() {

        upload.setOnClickListener({
            if (fab_menu.isOpened)
                fab_menu.close(true)
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/jpeg"
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            startActivityForResult(Intent.createChooser(intent, "Select Photo to be uploaded"), RC_PHOTO_PICKER)
        })

        camera.setOnClickListener({
            if (fab_menu.isOpened)
                fab_menu.close(true)

            val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, CAMERA_REQUEST)

        })

        openScratchCardsButton.setOnClickListener({startActivity<ScratchCardsActivity>()})
    }

    private fun setProgressUser(){
        user.text = dbData.userName
        val currentXPlevel = (dbData.totalPoints / 200L).toInt() + 1
        val currentXPPoints =  (dbData.totalPoints - (currentXPlevel - 1) * 200).toInt()
        xpLevel.text = currentXPlevel.toString()
        totalProgress.max = 200
        totalProgress.progress = currentXPPoints
        pointsText.text = currentXPPoints.toString() + " / 200"
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    private fun fetchInitialsTotalProgress() {
        mLeaderboardRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot?) {
                if (snapshot != null) {

                    snapshot.children.mapNotNullTo(list) {
                        it.getValue<LeaderboardData>(LeaderboardData::class.java)
                    }

                    var max: Long = 0
                    for (e in list) {
                        max = maxOf(e.score, max)
                    }

                    val myRef = mFirebaseDB.getReference("leaderboard").child(mFirebaseAuth.currentUser!!.uid)

                    myRef.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot?) {
                            if (dataSnapshot != null) {
                                val myPoints: LeaderboardData? = dataSnapshot.getValue(LeaderboardData::class.java)
                                if (myPoints != null) {
                                    if (myPoints.score == 0L) {
                                        relativeProgress.progress = 0
                                    } else {
                                        relativeProgress.progress = (myPoints.score * 100 / max).toInt()
                                    }
                                    main.visibility = View.VISIBLE
                                    dialog.dismiss()
                                }
                            } else {
                                relativeProgress.progress = 0
                                main.visibility = View.VISIBLE
                                dialog.dismiss()
                            }
                        }

                        override fun onCancelled(p0: DatabaseError?) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                    })
                }
            }

            override fun onCancelled(error: DatabaseError?) {
                error(error)
            }

        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main.visibility = View.INVISIBLE
        dialog = ProgressDialog.show(this, "Fetching User", "Loading...")
        initDB()
        setListeners()

        title = "My Dashboard"

        mDBReference = fetchDBCurrentUser()!!
        mLeaderboardRef = mFirebaseDB.getReference("leaderboard")

        mDBReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value != null) {
                    dbData = snapshot.getValue(User::class.java)!!
                    setProgressUser()
                    if (!dbData.accountVerified) {
                        startActivity(intentFor<UnverifiedActivity>().clearTop().newTask())
                        finish()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError?) {
                // Shorthand Anko Logging ;)
                error(error)
            }
        })

        try {
            fetchInitialsTotalProgress()

        } catch (e: Exception) {
            relativeProgress.progress = 0
            main.visibility = View.VISIBLE
            dialog.dismiss()
        }

        setProgressUser()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            AuthUI.getInstance().signOut(this)
            user.text = ""
            finish()
            return true
        } else if (item.itemId == R.id.uploadedImages) {
            startActivity<UploadsActivity>()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_PHOTO_PICKER && resultCode == Activity.RESULT_OK) {
            if (mFirebaseAuth.currentUser != null) {
                val pd = ProgressDialog.show(this, "Uploading File", "Processing...")
                val selectedImageUri = data!!.data

                val userStorage = mStorageReference.child("user")
                val idReference = userStorage.child(mFirebaseAuth.currentUser!!.uid)
                val photoRef = idReference.child(System.currentTimeMillis().toString())
                photoRef.putFile(selectedImageUri).addOnSuccessListener(this) {
                    pd.dismiss()
                    mDBReference.child("images").push().setValue(Image(photoRef.path, false))
                    toast("Successfully Uploaded")
                }.addOnFailureListener(this) {
                    pd.dismiss()
                    toast("Failed")
                }
            }
        }
        else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            if (mFirebaseAuth.currentUser != null) {
                val pd = ProgressDialog.show(this, "Uploading File", "Processing...")
                val extras = data!!.extras
                val imageBitmap = extras.get("data") as Bitmap
                val selectedImageUri = getImageUri(applicationContext, imageBitmap)

                val userStorage = mStorageReference.child("user")
                val idReference = userStorage.child(mFirebaseAuth.currentUser!!.uid)
                val photoRef = idReference.child(System.currentTimeMillis().toString())
                photoRef.putFile(selectedImageUri).addOnSuccessListener(this) {
                    pd.dismiss()
                    mDBReference.child("images").push().setValue(Image(photoRef.path, false))
                    toast("Successfully Uploaded")
                }.addOnFailureListener(this) {
                    pd.dismiss()
                    toast("Failed")
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (isUserSignedIn()) {
            user.text = dbData.userName
        }
    }

    override fun onBackPressed() {
        if (fab_menu.isOpened)
            fab_menu.close(true)
        else
            super.onBackPressed()
    }
}