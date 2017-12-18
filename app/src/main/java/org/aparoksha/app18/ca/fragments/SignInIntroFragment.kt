package org.aparoksha.app18.ca.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firebase.ui.auth.AuthUI
import com.github.paolorotolo.appintro.ISlidePolicy
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_signin_intro.*
import org.aparoksha.app18.ca.R
import org.aparoksha.app18.ca.isUserSignedIn
import org.jetbrains.anko.toast
import java.util.*

/**
 * Created by betterclever on 18/12/17.
 */

class SignInIntroFragment : Fragment(), ISlidePolicy {

    private val RC_SIGN_IN: Int = 23

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_signin_intro, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isUserSignedIn()) {
            signInButton.visibility = View.GONE
            signedInTextVIew.visibility = View.VISIBLE
        }

        signInButton.setOnClickListener {

            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(false)
                            .setAvailableProviders(
                                    Arrays.asList<AuthUI.IdpConfig>(
                                            AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build(),
                                            AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                            .build(),
                    RC_SIGN_IN)

        }
    }

    override fun isPolicyRespected() = FirebaseAuth.getInstance().currentUser != null


    override fun onUserIllegallyRequestedNextPage() {
        activity?.toast("Please Sign In First!!")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                activity?.toast("Signed In")
                signInButton.setOnClickListener(null)
                signInButton.visibility = View.GONE
                signedInTextVIew.visibility = View.VISIBLE
            }
        }
    }
}