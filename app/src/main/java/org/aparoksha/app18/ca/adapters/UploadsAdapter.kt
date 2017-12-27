package org.aparoksha.app18.ca.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.uploads_container.view.*
import org.aparoksha.app18.ca.GlideApp
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
                     private val context: Context,
                     private val noItemView: TextView)
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
            GlideApp.with(context)
                    .load(imageRef)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            mView.progress_bar.visibility = View.GONE
                            mView.error_loading.visibility = View.VISIBLE
                            return false
                        }

                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            mView.progress_bar.visibility = View.GONE
                            mView.error_loading.visibility = View.GONE
                            return false
                        }

                    })
                    .into(mView.image)

            val colorAccent = ContextCompat.getColor(context, R.color.colorAccent)
            val colorGreen = ContextCompat.getColor(context, R.color.green)

            mView.status.text = if (image.verified) "Verified" else "Unverified"
            mView.status.setTextColor(if (image.verified) colorGreen else colorAccent)
            val uploadDate = Timestamp(imageRef.name.toLong()) as Date
            mView.uploadDateTV.text = "Uploaded on ${SimpleDateFormat("MMM d, yyyy").format(uploadDate)}"
        }

    }

    override fun onDataChanged() {
        super.onDataChanged()
        noItemView.visibility = if (itemCount == 0) View.VISIBLE else View.GONE
    }
}