package dev.skyit.yournews.ui.main.allnews

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.skyit.yournews.BaseFragment
import dev.skyit.yournews.databinding.NewsListFragmentBinding
import dev.skyit.yournews.repository.preferences.IUserPreferences
import dev.skyit.yournews.ui.ArticleMinimal
import dev.skyit.yournews.ui.main.MainFragmentDirections
import dev.skyit.yournews.ui.main.bookmarks.INewsOptionHandler
import dev.skyit.yournews.ui.main.bookmarks.NewsArticlesAdapter
import dev.skyit.yournews.ui.main.newsheadlines.options.ArticleOptionsDialog
import dev.skyit.yournews.ui.utils.mainNavController
import dev.skyit.yournews.ui.utils.setItemSpacing
import dev.skyit.yournews.utils.toArrayList
import java.lang.ref.WeakReference
import javax.inject.Inject


@AndroidEntryPoint
class ArticleListFragment : BaseFragment(), INewsOptionHandler {

    private lateinit var src: ArticlesSource

    companion object {
        fun newInstance(src: ArticlesSource) : ArticleListFragment {
            val frag = ArticleListFragment()
            frag.src = src
            return frag
        }
    }

    private lateinit var binding: NewsListFragmentBinding
    private lateinit var adapter: NewsArticlesAdapter

    private val vModel: ArticleListViewModel by viewModels()

    @Inject
    protected lateinit var userPreferences: IUserPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NewsListFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vModel.setSource(src)


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

        vModel.articles.observe(viewLifecycleOwner, Observer {
            adapter.updateData(it.toArrayList())
        })

        vModel.loadArticles()


        binding.swipeRefresh.setOnRefreshListener {
            vModel.loadArticles()
            binding.swipeRefresh.isRefreshing = false
        }



    }

    override fun onResume() {
        super.onResume()

        val cardType = if (userPreferences.useMiniCards) 0 else 1
        adapter.updateViewType(cardType)
    }

    override fun handleOptions(article: ArticleMinimal) {
        mainNavController.navigate(
            MainFragmentDirections.actionMainFragmentToArticleOptionsDialog(
                article.extended, ArticleOptionsDialog.OptionsFor.NEW_ARTICLE
            )
        )
    }
}