package org.aparoksha.app18.ca.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_feeds.*
import org.aparoksha.app18.ca.R
import org.aparoksha.app18.ca.adapters.FeedsAdapter
import org.aparoksha.app18.ca.models.FeedsData

class FeedsActivity : AppCompatActivity() {

    private lateinit var adapter: FeedsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feeds)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        title = "Feeds"

        val mFirebaseDB = FirebaseDatabase.getInstance()

        val query = mFirebaseDB.getReference("feed")

        val options = FirebaseRecyclerOptions.Builder<FeedsData>()
                .setQuery(query, FeedsData::class.java)
                .build()

        feeds.layoutManager = LinearLayoutManager(this)

        adapter = FeedsAdapter(options,this,noFeedsView)
        feeds.adapter = adapter

        noFeedsView.visibility = if (adapter.itemCount == 0) View.VISIBLE else View.GONE
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
