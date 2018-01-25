package org.aparoksha.app18.ca.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.activity_unverified.*
import org.aparoksha.app18.ca.R

class UnverifiedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unverified)

        title = "Unverified Account"
        textView3.text = "Contact Team Aparoksha"
        textView4.text = "Your account is not verified yet. Mail your details to app@aparoksha.org and get it verified."

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.logout){
            AuthUI.getInstance().signOut(this)
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }


}
