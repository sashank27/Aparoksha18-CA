package org.aparoksha.app18.ca.Activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.aparoksha.app18.ca.Fragments.NewCardFragment
import org.aparoksha.app18.ca.R
import org.aparoksha.app18.ca.Fragments.ScratchCardFragment

/**
 * Created by sashank on 9/11/17.
 */

class ScratchCardsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scratch)

        val ft =supportFragmentManager.beginTransaction()
        ft.add(R.id.frame,ScratchCardFragment())
        ft.commit()

    }

    override fun onBackPressed() {
        val fragmentList = supportFragmentManager.fragments

        if(fragmentList.size != 0) {
            val activeFragment = fragmentList[fragmentList.size - 1]
            if (activeFragment is ScratchCardFragment) {
                super.onBackPressed()
            } else if (activeFragment is NewCardFragment) {
                supportFragmentManager.beginTransaction().
                        replace(R.id.frame, ScratchCardFragment()).commit()
            }
        }
        else
            super.onBackPressed()
    }
}