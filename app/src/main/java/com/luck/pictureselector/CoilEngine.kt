package com.luck.pictureselector

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.Px
import coil.imageLoader
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.luck.picture.lib.engine.ImageEngine
import com.luck.picture.lib.interfaces.OnCallbackListener
import com.luck.picture.lib.utils.ActivityCompatHelper

/**
 * @author：luck
 * @date：2022/2/14 3:00 下午
 * @describe：CoilEngine
 */
class CoilEngine : ImageEngine {
    override fun loadImage(context: Context, url: String, imageView: ImageView) {
        if (!ActivityCompatHelper.assertValidRequest(context)) {
            return
        }
        val target = ImageRequest.Builder(context)
            .data(url)
            .target(imageView)
            .build()
        context.imageLoader.enqueue(target)
    }

    override fun loadImageBitmap(
        context: Context,
        url: String,
        maxWidth: Int,
        maxHeight: Int,
        call: OnCallbackListener<Bitmap>?
    ) {
        if (!ActivityCompatHelper.assertValidRequest(context)) {
            return
        }
        val builder = ImageRequest.Builder(context)
        if (maxWidth > 0 && maxHeight > 0) {
            builder.size(maxWidth, maxHeight)
        }
        builder.data(url)
        builder.target {
            call?.onCall(it.toBitmap())
        }
        val request = builder.build();
        context.imageLoader.enqueue(request)
    }

    fun Drawable.toBitmap(
        @Px width: Int = intrinsicWidth,
        @Px height: Int = intrinsicHeight,
        config: Bitmap.Config? = null
    ): Bitmap {
        if (this is BitmapDrawable) {
            if (config == null || bitmap.config == config) {
                // Fast-path to return original. Bitmap.createScaledBitmap will do this check, but it
                // involves allocation and two jumps into native code so we perform the check ourselves.
                if (width == intrinsicWidth && height == intrinsicHeight) {
                    return bitmap
                }
                return Bitmap.createScaledBitmap(bitmap, width, height, true)
            }
        }
        val oldLeft = bounds.left
        val oldTop = bounds.top
        val oldRight = bounds.right
        val oldBottom = bounds.bottom
        val bitmap = Bitmap.createBitmap(width, height, config ?: Bitmap.Config.ARGB_8888)
        setBounds(0, 0, width, height)
        draw(Canvas(bitmap))
        setBounds(oldLeft, oldTop, oldRight, oldBottom)
        return bitmap
    }


    override fun loadAlbumCover(context: Context, url: String, imageView: ImageView) {
        if (!ActivityCompatHelper.assertValidRequest(context)) {
            return
        }
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        val target = ImageRequest.Builder(context)
            .data(url)
            .transformations(RoundedCornersTransformation(8F))
            .size(180, 180)
            .placeholder(R.drawable.ps_image_placeholder)
            .target(imageView)
            .build()
        context.imageLoader.enqueue(target)
    }

    override fun loadGridImage(context: Context, url: String, imageView: ImageView) {
        if (!ActivityCompatHelper.assertValidRequest(context)) {
            return
        }
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        val target = ImageRequest.Builder(context)
            .data(url)
            .size(270, 270)
            .placeholder(R.drawable.ps_image_placeholder)
            .target(imageView)
            .build()
        context.imageLoader.enqueue(target)
    }


    override fun pauseRequests(context: Context?) {

    }

    override fun resumeRequests(context: Context?) {

    }
}