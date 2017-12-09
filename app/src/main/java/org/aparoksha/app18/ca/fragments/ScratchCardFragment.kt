package org.aparoksha.app18.ca.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import kotlinx.android.synthetic.main.fragment_scratch.*
import org.aparoksha.app18.ca.R

/**
 * Created by sashank on 11/11/17.
 */

class ScratchCardFragment : Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_scratch,container,false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button.setOnClickListener({
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            val ft = activity.supportFragmentManager.beginTransaction()
            ft.add(R.id.frame, NewCardFragment())
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            ft.commit()
        })
    }
}