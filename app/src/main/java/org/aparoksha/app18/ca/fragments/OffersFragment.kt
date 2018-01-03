package org.aparoksha.app18.ca.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_offer.view.*
import org.aparoksha.app18.ca.R

/**
 * Created by akshat on 3/1/18.
 */
 class OffersFragment: Fragment() {
    private var head = ""
    private var image = 0
    private var foot = ""

    companion object {
        fun instantiate(head: String,image: Int, foot:String) : OffersFragment {
            val fragment = OffersFragment()
            var args = Bundle()
            args.putInt("image",image)
            args.putString("head",head)
            args.putString("foot",foot)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        image = arguments!!.getInt("image")
        head = arguments!!.getString("head")
        foot = arguments!!.getString("foot")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view =  inflater.inflate(R.layout.fragment_offer,container,false)
        view.textView9.text = head
        view.textView10.text = foot
        Glide.with(activity)
                .load(image)
                .into(view.imageView1)
        return view
    }
}