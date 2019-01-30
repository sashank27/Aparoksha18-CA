package org.aparoksha.app19.ca.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import kotlinx.android.synthetic.main.activity_contacts.*
import org.aparoksha.app19.ca.R

class ContactsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)

        title = "Contact Us"

        floatingActionButton.setOnClickListener {
            val callNumber = "+917007406601"
            if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                val requestCode = 1
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.CALL_PHONE),
                        requestCode)
            }
            else
                startActivity(Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:" + callNumber)))
        }

        floatingActionButton2.setOnClickListener {
            val callNumber = "+917017733477"
            if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                val requestCode = 2
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.CALL_PHONE),
                        requestCode)
            }
            else
                startActivity(Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:" + callNumber)))
        }

        floatingActionButton3.setOnClickListener {
            val callNumber = "+918077937106"
            if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                val requestCode = 2
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.CALL_PHONE),
                        requestCode)
            }
            else
                startActivity(Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:" + callNumber)))
        }
    }
}
