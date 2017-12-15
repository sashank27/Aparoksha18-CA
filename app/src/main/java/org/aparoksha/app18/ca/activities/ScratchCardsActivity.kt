package org.aparoksha.app18.ca.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import kotlinx.android.synthetic.main.activity_scratch.*
import org.aparoksha.app18.ca.R
import org.aparoksha.app18.ca.adapters.ScratchCardsAdapter
import org.aparoksha.app18.ca.fragments.NewCardFragment

/**
 * Created by sashank on 9/11/17.
 */

class ScratchCardsActivity : AppCompatActivity() {

    private lateinit var mFirebaseDB: FirebaseDatabase
    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var DBQuery: Query

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scratch)
        title = "Your Cards"

        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseDB = FirebaseDatabase.getInstance()

        DBQuery = mFirebaseDB.getReference("users").
                child(mFirebaseAuth.currentUser!!.uid).child("cards")

        recyclerview.layoutManager = GridLayoutManager(this,2)
        recyclerview.adapter = ScratchCardsAdapter(DBQuery,this)

    }


    override fun onBackPressed() {
        val fragmentList = supportFragmentManager.fragments

        if(fragmentList.size != 0) {
            val activeFragment = fragmentList[fragmentList.size - 1]
            if (activeFragment is NewCardFragment)
                supportFragmentManager.beginTransaction().remove(activeFragment).commit()
            else
                super.onBackPressed()
        }
        else
            super.onBackPressed()
    }

}