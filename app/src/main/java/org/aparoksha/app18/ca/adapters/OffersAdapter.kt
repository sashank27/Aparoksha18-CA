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
    val heads = arrayOf("End in Top 5 on Leaderboard",
                        "End in Top 10 on Leaderboard",
                        "Become Campus Ambassador of Aparoksha\'18")

    val footer = arrayOf("Get a chance to grab Aparoksha\'18 Hoodie",
                        "Get a chance to grab Aparoksha\'18 Dream T-Shirt",
                        "Get a chance to grab certificate, stickers and other exciting goodies")

    val images = arrayOf(R.drawable.hoodie,R.drawable.tee,R.drawable.goodies)

    override fun getCount(): Int {
        return images.size
    }

    override fun getItem(position: Int): Fragment {
        return OffersFragment.instantiate(heads[position],images[position],footer[position]) as Fragment
    }
}