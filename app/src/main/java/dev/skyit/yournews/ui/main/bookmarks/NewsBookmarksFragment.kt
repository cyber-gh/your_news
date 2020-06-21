package dev.skyit.yournews.ui.main.bookmarks

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import dagger.hilt.android.AndroidEntryPoint
import dev.skyit.yournews.BaseFragment
import dev.skyit.yournews.R
import dev.skyit.yournews.databinding.NewsArticleListItemBinding
import dev.skyit.yournews.databinding.NewsArticleListItemBindingImpl
import dev.skyit.yournews.databinding.NewsArticleListItemSmallBinding
import dev.skyit.yournews.databinding.NewsBookmarksFragmentBinding
import dev.skyit.yournews.repository.database.ArticleEntity
import dev.skyit.yournews.repository.preferences.IUserPreferences
import dev.skyit.yournews.ui.ArticleMinimal
import dev.skyit.yournews.ui.utils.*
import dev.skyit.yournews.utils.toArrayList
import java.lang.IllegalArgumentException
import javax.inject.Inject


@AndroidEntryPoint
class NewsBookmarksFragment: BaseFragment() {

    private lateinit var binding: NewsBookmarksFragmentBinding

    private val vModel: NewsBookmarksViewModel by viewModels()
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
        adapter = NewsArticlesAdapter(cardType)


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

    }

    override fun onResume() {
        super.onResume()

        val cardType = if (userPreferences.useMiniCards) 0 else 1
        adapter.updateViewType(cardType)
    }


}

class NewsArticlesViewFactory: ElementHolderFactory<ArticleMinimal>() {
    override fun createHolder(
        inflater: LayoutInflater,
        viewType: Int
    ): BaseViewHolder<ArticleMinimal> {
        return when(viewType) {
            0 -> createSmallItem(inflater)
            1 -> createNormalItem(inflater)
            else -> throw IllegalArgumentException("Unsupported view type")
        }
    }

    private fun createNormalItem(inflater: LayoutInflater) : BaseViewHolder<ArticleMinimal> {
        val binding = NewsArticleListItemBinding.inflate(inflater)

        return object : BaseViewHolder<ArticleMinimal>(binding.root) {
            override fun bind(item: ArticleMinimal) {
                binding.data = item
                if (item.imageLink != null) {
                    binding.imageView.load(item.imageLink) {
                        crossfade(true)
                        placeholder(R.drawable.news_article_placeholder)

                    }
                }
            }

        }
    }

    private fun createSmallItem(inflater: LayoutInflater) : BaseViewHolder<ArticleMinimal> {
        val binding = NewsArticleListItemSmallBinding.inflate(inflater)

        return object : BaseViewHolder<ArticleMinimal>(binding.root) {
            override fun bind(item: ArticleMinimal) {
                binding.data = item
                if (item.imageLink != null) {
                    binding.imageView.load(item.imageLink) {
                        crossfade(true)
                        placeholder(R.drawable.news_article_placeholder)

                    }
                }
            }

        }
    }



}

class NewsArticlesAdapter(
    private var vType: Int
): AdvancedRecyclerAdapter<ArticleMinimal>(elementHolderFactory = NewsArticlesViewFactory()) {
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



