package org.aparoksha.app18.ca.activities

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import java.util.*
import android.graphics.Bitmap
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore.Images
import android.util.Log
import android.view.View
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.google.firebase.database.*
import org.aparoksha.app18.ca.R
import java.io.ByteArrayOutputStream
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import org.aparoksha.app18.ca.DetailsActivity
import org.aparoksha.app18.ca.models.*
import org.json.JSONObject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import org.aparoksha.app18.ca.models.Image

class MainActivity : AppCompatActivity() {

    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var mAuthStateListener: FirebaseAuth.AuthStateListener
    private lateinit var mStorageReference: StorageReference
    private lateinit var mFirebaseStorage: FirebaseStorage
    private lateinit var mFirebaseDB: FirebaseDatabase
    private lateinit var mDBReference: DatabaseReference
    private lateinit var dbData: Data
    private lateinit var mLeaderboardRef: DatabaseReference
    private var list: MutableList<User> = mutableListOf()
    private lateinit var dialog: ProgressDialog

    private val RC_SIGN_IN = 1
    private val RC_PHOTO_PICKER = 2
    private val CAMERA_REQUEST = 3
    private val DETAILS_REQUEST = 4
    private val ERROR_ACTIVITY = 5
    private val PERMISSIONS_CAMERA_STORAGE_REQUEST = 12

    private fun initDB() {
        dbData = Data()
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

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) + ContextCompat
                    .checkSelfPermission(this,
                            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        || ActivityCompat.shouldShowRequestPermissionRationale(
                        this, Manifest.permission.CAMERA)) {

                    Snackbar.make(this.findViewById(android.R.id.content),
                            "Please Grant Permissions to capture and upload photos",
                            Snackbar.LENGTH_LONG).setAction("ENABLE")
                    {
                        ActivityCompat.requestPermissions(
                                this,arrayOf(Manifest.permission
                                .WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA),
                                PERMISSIONS_CAMERA_STORAGE_REQUEST)
                    }.show()
                } else {
                    ActivityCompat.requestPermissions(this,
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA),
                            PERMISSIONS_CAMERA_STORAGE_REQUEST)
                }
            } else {
                val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_REQUEST)
            }
        })

        scratch.setOnClickListener({
            val cards = Intent(this, ScratchCardsActivity::class.java)
            startActivity(cards)
        })

        mAuthStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val mUser = firebaseAuth.currentUser
            if (mUser != null) {
            } else {
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setIsSmartLockEnabled(false)
                                .setAvailableProviders(
                                        Arrays.asList<AuthUI.IdpConfig>(
                                                AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build(),
                                                AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                                .build(),
                        RC_SIGN_IN)
            }
        }
    }

    fun fetchInitialsTotalProgress() {
        mLeaderboardRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot?) {
                if(p0 != null) {
                    p0.children.mapNotNullTo(list) {
                        it.getValue<User>(User::class.java)
                    }
                    var max:Long = 0
                    for (e in list) {
                        max = maxOf(e.score,max)
                    }
                    val myRef = mFirebaseDB.getReference("leaderboard").child(mFirebaseAuth.currentUser!!.uid)
                    myRef.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(p0: DataSnapshot?) {
                            if (p0 != null) {
                                val myPoints: User? = p0.getValue(User::class.java)
                                if (myPoints != null) {
                                    if (myPoints.score == 0L) {
                                        totalProgress.progress = 0
                                    } else {
                                        totalProgress.progress = (myPoints.score*100/max).toInt()
                                    }
                                    pointsText.text = myPoints.score.toString() + " / " + max.toString()
                                    main.visibility = View.VISIBLE
                                    dialog.dismiss()
                                }
                            } else {
                                totalProgress.progress = 0
                                pointsText.text = "0 / "+ max
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

            override fun onCancelled(p0: DatabaseError?) {
            }

        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main.visibility = View.INVISIBLE
        dialog = ProgressDialog.show(this,"Fetching Data","Loading...")
        initDB()
        setListeners()

        title = "My Dashboard"

        if (mFirebaseAuth.currentUser != null) {
            mDBReference = mFirebaseDB.getReference("users").child(mFirebaseAuth.currentUser!!.uid)
            mDBReference.keepSynced(true)
            mLeaderboardRef = mFirebaseDB.getReference("leaderboard")
            mLeaderboardRef.keepSynced(true)

            mDBReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.value != null) {
                        dbData = p0.getValue(Data::class.java)!!
                        user.text = dbData.userName
                        scratchcardxp.max = 8
                        scratchcardxp.progress = (((dbData.totalPoints % 200) /25 ) % 8L).toInt()
                        if(!dbData.accountVerified) {
                            val i = Intent(this@MainActivity,UnverifiedActivity::class.java)
                            startActivityForResult(i,ERROR_ACTIVITY)
                        }
                    } else {
                        val i = Intent(this@MainActivity,DetailsActivity::class.java)
                        startActivityForResult(i,DETAILS_REQUEST)
                    }
                }

                override fun onCancelled(p0: DatabaseError?) {
                    //To change body of created functions use File | Settings | File Templates
                }
            })

            try {
                fetchInitialsTotalProgress()
            } catch (e: Exception) {
                totalProgress.progress = 0
                pointsText.text = "0 / 0"
                main.visibility = View.VISIBLE
                dialog.dismiss()
            }

            user.text = dbData.userName

        } else {
            AuthUI.getInstance().signOut(this)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            AuthUI.getInstance().signOut(this)
            user.text = ""
            return true
        } else if(item.itemId == R.id.uploadedImages) {
            val i = Intent(this,UploadsActivity::class.java)
            startActivity(i)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                toast("Signed In")

                mDBReference = mFirebaseDB.getReference("users").child(mFirebaseAuth.currentUser!!.uid)
                mDBReference.keepSynced(true)
                mLeaderboardRef = mFirebaseDB.getReference("leaderboard")
                mLeaderboardRef.keepSynced(true)

                mDBReference.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.value != null) {
                            dbData = p0.getValue(Data::class.java)!!
                            user.text = dbData.userName
                            scratchcardxp.max = 8
                            scratchcardxp.progress = (((dbData.totalPoints % 200) /25 ) % 8L).toInt()
                            if(!dbData.accountVerified) {
                                val i = Intent(this@MainActivity,UnverifiedActivity::class.java)
                                startActivityForResult(i,ERROR_ACTIVITY)
                            }
                        } else {
                            dbData = Data()
                            val i = Intent(this@MainActivity,DetailsActivity::class.java)
                            startActivityForResult(i,DETAILS_REQUEST)
                        }
                    }
                    override fun onCancelled(p0: DatabaseError?) {
                        //To change body of created functions use File | Settings | File Templates.
                    }
                })
                user.text = mFirebaseAuth.currentUser!!.email

                try {
                    fetchInitialsTotalProgress()
                } catch (e: Exception) {
                    totalProgress.progress = 0
                    pointsText.text = "0 / 0"
                    main.visibility = View.VISIBLE
                    dialog.dismiss()
                }

                user.text = mFirebaseAuth.currentUser!!.email

            } else {
                toast("Sign In Cancelled")
                finish()
            }
        }

        else if (requestCode == RC_PHOTO_PICKER && resultCode == Activity.RESULT_OK) {
            if (mFirebaseAuth.currentUser != null) {
                val pd = ProgressDialog.show(this, "Uploading File", "Processing...")
                val selectedImageUri = data!!.data

                val userStorage = mStorageReference.child("user")
                val idReference = userStorage.child(mFirebaseAuth.currentUser!!.uid)
                val photoRef = idReference.child(System.currentTimeMillis().toString())
                photoRef.putFile(selectedImageUri).addOnSuccessListener(this) {
                    pd.dismiss()
                    mDBReference.child("images").child(dbData.count.toString()).setValue(Image(photoRef.path,false))
                    updateScore()
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
                    mDBReference.child("images").child(dbData.count.toString()).setValue(Image(photoRef.path,false))
                    updateScore()
                    toast("Successfully Uploaded")
                }.addOnFailureListener(this) {
                    pd.dismiss()
                    toast("Failed")
                }
            }
        }

        else if (requestCode == DETAILS_REQUEST) {

            if(data == null) {
                finish()
            }
            else {
                val extras = data.extras
                dbData.collegeName = extras.get("collegeName").toString()
                dbData.userName = extras.get("userName").toString()
                dbData.fullName = extras.get("fullName").toString()
                dbData.gender = extras.get("gender").toString()
                mDBReference.child("gender").setValue(dbData.gender)
                mDBReference.child("collegeName").setValue(dbData.collegeName)
                mDBReference.child("fullName").setValue(dbData.fullName)
                mDBReference.child("userName").setValue(dbData.userName)
                user.text = dbData.userName
                if (mFirebaseAuth.currentUser!!.email == null) {
                    mDBReference.child("identifier").setValue(mFirebaseAuth.currentUser!!.phoneNumber)
                } else {
                    mDBReference.child("identifier").setValue(mFirebaseAuth.currentUser!!.email)
                }

                mDBReference.child("revealedCount").setValue(0)
                mDBReference.child("totalPoints").setValue(0)
                mDBReference.child("count").setValue(0)
                mDBReference.child("accountVerified").setValue(false)
                val i = Intent(this@MainActivity,UnverifiedActivity::class.java)
                startActivityForResult(i,ERROR_ACTIVITY)
            }
        } else if(requestCode == ERROR_ACTIVITY) {
            if(!dbData.accountVerified) {
                finish()
            }
        }
    }

    private fun updateScore() {
        dbData.count++
        mDBReference.child("count").setValue(dbData.count)
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    override fun onPause() {
        super.onPause()
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener)
    }

    override fun onResume() {
        super.onResume()
        if(mFirebaseAuth.currentUser != null) {
            user.text = dbData.userName
        }
        mFirebaseAuth.addAuthStateListener(mAuthStateListener)
    }

    override fun onBackPressed() {
        if(fab_menu.isOpened)
            fab_menu.close(true)
        else
            super.onBackPressed()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_CAMERA_STORAGE_REQUEST -> if (grantResults.isNotEmpty()) {
                val cameraPermission = (grantResults[1] == PackageManager.PERMISSION_GRANTED)
                val readExternalFile = (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                if (cameraPermission && readExternalFile) {
                    val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(cameraIntent, CAMERA_REQUEST)
                } else {
                    Snackbar.make(this.findViewById(android.R.id.content),
                            "Please Grant Permissions to capture and upload photos",
                            Snackbar.LENGTH_LONG).setAction("ENABLE"
                    ) {
                        ActivityCompat.requestPermissions(
                                this,arrayOf(Manifest.permission
                                .WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA),
                                PERMISSIONS_CAMERA_STORAGE_REQUEST)
                    }.show()
                }
            }
        }
    }
}