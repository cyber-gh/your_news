package dev.skyit.yournews.ui.main.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import coil.api.load
import com.soywiz.klock.DateTime
import dev.skyit.yournews.BaseFragment
import dev.skyit.yournews.databinding.ArticleDetailsFragmentBinding
import dev.skyit.yournews.ui.utils.mainNavController
import dev.skyit.yournews.ui.utils.relativeTime

class ArticleDetailsFragment: BaseFragment() {

    private lateinit var binding: ArticleDetailsFragmentBinding
    private val args: ArticleDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ArticleDetailsFragmentBinding.inflate(inflater)
        binding.article = args.article
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args.article.urlToImage?.let {
            binding.imageView2.load(it) {
                crossfade(300)
            }
        }

        binding.textView9.text = DateTime.Companion.fromUnix(args.article.publishedAt).relativeTime()

        binding.materialToolbar.onBackPressed {
            mainNavController.navigateUp()
        }

        binding.materialToolbar.setTitle("Article")

        binding.button.setOnClickListener {
            mainNavController.navigate(
                ArticleDetailsFragmentDirections
                    .actionArticleDetailsFragmentToWebFragment(args.article.url))
        }
    }
}