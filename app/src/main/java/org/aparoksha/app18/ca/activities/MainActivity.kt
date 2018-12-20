package org.aparoksha.app18.ca.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.appinvite.AppInviteInvitation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import org.aparoksha.app18.ca.R
import org.aparoksha.app18.ca.models.LeaderboardData
import org.aparoksha.app18.ca.models.User
import org.jetbrains.anko.*
import org.aparoksha.app18.ca.utils.*


class MainActivity : AppCompatActivity(), AnkoLogger {

    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var mFirebaseDB: FirebaseDatabase
    private lateinit var mDBReference: DatabaseReference
    private lateinit var dbData: User
    private lateinit var dialog: ProgressDialog

    private val RC_PHOTO_PICKER = 1
    private val CAMERA_REQUEST = 2
    private val REQUEST_INVITE = 3

    private fun initDB() {
        dbData = User()
        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseDB = FirebaseDatabase.getInstance()
    }

    private fun setListeners() {

        upload.setOnClickListener    {
            if (fab_menu.isOpened)
                fab_menu.close(true)
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/jpeg"
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            startActivityForResult(Intent.createChooser(intent, "Select Photo to be uploaded"), RC_PHOTO_PICKER)
        }

        camera.setOnClickListener {
            if (fab_menu.isOpened)
                fab_menu.close(true)

            val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, CAMERA_REQUEST)
        }

        viewScratchCardsButton.setOnClickListener { startActivity<ScratchCardsActivity>() }
        viewUploadsButton.setOnClickListener { startActivity<UploadsActivity>() }
        viewRewardsButton.setOnClickListener { startActivity<OffersActivity>() }
        viewFeedButton.setOnClickListener { startActivity<FeedsActivity>() }
    }

    private fun setProgressUser() {
        user.text = dbData.userName
        val currentXPlevel = (dbData.totalPoints / 200L).toInt() + 1
        val currentXPPoints = (dbData.totalPoints - (currentXPlevel - 1) * 200).toInt()
        xpLevel.text = currentXPlevel.toString()
        totalProgress.max = 200
        totalProgress.progress = currentXPPoints
        pointsText.text = currentXPPoints.toString() + " / 200"
    }

    private fun fetchInitialsTotalProgress(mLeaderboardRef: DatabaseReference) {

        mLeaderboardRef.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot?) {

                val list: MutableList<LeaderboardData> = mutableListOf()
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
                                    if (dialog.isShowing) dialog.dismiss()
                                }
                            } else {
                                relativeProgress.progress = 0
                                main.visibility = View.VISIBLE
                                if (dialog.isShowing) dialog.dismiss()
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

        title = "Dashboard"

        mDBReference = fetchDBCurrentUser()!!
        val mLeaderboardRef = mFirebaseDB.getReference("leaderboard")

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
            fetchInitialsTotalProgress(mLeaderboardRef)
        } catch (e: Exception) {
            relativeProgress.progress = 0
            main.visibility = View.VISIBLE
            if (dialog.isShowing) dialog.dismiss()
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
        } else if (item.itemId == R.id.developers) {
            startActivity<DevelopersActivity>()
        } else if (item.itemId == R.id.invite) {
            onInviteClicked()
        } else if (item.itemId == R.id.contact) {
            startActivity<ContactsActivity>()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_PHOTO_PICKER && resultCode == Activity.RESULT_OK) {
            if (mFirebaseAuth.currentUser != null) {
                val pd = ProgressDialog.show(this, "Uploading File", "Processing...")
                val selectedImageUri = compressImage(this,data!!.data,"image")
                uploadFile(selectedImageUri!!, mFirebaseAuth, mDBReference, pd, this)
            }
        } else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            if (mFirebaseAuth.currentUser != null) {
                val pd = ProgressDialog.show(this, "Uploading File", "Processing...")
                val extras = data!!.extras
                val imageBitmap = extras.get("data") as Bitmap
                val selectedImageUri = getImageUri(applicationContext, imageBitmap, "Title")
                uploadFile(selectedImageUri, mFirebaseAuth, mDBReference, pd, this)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        // Fix the bottom scrolling bug
        /* Handler().postDelayed({
             scrollLayout.smoothScrollBy(0,0)
         }, 3000)*/

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

    private fun onInviteClicked() {
        val intent = AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage(getString(R.string.invitation_message))
                .setEmailHtmlContent("<html><body>"
                        + "<br><img src=\"http://i65.tinypic.com/33v1yxz.png\" alt=\"CA\" />"
                        + "<p>Download Aparoksha\'19 Campus Ambassador either from Play Store or by clicking <a href=\"https://play.google.com/store/apps/details?id=org.aparoksha.app18.ca\">here</a>"
                        + "</body></html>")
                .setEmailSubject("Join Aparoksha\'19 Campus Ambassador App")
                .build()
        startActivityForResult(intent, REQUEST_INVITE)
    }
}