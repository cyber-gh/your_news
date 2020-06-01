package dev.skyit.yournews.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.Toolbar
import dev.skyit.yournews.R
import kotlinx.android.synthetic.main.app_bar_view.view.*

class AppMainToolbar: Toolbar {

    private fun inflateView() {
        View.inflate(context, R.layout.app_bar_view, this)
    }


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

    fun onSearch(callback: () -> Unit) {
        this.imageButton3.setOnClickListener { callback() }
    }
}