package org.aparoksha.app18.ca.utils

import android.content.Context

import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.storage.StorageReference

import java.io.InputStream

/**
 * Created by sashank on 27/12/17.
 */
@GlideModule
class AparokshaGlideModule : AppGlideModule() {
    override fun registerComponents(context: Context?, glide: Glide?, registry: Registry?) {
        registry!!.append(StorageReference::class.java, InputStream::class.java,
                FirebaseImageLoader.Factory())
    }
}