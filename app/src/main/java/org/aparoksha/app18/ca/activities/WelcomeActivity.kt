package org.aparoksha.app18.ca.activities

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import com.heinrichreimersoftware.materialintro.app.IntroActivity
import com.heinrichreimersoftware.materialintro.app.NavigationPolicy
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide
import org.aparoksha.app18.ca.R
import org.jetbrains.anko.startActivity


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
                .backgroundDark(R.color.colorAccent)
                .scrollable(true)
                .build())

        addSlide(SimpleSlide.Builder()
                .title("Upload Files")
                .description("Easily upload files, either from storage or through direct capture")
                .image(R.drawable.intro2)
                .background(android.R.color.white)
                .backgroundDark(R.color.colorAccent)
                .scrollable(true)
                .build())

        addSlide(SimpleSlide.Builder()
                .title("View your Photos")
                .description("Keep a track of the files you've uploaded, while they get verified by us. Recieve points and increase your XP!!")
                .image(R.drawable.intro3)
                .background(android.R.color.white)
                .backgroundDark(R.color.colorAccent)
                .scrollable(true)
                .build())

        addSlide(SimpleSlide.Builder()
                .title("Scratch Cards")
                .description("Reach a new level, and get a Bonus Scratch Card, which may fetch you even more points!")
                .image(R.drawable.intro4)
                .background(android.R.color.white)
                .backgroundDark(R.color.colorAccent)
                .scrollable(true)
                .build())

        val permissionsSlide = SimpleSlide.Builder()
                .title("We need some Permissions")
                .description("Permissions are required to use camera and gallery to upload pictures.")
                .background(android.R.color.white)
                .backgroundDark(R.color.colorAccent)
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
                .backgroundDark(R.color.colorAccent)
                .build()

        addSlide(last)

        setNavigationPolicy(object : NavigationPolicy {
            override fun canGoForward(position: Int): Boolean {
                if(getSlide(position) == last){
                    this@WelcomeActivity.startActivity<MainActivity>()
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