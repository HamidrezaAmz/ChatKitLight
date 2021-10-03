package ir.vasl.chatkitlight.utils

import android.content.Context
import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat
import ir.vasl.chatkitlight.R


class TypefaceHelper {

    companion object {
        fun getTypeFace(context: Context): Typeface? = ResourcesCompat.getFont(context, R.font.iransans)
    }
}