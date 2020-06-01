package dev.skyit.yournews.ui.main.web

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import dev.skyit.yournews.BaseFragment
import dev.skyit.yournews.databinding.WebFragmentBinding
import dev.skyit.yournews.ui.utils.mainNavController

class WebFragment: BaseFragment() {

    private lateinit var binding: WebFragmentBinding

    private val args: WebFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = WebFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.webView.settings.javaScriptEnabled = true
        binding.webView.loadUrl(args.url)

        binding.materialToolbar2.onBackPressed {
            mainNavController.navigateUp()
        }

    }
}