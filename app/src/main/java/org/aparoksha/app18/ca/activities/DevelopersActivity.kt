package org.aparoksha.app18.ca.activities

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import kotlinx.android.synthetic.main.activity_developers.*
import org.aparoksha.app18.ca.R

class DevelopersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_developers)

        title = "App Operations"

        pranjal.setOnClickListener {
            openChromeTab("https://github.com/betterclever")
        }

        sashank.setOnClickListener {
            openChromeTab("https://github.com/sashank27")
        }

        akshat.setOnClickListener {
            openChromeTab("https://github.com/dabbler011")
        }
    }

    fun openChromeTab(url: String){
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(url))
    }
}
