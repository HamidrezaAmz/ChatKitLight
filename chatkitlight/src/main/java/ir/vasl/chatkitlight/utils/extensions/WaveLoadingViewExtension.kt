package ir.vasl.chatkitlight.utils.extensions

import android.graphics.Paint
import android.graphics.Typeface
import me.itangqi.waveloadingview.WaveLoadingView

fun WaveLoadingView.setTypeface(typeface: Typeface?) {
    typeface?.let { customTypeface ->
        getCenterTitlePaint().let { paint ->
            paint.typeface = customTypeface
            this.invalidate()
        }
    }
}

fun WaveLoadingView.getCenterTitlePaint(): Paint {
    return WaveLoadingView::class.java.getDeclaredField("mCenterTitlePaint").let {
        it.isAccessible = true
        val value = it.get(this)
        return@let value as Paint
    }
}


