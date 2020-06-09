package dev.skyit.yournews.ui.main.bookmarks

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import coil.api.load
import dev.skyit.yournews.BaseFragment
import dev.skyit.yournews.R
import dev.skyit.yournews.databinding.NewsArticleListItemBinding
import dev.skyit.yournews.databinding.NewsBookmarksFragmentBinding
import dev.skyit.yournews.ui.ArticleMinimal
import dev.skyit.yournews.ui.utils.SimpleRecyclerAdapter
import dev.skyit.yournews.ui.utils.setItemSpacing
import dev.skyit.yournews.utils.toArrayList
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewsBookmarksFragment: BaseFragment() {

    private lateinit var binding: NewsBookmarksFragmentBinding

    private val vModel: NewsBookmarksViewModel by viewModel()

    private lateinit var adapter : SimpleRecyclerAdapter<ArticleMinimal, NewsArticleListItemBinding>

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

        adapter = SimpleRecyclerAdapter(
            binderCreator = {
                NewsArticleListItemBinding.inflate(it)
            },
            injectData = { data ->
                this.data = data
                if (data.imageLink != null) {
                    this.imageView.load(data.imageLink) {
                        crossfade(true)
                        placeholder(R.drawable.news_article_placeholder)

                    }
                }
            },
            idKey = {
                url
            }
        )

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


}