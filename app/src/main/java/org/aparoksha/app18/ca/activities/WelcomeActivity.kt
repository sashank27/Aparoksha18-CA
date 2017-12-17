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

data class IntroSlideData(
        val titleRes: Int,
        val descriptionRes: Int,
        val imageRes: Int
)

class WelcomeActivity : IntroActivity() {

    private val slides = arrayOf<IntroSlideData>(
            IntroSlideData(R.string.sign_in_slide_title, R.string.sign_in_slide_detail, R.drawable.intro1),
            IntroSlideData(R.string.upload_slide_title, R.string.upload_slide_detail, R.drawable.intro2),
            IntroSlideData(R.string.uploaded_slide_title, R.string.uploaded_slide_detail, R.drawable.intro3),
            IntroSlideData(R.string.scratch_card_slide_title, R.string.scratch_card_slide_detail, R.drawable.intro4)
    )

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
                .title(getString(R.string.sign_in_slide_title))
                .description(getString(R.string.sign_in_slide_detail))
                .image(R.drawable.intro1)
                .background(android.R.color.white)
                .backgroundDark(R.color.colorAccent)
                .scrollable(true)
                .build())

        addSlide(SimpleSlide.Builder()
                .title(getString(R.string.upload_slide_title))
                .description(getString(R.string.upload_slide_detail))
                .image(R.drawable.intro2)
                .background(android.R.color.white)
                .backgroundDark(R.color.colorAccent)
                .scrollable(true)
                .build())

        addSlide(SimpleSlide.Builder()
                .title(getString(R.string.uploaded_slide_title))
                .description(getString(R.string.uploaded_slide_detail))
                .image(R.drawable.intro3)
                .background(android.R.color.white)
                .backgroundDark(R.color.colorAccent)
                .scrollable(true)
                .build())

        addSlide(SimpleSlide.Builder()
                .title(getString(R.string.scratch_card_slide_title))
                .description(getString(R.string.scratch_card_slide_detail))
                .image(R.drawable.intro4)
                .background(android.R.color.white)
                .backgroundDark(R.color.colorAccent)
                .scrollable(true)
                .build())

        val permissionsSlide = SimpleSlide.Builder()
                .title(getString(R.string.permission_slide_title))
                .description(getString(R.string.permission_slide_detail))
                .background(android.R.color.white)
                .backgroundDark(R.color.colorAccent)
                .scrollable(true)
                .buttonCtaLabel("Grant Permissions")
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