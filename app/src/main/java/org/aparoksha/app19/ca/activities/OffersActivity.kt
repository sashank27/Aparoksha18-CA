package org.aparoksha.app19.ca.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewPager
import kotlinx.android.synthetic.main.activity_offers.*
import org.aparoksha.app19.ca.R
import org.aparoksha.app19.ca.utils.ZoomOutPageTransformer
import org.aparoksha.app19.ca.adapters.OffersAdapter

class OffersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offers)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        title = "Rewards"

        pager.setPageTransformer(true, ZoomOutPageTransformer())
        pager.adapter = OffersAdapter(supportFragmentManager)

        pageIndicatorView.count = 8
        pageIndicatorView.selection = 0

        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                pageIndicatorView.selection = position
            }

        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
