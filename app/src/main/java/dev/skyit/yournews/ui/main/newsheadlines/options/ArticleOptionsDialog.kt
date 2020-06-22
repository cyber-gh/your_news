package dev.skyit.yournews.ui.main.newsheadlines.options

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import dev.skyit.yournews.R
import dev.skyit.yournews.databinding.ArticleItemOptionsDialogBinding
import dev.skyit.yournews.ui.utils.errAlert
import dev.skyit.yournews.ui.utils.toastl


@AndroidEntryPoint
class ArticleOptionsDialog : BottomSheetDialogFragment() {
    private lateinit var binding: ArticleItemOptionsDialogBinding

    enum class OptionsFor {
        NEW_ARTICLE, BOOKMARKED_ARTICLE
    }

    private val args: ArticleOptionsDialogArgs by navArgs()

    private val vModel: ArticleOptionViewModel by viewModels()
    private val sharedVModel: SharedArticleOptionViewModel by activityViewModels()

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

    private fun configureByMode(optionsFor: OptionsFor) {
        if (optionsFor == OptionsFor.NEW_ARTICLE) {
            binding.removeBookmarkBtn.isVisible = false
        }

        if (optionsFor == OptionsFor.BOOKMARKED_ARTICLE) {
            binding.bookmarkBtn.isVisible = false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureByMode(args.target)

        vModel.inject(args.article)
        binding.bookmarkBtn.setOnClickListener {
            vModel.bookmark()

            context?.toastl("Article bookmarked")
            dismiss()

        }

        binding.removeBookmarkBtn.setOnClickListener {
            vModel.removeBookmark()

            context?.toastl("Article removed")
            sharedVModel.articleDeletedEvent.value = Unit
            dismiss()
        }

        binding.shareBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, args.article.url)
            intent.putExtra(Intent.EXTRA_SUBJECT, args.article.title)
            startActivity(intent)
        }

        binding.hideFromSourceBtn.setOnClickListener {
            errAlert(getString(R.string.not_available))
        }

        binding.visitSourceBtn.setOnClickListener {
            vModel.getSource()
        }

        vModel.sourceLinkLive.observe(viewLifecycleOwner, Observer {
            findNavController().navigate(
                ArticleOptionsDialogDirections.actionArticleOptionsDialogToWebFragment(it)
            )
        })


    }

}