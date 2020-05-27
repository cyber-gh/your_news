package dev.skyit.yournews.ui.main.newsheadlines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import coil.transform.CircleCropTransformation
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import dev.skyit.yournews.BaseFragment
import dev.skyit.yournews.R
import dev.skyit.yournews.databinding.NewsArticleListItemBinding
import dev.skyit.yournews.databinding.NewsHeadlinesFragmentBinding
import dev.skyit.yournews.ui.ArticleMinimal
import dev.skyit.yournews.ui.utils.RecyclerAdapter
import dev.skyit.yournews.ui.utils.buildDiffUttil
import dev.skyit.yournews.ui.utils.setItemSpacing
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewsHeadlinesFragment : BaseFragment() {
    private lateinit var binding: NewsHeadlinesFragmentBinding

    private lateinit var adapter: NewsHeadlinesAdapter
    private val vModel: NewsHeadlinesViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NewsHeadlinesFragmentBinding.inflate(inflater)
        return binding.root
    }

    private fun snack(msg: String) {
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT).show()
    }
    //TODO move these
    private fun errAlert(msg: String) {
        AlertDialog.Builder(requireContext()).setTitle("Error").setMessage(msg).show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vModel.internetReconnected.observe(viewLifecycleOwner, Observer {
            Snackbar.make(binding.root, "Reconnected to internet", Snackbar.LENGTH_LONG)
                .setAction("Refresh") {
                    vModel.refreshList()
                }.show()
        })

        binding.swipeRefresh.setOnRefreshListener {
            vModel.refreshList()
        }

        vModel.refreshStatusLive.observe(viewLifecycleOwner, Observer {
            when (it) {
                LoadStatus.COMPLETED -> snack("Data reloaded")
                LoadStatus.FAILED -> errAlert("Unable to load data, check your internet connection")
            }

            binding.swipeRefresh.isRefreshing = it == LoadStatus.REFRESHING
        })

        adapter = NewsHeadlinesAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 1)
        binding.recyclerView.setItemSpacing()

        vModel.newsPagedLive.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

}

class NewsHeadlinesAdapter
    : PagedListAdapter<ArticleMinimal,
        NewsHeadlinesAdapter.NewsHeadlinesViewHolder>(buildDiffUttil { this.title }) {

    class NewsHeadlinesViewHolder(private val binding: NewsArticleListItemBinding):
            RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ArticleMinimal) {
            binding.data = data
            binding.executePendingBindings()
            data.imageLink?.let {
                binding.imageView.load(it) {
                    crossfade(true)
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
