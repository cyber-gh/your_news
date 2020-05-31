package dev.skyit.yournews

import androidx.fragment.app.Fragment
import dev.skyit.yournews.ui.custom.AppToolbar

open class BaseFragment : Fragment() {
    protected val appBar: AppToolbar?
    get() = (activity as? MainActivity)?.toolbar
}