package com.ryunen344.twikot

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import java.io.File

@BindingAdapter("localImage")
fun localImage(imageView : ImageView, localUri : String?) {
    localUri ?: return
    if (File(localUri).exists()) {
        imageView.setImageURI(localUri.toUri())
    }
}