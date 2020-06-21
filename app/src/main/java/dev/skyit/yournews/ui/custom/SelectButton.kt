package dev.skyit.yournews.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import dev.skyit.yournews.R
import kotlinx.android.synthetic.main.select_button.view.*

class SelectButton : ConstraintLayout {
    constructor(context: Context) : super(context) {
        inflateView()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        inflateView()
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    ) {
        inflateView()
    }

    private fun inflateView() {
        View.inflate(context, R.layout.select_button, this)
    }

    fun getTitle() : String {
        return this.title_textview.text.toString()
    }

    fun setTitle(txt: String) {
        this.title_textview.setText(txt)
    }

    fun setSubtitle(txt: String) {
        this.subtitle_textview.setText(txt)
    }
}