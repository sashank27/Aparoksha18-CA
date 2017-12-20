package org.aparoksha.app18.ca.adapters

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.uploads_container.view.*
import org.aparoksha.app18.ca.R
import org.aparoksha.app18.ca.models.Image
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by akshat on 12/12/17.
 */

class UploadsAdapter(options: FirebaseRecyclerOptions<Image>,
                     private val storageReference: StorageReference,
                     private val context: Context)
    : FirebaseRecyclerAdapter<Image, UploadsAdapter.UploadsViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UploadsViewHolder {
        return UploadsViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.uploads_container, parent, false)
        )
    }

    override fun onBindViewHolder(holder: UploadsViewHolder, position: Int, model: Image) {
        holder.bindView(model, storageReference, context)
    }


    class UploadsViewHolder(private var mView: View) : RecyclerView.ViewHolder(mView) {

        fun bindView(image: Image, storageRef: StorageReference, context: Context) {
            val imageRef = storageRef.child(image.path)
            Glide.with(context)
                    .load(imageRef)
                    .into(mView.image)

            val colorAccent = ContextCompat.getColor(context, R.color.colorAccent)
            val colorGreen = ContextCompat.getColor(context, R.color.green)

            mView.status.text = if (image.verified) "Verified" else "Unverified"
            mView.status.setTextColor(if (image.verified) colorGreen else colorAccent)
            val uploadDate = Timestamp(imageRef.name.toLong()) as Date
            mView.uploadDateTV.text = "Uploaded on ${SimpleDateFormat("MMM d, yyyy").format(uploadDate)}"
        }

    }
}