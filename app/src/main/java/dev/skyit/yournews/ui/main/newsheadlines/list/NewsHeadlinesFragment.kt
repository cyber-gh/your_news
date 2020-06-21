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
import dev.skyit.yournews.databinding.NewsHeadlinesFragmentBinding
import dev.skyit.yournews.ui.ArticleMinimal
import dev.skyit.yournews.ui.main.MainFragmentDirections
import dev.skyit.yournews.ui.utils.*


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

        adapter = NewsHeadlinesAdapter (onItemClick = {
                mainNavController.navigate(MainFragmentDirections.actionMainFragmentToWebFragment(it.url))
            },onItemOptionsClick =  {
                mainNavController.navigate(
                    MainFragmentDirections.actionMainFragmentToArticleOptionsDialog(it.extended)
                )
            })
        binding.recyclerView.adapter = adapter
        val nrColumns = if (requireContext().resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 1 else 2
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), nrColumns)
        binding.recyclerView.setItemSpacing()

        vModel.newsPagedLive.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

}

class NewsHeadlinesAdapter(
    private val onItemClick: (ArticleMinimal) -> Unit,
    private val onItemOptionsClick: (ArticleMinimal) -> Unit
)
    : PagedListAdapter<ArticleMinimal, NewsHeadlinesAdapter.NewsHeadlinesViewHolder>(buildDiffUtill { this.title }) {

    inner class NewsHeadlinesViewHolder(private val binding: NewsArticleListItemBinding):
            RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ArticleMinimal) {
            binding.data = data
            binding.executePendingBindings()
            binding.root.setOnClickListener {
                onItemClick(data)
            }
            binding.imageButton2.setOnClickListener {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHeadlinesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = NewsArticleListItemBinding.inflate(inflater)
        return NewsHeadlinesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsHeadlinesViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }



}
