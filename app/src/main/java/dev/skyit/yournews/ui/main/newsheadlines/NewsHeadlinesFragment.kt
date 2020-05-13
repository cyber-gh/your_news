package dev.skyit.yournews.ui.main.newsheadlines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import dev.skyit.yournews.BaseFragment
import dev.skyit.yournews.databinding.NewsArticleListItemBinding
import dev.skyit.yournews.databinding.NewsHeadlinesFragmentBinding
import dev.skyit.yournews.ui.ArticleMinimal
import dev.skyit.yournews.ui.utils.RecyclerAdapter
import dev.skyit.yournews.ui.utils.buildDiffUttil
import dev.skyit.yournews.ui.utils.setItemSpacing
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                Picasso.get().load(it).into(binding.imageView)
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


class DiffArticleUtilCallBack : DiffUtil.ItemCallback<ArticleMinimal>() {
    override fun areItemsTheSame(oldItem: ArticleMinimal, newItem: ArticleMinimal): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: ArticleMinimal, newItem: ArticleMinimal): Boolean {
        return oldItem == newItem
    }

}