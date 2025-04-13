package com.android.newuplift.utility

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Animatable
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.newuplift.R

fun pickMood(choiceSpinner: String): String {
    return when (choiceSpinner) {
        "Happy" -> "happiness"
        "Lost" -> "wisdom"
        "Motivated" -> "motivation"
        "Bored" -> "general"
        else -> "happiness"
    }
}
fun Fragment.showToast(message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
fun Activity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}



fun EditText.isBlank() : Boolean{
    return this.text.toString().isBlank()
}

fun EditText.stringed() : String{
    return this.text.toString()
}



//private fun startFlameAnimation(view: View) {
//    val flameImageView = view.findViewById<ImageView>(R.id.my_image_view)
//    val drawable = flameImageView?.drawable
//    if (drawable is Animatable) {
//        drawable.start()
//    }
//}