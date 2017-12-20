package org.aparoksha.app18.ca.adapters

import android.content.Context
import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.cards_container.view.*
import org.aparoksha.app18.ca.R
import org.aparoksha.app18.ca.fragments.NewCardFragment
import org.aparoksha.app18.ca.models.Card

/**
 * Created by sashank on 13/12/17.
 */

class ScratchCardsAdapter(options: FirebaseRecyclerOptions<Card>, val context: Context)
    : FirebaseRecyclerAdapter<Card, ScratchCardsAdapter.ScratchCardsViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScratchCardsViewHolder {
        return ScratchCardsViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.cards_container, parent, false))
    }

    override fun onBindViewHolder(holder: ScratchCardsViewHolder, position: Int, model: Card) {
        holder.bindView(model, context, this.getRef(position))
    }

    class ScratchCardsViewHolder(private var mView: View) : RecyclerView.ViewHolder(mView) {

        fun bindView(card: Card, mContext: Context, ref: DatabaseReference) {

            if (card.revealed) {
                Glide.with(mContext)
                        .load(R.drawable.ic_trophy)
                        .into(mView.check)
                mView.value.text = "You earned " + card.value.toString() + " points."
            } else {
                Glide.with(mContext)
                        .load(R.drawable.ic_question)
                        .into(mView.check)
                mView.value.text = "Not Yet Revealed"

                mView.setOnClickListener {
                    val ft = (mContext as AppCompatActivity).supportFragmentManager.beginTransaction()
                    mContext.window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

                    val bundle = Bundle()
                    bundle.putString("reference", ref.key)
                    bundle.putString("points",card.value.toString())

                    val frag = NewCardFragment()
                    frag.arguments = bundle

                    ft.add(R.id.frame, frag)
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    ft.commit()

                    //ref.child("revealed").setValue(true)
                }
            }
        }
    }
}