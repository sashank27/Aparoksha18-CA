package org.aparoksha.app18.ca.activities

import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.*
import android.widget.Button
import kotlinx.android.synthetic.main.activity_welcome.*
import org.aparoksha.app18.ca.R

class WelcomeActivity : AppCompatActivity() {

    private var layouts: IntArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= 21) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_welcome)

        title = ""
        tabDots.setupWithViewPager(pager, true)
        layouts = intArrayOf(R.layout.welcome_slide1, R.layout.welcome_slide2, R.layout.welcome_slide3, R.layout.welcome_slide4)

        val myViewPagerAdapter = MyViewPagerAdapter()
        pager!!.adapter = myViewPagerAdapter
        pager!!.addOnPageChangeListener(viewPagerPageChangeListener)

        btn_skip!!.setOnClickListener { launchHomeScreen() }
        btn_next!!.setOnClickListener(View.OnClickListener {
            // checking for last page
            // if last page home screen will be launched
            val current = getItem(1)
            if (current < layouts!!.size) {
                //  to next screen
                pager!!.currentItem = current
            } else {
                launchHomeScreen()
            }
        })
    }

    private fun getItem(i: Int): Int {
        return pager!!.currentItem + i
    }

    private fun launchHomeScreen() {
        startActivity(Intent(this@WelcomeActivity, MainActivity::class.java))
        finish()
    }

    private val viewPagerPageChangeListener = object : ViewPager.OnPageChangeListener {

        override fun onPageSelected(position: Int) {
            if (position == layouts!!.size - 1) {
                btn_next!!.setText("Got It")
                btn_skip!!.visibility = View.GONE
            } else {
                btn_next!!.setText("Next")
                btn_skip!!.visibility = View.VISIBLE
            }
        }

        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {
            // empty body
        }

        override fun onPageScrollStateChanged(arg0: Int) {
            //empty body
        }
    }

    inner class MyViewPagerAdapter : PagerAdapter() {
        private var layoutInflater: LayoutInflater? = null

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = layoutInflater!!.inflate(layouts!![position], container, false)
            container.addView(view)
            return view
        }

        override fun getCount(): Int {
            return layouts!!.size
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view == obj
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as View
            container.removeView(view)
        }
    }
}
