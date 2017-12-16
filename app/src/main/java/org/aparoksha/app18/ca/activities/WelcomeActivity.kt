package org.aparoksha.app18.ca.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.support.design.widget.TabLayout
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.*
import android.widget.Button
import com.heinrichreimersoftware.materialintro.app.IntroActivity
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide
import kotlinx.android.synthetic.main.activity_welcome.*
import org.aparoksha.app18.ca.R
import android.support.design.widget.Snackbar
import com.heinrichreimersoftware.materialintro.app.OnNavigationBlockedListener
import android.text.Spanned
import android.text.SpannableString





class WelcomeActivity : IntroActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        isFullscreen = true
        super.onCreate(savedInstanceState)

        buttonBackFunction = IntroActivity.BUTTON_BACK_FUNCTION_SKIP
        buttonNextFunction = IntroActivity.BUTTON_NEXT_FUNCTION_NEXT
        isButtonBackVisible = true
        isButtonNextVisible = true
        isButtonCtaVisible = true
        buttonCtaTintMode = IntroActivity.BUTTON_CTA_TINT_MODE_TEXT

        pageScrollDuration = 1000

        addSlide(SimpleSlide.Builder()
                .title("Easy Login")
                .description("Sign up using either your Google account or mobile number, and get the account verified by Team Aparoksha")
                .image(R.drawable.intro1)
                .background(android.R.color.white)
                .backgroundDark(R.color.bright_foreground_disabled_material_dark)
                .scrollable(true)
                .build())

        addSlide(SimpleSlide.Builder()
                .title("Upload Files")
                .description("Easily upload files, either from storage or through direct capture")
                .image(R.drawable.intro2)
                .background(android.R.color.white)
                .backgroundDark(R.color.bright_foreground_disabled_material_dark)
                .scrollable(true)
                .build())

        addSlide(SimpleSlide.Builder()
                .title("View your Photos")
                .description("Keep a track of the files you've uploaded, while they get verified by us. Recieve points and increase your XP!!")
                .image(R.drawable.intro3)
                .background(android.R.color.white)
                .backgroundDark(R.color.bright_foreground_disabled_material_dark)
                .scrollable(true)
                .build())

        addSlide(SimpleSlide.Builder()
                .title("Scratch Cards")
                .description("Reach a new level, and get a Bonus Scratch Card, which may fetch you even more points!")
                .image(R.drawable.intro4)
                .background(android.R.color.white)
                .backgroundDark(R.color.bright_foreground_disabled_material_dark)
                .scrollable(true)
                .build())

        val permissionsSlide = SimpleSlide.Builder()
                .title("We need some Permissions")
                .description("Permissions are required so as to succesfully capture images from camera and upload them")
                .background(android.R.color.white)
                .backgroundDark(R.color.bright_foreground_disabled_material_dark)
                .scrollable(true)
                .permissions(arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ))
                .build()

        addSlide(permissionsSlide)

        addOnNavigationBlockedListener { position, direction ->
            val contentView = findViewById<View>(android.R.id.content)
            if (contentView != null) {
                val slide = getSlide(position)

                if (slide === permissionsSlide) {
                    Snackbar.make(contentView,
                            "Please grant the permissions before proceeding",
                            Snackbar.LENGTH_LONG)
                            .show()
                }
            }
        }

    }
}

    /*private var layouts: IntArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

/*
        if (Build.VERSION.SDK_INT >= 21) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
*/
        setContentView(R.layout.activity_welcome)

        title = ""
        tabDots.setupWithViewPager(pager, true)
        layouts = intArrayOf(R.layout.welcome_slide1, R.layout.welcome_slide2, R.layout.welcome_slide3, R.layout.welcome_slide4)

        val myViewPagerAdapter = MyViewPagerAdapter()
        pager.adapter = myViewPagerAdapter
        pager.addOnPageChangeListener(viewPagerPageChangeListener)

        btn_skip.setOnClickListener { launchHomeScreen() }
        btn_next.setOnClickListener({
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
        return pager.currentItem + i
    }

    private fun launchHomeScreen() {
        startActivity(Intent(this@WelcomeActivity, MainActivity::class.java))
        finish()
    }

    private val viewPagerPageChangeListener = object : ViewPager.OnPageChangeListener {

        override fun onPageSelected(position: Int) {
            if (position == layouts!!.size - 1) {
                btn_next.text = "Got It"
                btn_skip.visibility = View.GONE
            } else {
                btn_next.text = "Next"
                btn_skip.visibility = View.VISIBLE
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

        override fun isViewFromObject(view: View, obj: Any) = view == obj

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as View
            container.removeView(view)
        }
    }*/