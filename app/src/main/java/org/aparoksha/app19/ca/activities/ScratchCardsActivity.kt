package org.aparoksha.app19.ca.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_scratch.*
import org.aparoksha.app19.ca.R
import org.aparoksha.app19.ca.adapters.ScratchCardsAdapter
import org.aparoksha.app19.ca.fragments.NewCardFragment
import org.aparoksha.app19.ca.models.Card

/**
 * Created by sashank on 9/11/17.
 */

class ScratchCardsActivity : AppCompatActivity() {

    private lateinit var adapter: ScratchCardsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scratch)
        title = "Your Cards"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val mFirebaseAuth = FirebaseAuth.getInstance()
        val mFirebaseDB = FirebaseDatabase.getInstance()

        val query = mFirebaseDB.getReference("users").
                child(mFirebaseAuth.currentUser!!.uid).child("cards")

        val options = FirebaseRecyclerOptions.Builder<Card>()
                .setQuery(query, Card::class.java)
                .build()

        adapter = ScratchCardsAdapter(options, this, noCardsView)
        recyclerview.layoutManager = GridLayoutManager(this, 2)
        recyclerview.adapter = adapter

        noCardsView.visibility = if (adapter.itemCount == 0) View.VISIBLE else View.GONE

    }


    override fun onBackPressed() {
        val fragmentList = supportFragmentManager.fragments

        if (fragmentList.size != 0) {
            val activeFragment = fragmentList[fragmentList.size - 1]
            if (activeFragment is NewCardFragment)
                supportFragmentManager.beginTransaction().remove(activeFragment).commit()
            else
                super.onBackPressed()
        } else
            super.onBackPressed()
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