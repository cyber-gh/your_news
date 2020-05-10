package dev.skyit.yournews.ui.main.newsheadlines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import dev.skyit.yournews.BaseFragment
import dev.skyit.yournews.databinding.NewsArticleListItemBinding
import dev.skyit.yournews.databinding.NewsHeadlinesFragmentBinding
import dev.skyit.yournews.ui.ArticleMinimal
import dev.skyit.yournews.ui.utils.RecyclerAdapter
import dev.skyit.yournews.ui.utils.setItemSpacing
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewsHeadlinesFragment : BaseFragment() {
    private lateinit var binding: NewsHeadlinesFragmentBinding

    private lateinit var adapter: RecyclerAdapter<ArticleMinimal, NewsArticleListItemBinding>

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

        adapter = RecyclerAdapter(
            {
                NewsArticleListItemBinding.inflate(it)
            }, { data ->
                this.data = data
                data.imageLink?.let {
                    Picasso.get().load(it).into(this.imageView)
                }
            }
        )
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 1)
        binding.recyclerView.setItemSpacing()

        vModel.newsLive.observe(viewLifecycleOwner, Observer {
            adapter.updateData(ArrayList(it))
        })

        vModel.loadData()



    }

}