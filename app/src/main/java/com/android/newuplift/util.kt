package com.android.newuplift

import android.graphics.drawable.Animatable
import android.view.View
import android.widget.ImageView
import android.widget.Spinner

fun pickMood(choiceSpinner : String) : String{
    var tag : String = "None"
    when (choiceSpinner) {
        "Happy" -> tag = "friendship|happiness|love"
        "Lost" -> tag = "wisdom"
        "Motivated" -> tag = "motivational|inspirational|success|change|competition"
        "Bored" -> tag = "famous-quotes"

    }
    return tag
}

private fun startFlameAnimation(view: View) {
    val flameImageView = view.findViewById<ImageView>(R.id.my_image_view)
    val drawable = flameImageView?.drawable
    if (drawable is Animatable) {
        drawable.start()
    }
}