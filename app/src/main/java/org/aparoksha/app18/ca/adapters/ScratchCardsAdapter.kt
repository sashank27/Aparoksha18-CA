package org.aparoksha.app18.ca.adapters

import android.content.Context
import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.Query
import kotlinx.android.synthetic.main.cards_container.view.*
import org.aparoksha.app18.ca.R
import org.aparoksha.app18.ca.fragments.NewCardFragment
import org.aparoksha.app18.ca.models.Card

/**
 * Created by sashank on 13/12/17.
 */

class ScratchCardsAdapter(var mRef: Query, var mContext: Context) : FirebaseRecyclerAdapter<Card, ScratchCardsAdapter.ScratchCardsViewHolder>(
        Card::class.java,
        R.layout.cards_container,
        ScratchCardsViewHolder::class.java,
        mRef) {


    override fun populateViewHolder(viewHolder: ScratchCardsViewHolder?, model: Card?, position: Int) {
        if (model != null) {
            viewHolder?.bindView(model, mContext, mRef, position)
        }
    }


    class ScratchCardsViewHolder(private var mView: View) : RecyclerView.ViewHolder(mView) {

        fun bindView(card: Card, mContext: Context, mRef: Query, position: Int) {

            if(card.revealed){
                Glide.with(mContext)
                        .load(R.drawable.tick)
                        .into(mView.check)
                mView.value.text = "Points: " + card.value.toString()
            }

            else{
                Glide.with(mContext)
                        .load(R.drawable.cross)
                        .into(mView.check)
                mView.value.text = "Not Yet Revealed"

                mView.setOnClickListener {
                    val ft = (mContext as AppCompatActivity).supportFragmentManager.beginTransaction()

                    val bundle = Bundle()
                    bundle.putString("points",card.value.toString())

                    val frag = NewCardFragment()
                    frag.arguments = bundle

                    ft.add(R.id.frame, frag)
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    ft.commit()

                    card.revealed = true
                }
            }

        }
    }
}