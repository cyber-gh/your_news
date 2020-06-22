package dev.skyit.yournews.ui.main.bookmarks

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import coil.api.load
import dagger.hilt.android.AndroidEntryPoint
import dev.skyit.yournews.BaseFragment
import dev.skyit.yournews.R
import dev.skyit.yournews.databinding.NewsArticleListItemBinding
import dev.skyit.yournews.databinding.NewsArticleListItemSmallBinding
import dev.skyit.yournews.databinding.NewsBookmarksFragmentBinding
import dev.skyit.yournews.repository.preferences.IUserPreferences
import dev.skyit.yournews.ui.ArticleMinimal
import dev.skyit.yournews.ui.main.MainFragmentDirections
import dev.skyit.yournews.ui.main.newsheadlines.options.ArticleOptionsDialog
import dev.skyit.yournews.ui.main.newsheadlines.options.SharedArticleOptionViewModel
import dev.skyit.yournews.ui.utils.*
import dev.skyit.yournews.utils.toArrayList
import java.lang.IllegalArgumentException
import java.lang.ref.WeakReference
import javax.inject.Inject


interface INewsOptionHandler {
    fun handleOptions(article: ArticleMinimal)
}

@AndroidEntryPoint
class NewsBookmarksFragment: BaseFragment(), INewsOptionHandler {

    private lateinit var binding: NewsBookmarksFragmentBinding

    private val vModel: NewsBookmarksViewModel by viewModels()
    private val optionsModel: SharedArticleOptionViewModel by activityViewModels()
    private lateinit var adapter: NewsArticlesAdapter

    @Inject
    protected lateinit var userPreferences: IUserPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NewsBookmarksFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cardType = if (userPreferences.useMiniCards) 0 else 1
        adapter = NewsArticlesAdapter(cardType, onItemClick = {
            mainNavController.navigate(
                MainFragmentDirections.actionMainFragmentToWebFragment(it.url)
            )
        }, optionsHandler = WeakReference(this))


        binding.recyclerList.adapter = adapter
        val nrColumns = if (requireContext().resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 1 else 2
        binding.recyclerList.layoutManager = GridLayoutManager(requireContext(), nrColumns)
        binding.recyclerList.setItemSpacing()

        vModel.data.observe(viewLifecycleOwner, Observer {
            adapter.updateData(it.toArrayList())
        })

        vModel.loadData()


        binding.swipeRefresh.setOnRefreshListener {
            vModel.loadData()
            binding.swipeRefresh.isRefreshing = false
        }

        optionsModel.articleDeletedEvent.observe(viewLifecycleOwner, Observer {
            vModel.loadData()
        })

    }

    override fun onResume() {
        super.onResume()

        val cardType = if (userPreferences.useMiniCards) 0 else 1
        adapter.updateViewType(cardType)
    }

    override fun handleOptions(article: ArticleMinimal) {
        mainNavController.navigate(
            MainFragmentDirections.actionMainFragmentToArticleOptionsDialog(
                article.extended, ArticleOptionsDialog.OptionsFor.BOOKMARKED_ARTICLE
            )
        )
    }


}

class NewsArticlesViewFactory(
    private var optionsHandler: WeakReference<INewsOptionHandler>
): ElementHolderFactory<ArticleMinimal>() {
    override fun createHolder(
        inflater: LayoutInflater,
        viewType: Int
    ): BaseViewHolder<ArticleMinimal> {
        return when(viewType) {
            0 -> {
                val smallBinding = NewsArticleListItemSmallBinding.inflate(inflater)
                SmallItemViewHolder(smallBinding)
            }
            1 -> {
                val normalBinding = NewsArticleListItemBinding.inflate(inflater)
                NormalItemViewHolder(normalBinding)

            }
            else -> throw IllegalArgumentException("Unsupported view type")
        }
    }

    private inner class SmallItemViewHolder(private val binding: NewsArticleListItemSmallBinding)
        : BaseViewHolder<ArticleMinimal>(binding.root) {

        override fun bind(item: ArticleMinimal) {
            super.bind(item)
            binding.data = item
            if (item.imageLink != null) {
                binding.imageView.load(item.imageLink) {
                    crossfade(true)
                    placeholder(R.drawable.news_article_placeholder)

                }
            }
            binding.optionsBtn.setOnClickListener {
                optionsHandler.get()?.handleOptions(item)
            }
        }

    }

    private inner class NormalItemViewHolder(private val binding: NewsArticleListItemBinding)
        : BaseViewHolder<ArticleMinimal>(binding.root) {
        override fun bind(item: ArticleMinimal) {
            super.bind(item)
            binding.data = item
            if (item.imageLink != null) {
                binding.imageView.load(item.imageLink) {
                    crossfade(true)
                    placeholder(R.drawable.news_article_placeholder)

                }
            }
            binding.optionsBtn.setOnClickListener {
                optionsHandler.get()?.handleOptions(item)
            }
        }
    }
}

class NewsArticlesAdapter(
    private var vType: Int,
    onItemClick: (ArticleMinimal) -> Unit,
    optionsHandler: WeakReference<INewsOptionHandler>
): AdvancedRecyclerAdapter<ArticleMinimal>(
    elementHolderFactory = NewsArticlesViewFactory(optionsHandler),
    onItemClicked = onItemClick) {

    override fun extractId(item: ArticleMinimal): Any {
        return item.url
    }

    override fun getItemViewType(position: Int, element: ArticleMinimal): Int {
        return vType
    }

    fun updateViewType(newType: Int) {
        if (newType != vType) {
            vType = newType
            notifyDataSetChanged()
        }
    }

}



