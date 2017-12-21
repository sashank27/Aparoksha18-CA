package org.aparoksha.app18.ca.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.clock.scratch.ScratchView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.popup_card.*
import org.aparoksha.app18.ca.R

/**
 * Created by sashank on 12/11/17.
 */

class NewCardFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.popup_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val key: String = this.arguments!!.get("reference").toString()
        val points: Int = Integer.parseInt(this.arguments!!.get("points").toString())

        val myRef = FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child("cards")
                .child(key)

        scratchView.setEraserSize(100.0f)
        scratchView.setWatermark(R.drawable.ic_question)

        unrevealedIV.setImageDrawable(ContextCompat.getDrawable(context!!,
                if (points > 0) R.drawable.ic_trophy else R.drawable.ic_sad))
        pointsTV.text = "${points} Points"

        scratchView.setEraseStatusListener(object : ScratchView.EraseStatusListener {
            override fun onProgress(percent: Int) {
                if (percent > 60 && percent != 100) {
                    scratchView.clear()
                    showResultScratch(points)
                    myRef.child("revealed").setValue(true)
                    if (points != 0)
                        textView.text = "You won $points Points!!"
                    else
                        textView.text = "Bad luck. Come back soon !!"
                }
            }

            override fun onCompleted(view: View) {}
        })

    }

    private fun showResultScratch(generatedPoint: Int) {
        cardFrame.visibility = View.GONE
        animationView.visibility = View.VISIBLE

        if (generatedPoint != 0)
            animationView.setAnimation("box_gift.json")
        else
            animationView.setAnimation("box_empty.json")

        animationView.playAnimation()
        animationView.loop(true)
    }

}