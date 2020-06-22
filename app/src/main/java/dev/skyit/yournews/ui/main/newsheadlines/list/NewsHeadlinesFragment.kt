package dev.skyit.yournews.ui.main.newsheadlines.list

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.skyit.yournews.BaseFragment
import dev.skyit.yournews.R
import dev.skyit.yournews.databinding.NewsArticleListItemBinding
import dev.skyit.yournews.databinding.NewsArticleListItemSmallBinding
import dev.skyit.yournews.databinding.NewsHeadlinesFragmentBinding
import dev.skyit.yournews.ui.ArticleMinimal
import dev.skyit.yournews.ui.main.MainFragmentDirections
import dev.skyit.yournews.ui.utils.*
import java.lang.IllegalArgumentException


@AndroidEntryPoint
class NewsHeadlinesFragment : BaseFragment() {
    private lateinit var binding: NewsHeadlinesFragmentBinding

    private lateinit var adapter: NewsHeadlinesAdapter
    private val vModel: NewsHeadlinesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NewsHeadlinesFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        vModel.reloadSettings()

        val currentCardType = if (vModel.useMiniCards) 0 else 1
        if (currentCardType != adapter.cardType) {
            setupRecyclerView()
        }

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vModel.internetReconnected.observe(viewLifecycleOwner, Observer {
            Snackbar.make(binding.root, getString(R.string.recnnect_to_internet), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.refresh)) {
                    vModel.refreshList()
                }.show()
        })

        binding.swipeRefresh.setOnRefreshListener {
            vModel.refreshList()
        }

        vModel.refreshStatusLive.observe(viewLifecycleOwner, Observer {
            when (it) {
                LoadStatus.COMPLETED -> snack(getString(R.string.data_reloaded))
                LoadStatus.FAILED -> errAlert(getString(R.string.error_loading_data))
            }

            binding.swipeRefresh.isRefreshing = it == LoadStatus.REFRESHING
        })

        setupRecyclerView()
        val nrColumns = if (requireContext().resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 1 else 2
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), nrColumns)
        binding.recyclerView.setItemSpacing()

        vModel.newsPagedLive.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

    private fun setupRecyclerView() {

        adapter = NewsHeadlinesAdapter(onItemClick = {
            mainNavController.navigate(MainFragmentDirections.actionMainFragmentToWebFragment(it.url))
        }, onItemOptionsClick = {
            mainNavController.navigate(
                MainFragmentDirections.actionMainFragmentToArticleOptionsDialog(it.extended)
            )
        }, cardType = if (vModel.useMiniCards) 0 else 1)
        binding.recyclerView.adapter = adapter
    }

}



class NewsHeadlinesAdapter(
    private val onItemClick: (ArticleMinimal) -> Unit,
    private val onItemOptionsClick: (ArticleMinimal) -> Unit,
    val cardType: Int = normalView
) : PagedListAdapter<ArticleMinimal, BaseViewHolder<ArticleMinimal>>(buildDiffUtill { this.title }) {

    companion object {
        val miniCardView: Int = 0
        val normalView: Int = 1
    }

    inner class NormalNewsHeadlinesViewHolder(
        private val binding: NewsArticleListItemBinding
    ): BaseViewHolder<ArticleMinimal>(binding.root) {
        override fun bind(data: ArticleMinimal) {
            binding.data = data
            binding.executePendingBindings()
            binding.root.setOnClickListener {
                onItemClick(data)
            }
            binding.optionsBtn.setOnClickListener {
                onItemOptionsClick(data)
            }

            if (data.imageLink != null) {
                binding.imageView.load(data.imageLink) {
                    crossfade(true)
                    placeholder(R.drawable.news_article_placeholder)

                }
            }
        }
    }

    inner class MiniNewsHeadlinesViewHolder(
        private val binding: NewsArticleListItemSmallBinding
    ): BaseViewHolder<ArticleMinimal>(binding.root) {
        override fun bind(data: ArticleMinimal) {
            binding.data = data
            binding.executePendingBindings()
            binding.root.setOnClickListener {
                onItemClick(data)
            }
            binding.optionsBtn.setOnClickListener {
                onItemOptionsClick(data)
            }

            if (data.imageLink != null) {
                binding.imageView.load(data.imageLink) {
                    crossfade(true)
                    placeholder(R.drawable.news_article_placeholder)

                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return cardType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ArticleMinimal> {
        val inflater = LayoutInflater.from(parent.context)
        return when(viewType) {
            miniCardView -> {
                val binding = NewsArticleListItemSmallBinding.inflate(inflater)
                MiniNewsHeadlinesViewHolder(binding)
            }
            normalView -> {
                val binding = NewsArticleListItemBinding.inflate(inflater)
                NormalNewsHeadlinesViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Not supported view type")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<ArticleMinimal>, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }





}
