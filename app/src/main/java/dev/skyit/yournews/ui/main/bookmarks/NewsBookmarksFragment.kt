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
import dev.skyit.yournews.ui.utils.SimpleRecyclerAdapter
import dev.skyit.yournews.ui.utils.setItemSpacing
import dev.skyit.yournews.utils.toArrayList
import javax.inject.Inject


@AndroidEntryPoint
class NewsBookmarksFragment: BaseFragment() {

    private lateinit var binding: NewsBookmarksFragmentBinding

    private val vModel: NewsBookmarksViewModel by viewModels()

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

        val adapter: SimpleRecyclerAdapter<ArticleMinimal, *> = if (!userPreferences.useMiniCards) {

            SimpleRecyclerAdapter(
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
        } else {
            SimpleRecyclerAdapter(
                binderCreator = {
                    NewsArticleListItemSmallBinding.inflate(it)
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
        }


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



