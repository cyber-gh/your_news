package dev.skyit.yournews.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.widget.ScrollView

typealias OnScrollChangedListener = (dx: Int, dy: Int) -> Unit

class ObservableScrollView: ScrollView {

    constructor(context: Context) : super(context) {
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    ) {
    }

    private var listener: OnScrollChangedListener? = null

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        listener?.invoke(l - oldl, t - oldt)
    }

    fun setOnScrollListener(listener: OnScrollChangedListener) {
        this.listener = listener
    }


}