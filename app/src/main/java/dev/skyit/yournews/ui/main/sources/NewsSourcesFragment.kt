package dev.skyit.yournews.ui.main.sources

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.skyit.yournews.BaseFragment
import dev.skyit.yournews.databinding.NewsSourcesFragmentBinding

class NewsSourcesFragment: BaseFragment() {

    private lateinit var binding: NewsSourcesFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NewsSourcesFragmentBinding.inflate(inflater)
        return binding.root
    }


}