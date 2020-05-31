package dev.skyit.yournews.ui.main.bookmarks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.skyit.yournews.BaseFragment
import dev.skyit.yournews.databinding.NewsBookmarksFragmentBinding

class NewsBookmarksFragment: BaseFragment() {

    private lateinit var binding: NewsBookmarksFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NewsBookmarksFragmentBinding.inflate(inflater)
        return binding.root
    }


}