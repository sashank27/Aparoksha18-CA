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
    private val heads = arrayOf("Become Campus Ambassador of Aparoksha\'18",
            "Score 50 points", "Score 75 points",
            "Score 125 points", "Score 200 points",
            "Score 275 points", "Score 375 points",
            "Score 500 points", "Score 50000 points")

    private val footer = arrayOf("Get a chance to grab certificate, stickers and other exciting goodies",
            "Get a certificate from Aparoksha\'18 ",
            "Grab stickers from Aparoksha\'18 ",
            "Get a chance to grab Earphones",
            "Get a chance to grab Headphones",
            "Get a chance to grab External HDD",
            "Get a chance to grab Aparoksha\'18 Dream T-Shirt",
            "Get a chance to grab Aparoksha\'18 Hoodie",
            "Get a chance to grab a trip to GOA!!")

    private val images = arrayOf(R.drawable.goodies,
            R.drawable.certificate,R.drawable.stickers,
            R.drawable.earphones,R.drawable.headphones,
            R.drawable.hdd,R.drawable.tee,
            R.drawable.hoodie,R.drawable.goa)

    override fun getCount(): Int {
        return images.size
    }

    override fun getItem(position: Int): Fragment {
        return OffersFragment.instantiate(heads[position],images[position],footer[position])
    }
}