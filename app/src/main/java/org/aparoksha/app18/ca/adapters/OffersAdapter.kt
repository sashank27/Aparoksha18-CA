package org.aparoksha.app18.ca.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import org.aparoksha.app18.ca.R
import org.aparoksha.app18.ca.fragments.OffersFragment

/**
 * Created by akshat on 3/1/18.
 */

class OffersAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {
    private val heads = arrayOf("Score 100 points", "Score 400 points",
            "Score 1600 points", "Score 2000 points",
            "Score 5000 points", "Score 10000 points",
            "Score 20000 points", "Score 50000 points")

    private val footer = arrayOf("Get a certificate from Aparoksha\'18 ",
            "Get stickers from Aparoksha\'18 ",
            "Get Passes from Team Aparoksha",
            "Two Aparoksha\'18 Dream T-Shirts up for grab",
            "Two Aparoksha\'18 Hoodies up for grab",
            "Two Headphones up for grab",
            "Two External HDD up for grab",
            "A trip to GOA up for grab")

    private val images = arrayOf(R.drawable.certificate,R.drawable.stickers,
            R.drawable.pass,R.drawable.tee,
            R.drawable.hoodie,R.drawable.headphones,
            R.drawable.hdd,R.drawable.goa)

    override fun getCount(): Int {
        return images.size
    }

    override fun getItem(position: Int): Fragment {
        return OffersFragment.instantiate(heads[position],images[position],footer[position])
    }
}