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
import android.util.Log
import com.heinrichreimersoftware.materialintro.app.NavigationPolicy
import org.jetbrains.anko.toast


class WelcomeActivity : IntroActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        isFullscreen = true
        super.onCreate(savedInstanceState)

        buttonBackFunction = IntroActivity.BUTTON_BACK_FUNCTION_SKIP
        buttonNextFunction = IntroActivity.BUTTON_NEXT_FUNCTION_NEXT_FINISH
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
                .buttonCtaClickListener { v->
                    val i = Intent(this@WelcomeActivity,MainActivity::class.java)
                    startActivity(i)
                    finish()
                }
                .permissions(arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ))
                .build()

        addSlide(permissionsSlide)

        val last = SimpleSlide.Builder()
                .title("WELCOME")
                .background(android.R.color.white)
                .backgroundDark(R.color.bright_foreground_disabled_material_dark)
                .build()

        addSlide(last)

        setNavigationPolicy(object : NavigationPolicy {
            override fun canGoForward(position: Int): Boolean {
                if(getSlide(position) == last){
                    val i = Intent(this@WelcomeActivity,MainActivity::class.java)
                    startActivity(i)
                    finish()
                }
                return true
            }

            override fun canGoBackward(position: Int): Boolean {
                return true
            }
        })

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