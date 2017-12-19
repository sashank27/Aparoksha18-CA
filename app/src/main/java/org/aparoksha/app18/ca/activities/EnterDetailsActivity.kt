package org.aparoksha.app18.ca.activities

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.activity_details.view.*
import org.aparoksha.app18.ca.R
import org.aparoksha.app18.ca.setIntentDetails

class EnterDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        title = "Personal Details"

        submit.setOnClickListener({
            if (!collegeName.text.equals("") && !userName.text.equals("") && !fullName.text.equals("") && gender_group.checkedRadioButtonId != -1) {
                if(gender_group.male.isChecked) {
                    val intent: Intent= setIntentDetails(intent,fullName.text.toString(),
                            collegeName.text.toString(),userName.text.toString(),"male")
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                } else {
                    val intent: Intent= setIntentDetails(intent,fullName.text.toString(),
                            collegeName.text.toString(),userName.text.toString(),"female")
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            }
        })
    }
}