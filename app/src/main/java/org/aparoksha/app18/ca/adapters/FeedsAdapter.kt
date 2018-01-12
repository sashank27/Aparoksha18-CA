package org.aparoksha.app18.ca.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import org.aparoksha.app18.ca.R
import org.aparoksha.app18.ca.models.FeedsData
import com.leocardz.link.preview.library.SourceContent
import com.leocardz.link.preview.library.LinkPreviewCallback
import com.leocardz.link.preview.library.TextCrawler
import kotlinx.android.synthetic.main.feed_container.view.*
import android.graphics.Bitmap
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import android.widget.ImageView
import android.widget.TextView
import com.koushikdutta.urlimageviewhelper.UrlImageViewCallback
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper



/**
 * Created by akshat on 11/1/18.
 */

class FeedsAdapter (options: FirebaseRecyclerOptions<FeedsData>,
                    private val context: Context,
                    private val noFeedsView: TextView)
    : FirebaseRecyclerAdapter<FeedsData, FeedsAdapter.FeedsViewHolder>(options)  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedsViewHolder{
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.feed_container, parent, false)

        return FeedsViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeedsViewHolder, position: Int, model: FeedsData) {
        holder.bindView(model,position,context)
    }

    inner class FeedsViewHolder (private var mView: View) : RecyclerView.ViewHolder(mView) {

        fun bindView (feed: FeedsData, position: Int,context:Context) {

            val textCrawler = TextCrawler()

            val linkPreviewCallback = object : LinkPreviewCallback {
                override fun onPre() {
                    mView.image_progress.visibility = View.VISIBLE
                    mView.image_post_set.visibility = View.GONE
                }

                override fun onPos(sourceContent: SourceContent, b: Boolean) {

                    mView.image_progress.visibility = View.GONE
                    mView.image_post_set.visibility = View.VISIBLE

                    if(!sourceContent.images.isEmpty()) {
                        UrlImageViewHelper.setUrlDrawable(mView.image_post_set, sourceContent.images[0],
                                object : UrlImageViewCallback {

                                    override fun onLoaded(imageView: ImageView,
                                                          loadedBitmap: Bitmap?, url: String,
                                                          loadedFromCache: Boolean) {
                                        if (loadedBitmap != null) {
                                            mView.image_post_set.setImageBitmap(loadedBitmap)
                                        }
                                    }
                                })
                    }
                }
            }

            mView.title.text = feed.title
            mView.description.text = feed.description
            textCrawler.makePreview( linkPreviewCallback, feed.url)
            mView.setOnClickListener { openChromeTab(feed.url) }
        }

        fun openChromeTab(url: String){
            val builder = CustomTabsIntent.Builder()
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(context, Uri.parse(url))
        }
    }

    override fun onDataChanged() {
        super.onDataChanged()
        noFeedsView.visibility = if (itemCount == 0) View.VISIBLE else View.GONE
    }
}