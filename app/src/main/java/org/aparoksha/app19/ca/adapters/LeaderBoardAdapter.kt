package org.aparoksha.app19.ca.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import kotlinx.android.synthetic.main.layout_leaderboard_member.view.*
import org.aparoksha.app19.ca.R
import org.aparoksha.app19.ca.models.LeaderboardData

/**
 * Created by sashank on 13/12/17.
 */

class LeaderBoardAdapter(options: FirebaseRecyclerOptions<LeaderboardData>)
    : FirebaseRecyclerAdapter<LeaderboardData, LeaderBoardAdapter.LeaderBoardViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderBoardViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_leaderboard_member, parent, false)

        return LeaderBoardViewHolder(view)
    }


    override fun onBindViewHolder(holder: LeaderBoardViewHolder, position: Int, model: LeaderboardData) {
        holder.bindView(model, position)
    }

    inner class LeaderBoardViewHolder(private var mView: View) : RecyclerView.ViewHolder(mView) {

        fun bindView(user: LeaderboardData, position: Int) {
            mView.username.text = user.name
            mView.score.text = user.score.toString()
            mView.rank.text = (position + 1).toString()

            when {
                user.gender == "female" -> mView.profile.setImageResource(R.drawable.ic_user_girl)
                else -> mView.profile.setImageResource(R.drawable.ic_user_boy)
            }
        }

    }

    override fun getItem(position: Int): LeaderboardData {
        return super.getItem(itemCount - 1 - position)
    }

}
