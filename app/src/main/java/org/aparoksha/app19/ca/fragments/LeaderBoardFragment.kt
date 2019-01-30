package org.aparoksha.app19.ca.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_leaderboard.*
import org.aparoksha.app19.ca.R
import org.aparoksha.app19.ca.adapters.LeaderBoardAdapter
import org.aparoksha.app19.ca.models.LeaderboardData


/**
 * Created by sashank on 10/12/17.
 */

class LeaderBoardFragment : Fragment() {

    private lateinit var adapter: LeaderBoardAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val firebaseDB = FirebaseDatabase.getInstance()

        val query = firebaseDB.getReference("leaderboard")
                .orderByChild("score")
                .limitToLast(10)

        val options = FirebaseRecyclerOptions.Builder<LeaderboardData>()
                .setQuery(query, LeaderboardData::class.java)
                .build()

        adapter = LeaderBoardAdapter(options)
        return inflater.inflate(R.layout.fragment_leaderboard, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerview.layoutManager = LinearLayoutManager(context)
        recyclerview.adapter = adapter
        recyclerview.isNestedScrollingEnabled = false
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