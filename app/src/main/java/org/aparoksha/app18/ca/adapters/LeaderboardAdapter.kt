package org.aparoksha.app18.ca.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.Query
import kotlinx.android.synthetic.main.layout_leaderboard_member.view.*
import org.aparoksha.app18.ca.R
import org.aparoksha.app18.ca.models.User

/**
 * Created by sashank on 13/12/17.
 */

class LeaderboardAdapter(mRef : Query) : FirebaseRecyclerAdapter<User, LeaderboardAdapter.LeaderboardViewHolder>(
        User::class.java,
        R.layout.layout_leaderboard_member,
        LeaderboardViewHolder::class.java,
        mRef) {

    override fun populateViewHolder(viewHolder: LeaderboardViewHolder?, model: User?, position: Int) {
        if (model != null) {
            viewHolder?.bindView(model,position)
        }
    }

    class LeaderboardViewHolder(private var mView: View) : RecyclerView.ViewHolder(mView) {

        fun bindView(user: User, position: Int) {
            mView.username.text = user.name;
            mView.score.text = user.score.toString()
            mView.rank.text = (position+1).toString()

            if(user.gender.equals("female"))
                mView.profile.setImageResource(R.drawable.ic_user_girl)
            else
                mView.profile.setImageResource(R.drawable.ic_user_boy)
        }

    }

}
