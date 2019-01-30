package org.aparoksha.app19.ca.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_uploads.*
import org.aparoksha.app19.ca.R
import org.aparoksha.app19.ca.adapters.UploadsAdapter
import org.aparoksha.app19.ca.models.Image

class UploadsActivity : AppCompatActivity() {

    private lateinit var adapter: UploadsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uploads)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val mFirebaseAuth = FirebaseAuth.getInstance()
        val mFirebaseDB = FirebaseDatabase.getInstance()
        val mFirebaseStorage = FirebaseStorage.getInstance()

        title = "Uploads"

        val query = mFirebaseDB.getReference("users").
                child(mFirebaseAuth.currentUser!!.uid).child("images")

        uploadsList.layoutManager = GridLayoutManager(this, 2)
        val options = FirebaseRecyclerOptions.Builder<Image>()
                .setQuery(query, Image::class.java)
                .build()

        adapter = UploadsAdapter(options, mFirebaseStorage.reference, this, noItemView)
        uploadsList.adapter = adapter

        noItemView.visibility = if (adapter.itemCount == 0) View.VISIBLE else View.GONE

    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
