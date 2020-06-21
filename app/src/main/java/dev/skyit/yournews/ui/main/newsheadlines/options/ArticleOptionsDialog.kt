package dev.skyit.yournews.ui.main.newsheadlines.options

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import dev.skyit.yournews.R
import dev.skyit.yournews.databinding.ArticleItemOptionsDialogBinding
import dev.skyit.yournews.ui.utils.errAlert
import dev.skyit.yournews.ui.utils.snack


@AndroidEntryPoint
class ArticleOptionsDialog : BottomSheetDialogFragment() {
    private lateinit var binding: ArticleItemOptionsDialogBinding

    private val args: ArticleOptionsDialogArgs by navArgs()

    private val vModel: ArticleOptionViewModel by viewModels()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vModel.inject(args.article)
        binding.bookmarkBtn.setOnClickListener {
            vModel.bookmark()

            snack("Article bookmarked")
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