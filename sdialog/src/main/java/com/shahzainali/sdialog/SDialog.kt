package com.shahzainali.sdialog

import android.animation.Animator
import android.animation.ValueAnimator
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Handler
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.animation.LinearInterpolator
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.message_dialog.*


class SDialog(
        context: Context,
        title: String,
        message: String,
        duration: Long,
        speed: Long,
        isCancellable: Boolean,
        backColor: Int,
        titleColor: Int,
        messageColor: Int,
        image: Int?,
        imageTint: Int
) {


    data class Builder(
            var context: Context,
            private var title: String = "Title",
            private var message: String = "Message",
            private var duration: Long = 3000,
            private var speed: Long = 700,
            private var isCancellable: Boolean = true,
            private var backColor: Int = Color.DKGRAY,
            private var titleColor: Int = Color.WHITE,
            private var messageColor: Int = Color.WHITE,
            private var image: Int? = null,
            private var imageTint: Int = Color.BLACK
    ) {
        fun setTitle(title: String) = apply { this.title = title }
        fun setMessage(message: String) = apply { this.message = message }
        fun setDuration(duration: Long) = apply { this.duration = duration }
        fun setSpeed(speed: Long) = apply { this.speed = speed }
        fun setCancellable(isCancellable: Boolean) = apply { this.isCancellable = isCancellable }
        fun setBackgroundColor(backColor: Int) = apply { this.backColor = backColor }
        fun setTitleColor(titleColor: Int) = apply { this.titleColor = titleColor }
        fun setMessageColor(descriptionColor: Int) = apply { this.messageColor = descriptionColor }
        fun setImage(image: Int?) = apply { this.image = image }
        fun setImageTint(imageTint: Int) = apply { this.imageTint = imageTint }
        fun build() = SDialog(context, title, message, duration, speed, isCancellable, backColor, titleColor, messageColor, image, imageTint)

    }

    init {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        dialog.setContentView(R.layout.message_dialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val window = dialog.window
        window!!.setGravity(Gravity.TOP)

        dialog.title.text = title
        dialog.desc.text = message

        dialog.title.setTextColor(titleColor)
        dialog.desc.setTextColor(messageColor)

        dialog.setCancelable(isCancellable)


        val ddd = dialog.findViewById<RelativeLayout>(R.id.root)

        if (image == null) {
            dialog.image.visibility = View.GONE
        } else {
            dialog.image.visibility = View.VISIBLE
            dialog.image.setImageResource(image)
            dialog.image.setColorFilter(imageTint)
        }

        ddd.background = generateDrawable(backColor)
        dialog.show()


        animator(dialog, ddd, speed, false)


        Handler().postDelayed({
            animator(dialog, ddd, speed, true)
        }, duration)

    }

    private fun animator(
            dialog: Dialog,
            dialogLayout: RelativeLayout,
            speed: Long,
            isReverse: Boolean
    ) {
        var valueAnimator: ValueAnimator = if (!isReverse)
            ValueAnimator.ofFloat(-360f, 0f)
        else
            ValueAnimator.ofFloat(0f, -360f)

        valueAnimator.addUpdateListener {
            val value = it.animatedValue as Float
            dialogLayout.translationY = value
        }

        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.duration = speed

        valueAnimator.start()

        valueAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {
            }

            override fun onAnimationEnd(p0: Animator?) {
                if (isReverse)
                    dialog.dismiss()
            }

            override fun onAnimationCancel(p0: Animator?) {
            }

            override fun onAnimationStart(p0: Animator?) {
            }

        })
    }

    private fun generateDrawable(backColor: Int): GradientDrawable {
        val gd = GradientDrawable()
        gd.setColor(backColor)
        gd.cornerRadius = 15f
        return gd
    }


}