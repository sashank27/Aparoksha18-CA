package org.aparoksha.app18.ca.activities

import android.Manifest
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.github.paolorotolo.appintro.AppIntro
import com.github.paolorotolo.appintro.AppIntro2
import com.github.paolorotolo.appintro.AppIntro2Fragment
import com.github.paolorotolo.appintro.AppIntroFragment
import com.github.paolorotolo.appintro.model.SliderPage
import org.aparoksha.app18.ca.R
import android.Manifest.permission.READ_CONTACTS
import android.support.v4.app.Fragment
import org.aparoksha.app18.ca.fragments.DetailsIntroFragment
import org.aparoksha.app18.ca.fragments.SignInIntroFragment
import org.jetbrains.anko.*

class WelcomeActivity : AppIntro2() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val colorPrimary = ContextCompat.getColor(this, R.color.colorPrimary)
        val colorBlack = ContextCompat.getColor(this, android.R.color.black)

        // Show a Introductory fragment
        addSlide(AppIntro2Fragment.newInstance("Aparoksha'18 Campus Ambassador", "",
                R.drawable.logo, colorPrimary, colorBlack, colorBlack))

        // Get User Signed In
        addSlide(SignInIntroFragment())

        // Get Details From Users
        addSlide(DetailsIntroFragment())

        // Uploads Intro
        addSlide(AppIntro2Fragment.newInstance(
                getString(R.string.upload_slide_title),
                getString(R.string.upload_slide_detail),
                R.drawable.intro2, colorPrimary, colorBlack, colorBlack))

        addSlide(AppIntro2Fragment.newInstance(
                getString(R.string.uploaded_slide_title),
                getString(R.string.uploaded_slide_detail),
                R.drawable.intro3, colorPrimary, colorBlack, colorBlack))

        // Scratch Cards
        addSlide(AppIntro2Fragment.newInstance(
                getString(R.string.scratch_card_slide_title),
                getString(R.string.scratch_card_slide_detail),
                R.drawable.intro4, colorPrimary, colorBlack, colorBlack))

        showSkipButton(false)

        askForPermissions(arrayOf(Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE), 4)

    }

    override fun onDonePressed(fragment: Fragment) {
        startActivity(intentFor<MainActivity>().clearTop().newTask().noAnimation())
        finish()
    }
}
