package org.aparoksha.app18.ca.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_uploads.*
import org.aparoksha.app18.ca.R
import org.aparoksha.app18.ca.adapters.UploadsAdapter
import org.aparoksha.app18.ca.models.Image

class UploadsActivity : AppCompatActivity() {

    private lateinit var mFirebaseStorage: FirebaseStorage
    private lateinit var mFirebaseDB: FirebaseDatabase
    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var adapter: UploadsAdapter
    private lateinit var query: Query

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uploads)

        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseDB = FirebaseDatabase.getInstance()
        mFirebaseStorage = FirebaseStorage.getInstance()

        title = "Uploads"

        query = mFirebaseDB.getReference("users").
                child(mFirebaseAuth.currentUser!!.uid).child("images")

        uploadsList.layoutManager = GridLayoutManager(this,2)
        val options = FirebaseRecyclerOptions.Builder<Image>()
                .setQuery(query, Image::class.java)
                .build()

        adapter = UploadsAdapter(options, mFirebaseStorage.reference, this)
        uploadsList.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
}
