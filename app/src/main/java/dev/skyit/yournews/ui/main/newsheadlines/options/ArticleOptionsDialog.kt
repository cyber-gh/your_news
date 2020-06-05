package dev.skyit.yournews.ui.main.newsheadlines.options

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.skyit.yournews.R
import dev.skyit.yournews.databinding.ArticleItemOptionsDialogBinding

class ArticleOptionsDialog : BottomSheetDialogFragment() {
    private lateinit var binding: ArticleItemOptionsDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ArticleItemOptionsDialogBinding.inflate(inflater)
        return binding.root
    }


    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }

}