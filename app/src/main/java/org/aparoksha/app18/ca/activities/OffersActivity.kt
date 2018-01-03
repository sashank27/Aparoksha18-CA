package org.aparoksha.app18.ca.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_offers.*
import org.aparoksha.app18.ca.R
import org.aparoksha.app18.ca.ZoomOutPageTransformer
import org.aparoksha.app18.ca.adapters.OffersAdapter

class OffersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offers)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        title = "Offers"

        pager.setPageTransformer(true, ZoomOutPageTransformer())
        pager.adapter = OffersAdapter(supportFragmentManager)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
