package dev.skyit.yournews.ui.main.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.transition.TransitionInflater
import coil.api.load
import coil.transform.CircleCropTransformation
import dagger.hilt.android.AndroidEntryPoint
import dev.skyit.yournews.BaseFragment
import dev.skyit.yournews.api.models.headlines.ArticleDTO
import dev.skyit.yournews.databinding.SearchItemListViewBinding
import dev.skyit.yournews.databinding.SearchNewsFragmentBinding
import dev.skyit.yournews.repository.converters.toEntity
import dev.skyit.yournews.ui.utils.SimpleRecyclerAdapter
import dev.skyit.yournews.ui.utils.hideKeyboard
import dev.skyit.yournews.ui.utils.mainNavController
import dev.skyit.yournews.utils.toArrayList



@AndroidEntryPoint
class SearchNewsFragment : BaseFragment() {

    private lateinit var binding: SearchNewsFragmentBinding

    private val vModel: SearchNewsViewModel by viewModels()
    private lateinit var adapter: SimpleRecyclerAdapter<ArticleDTO, SearchItemListViewBinding>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.slide_bottom)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SearchNewsFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
            activity?.hideKeyboard()
        }

        binding.searchEditText.requestFocus()

        val imm: InputMethodManager? = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.showSoftInput(binding.searchEditText, InputMethodManager.SHOW_IMPLICIT)

        binding.searchEditText.addTextChangedListener {
            val str = it?.toString() ?: return@addTextChangedListener
            vModel.newSearch(str)

        }

        adapter = SimpleRecyclerAdapter(binderCreator =  { inflater ->
            SearchItemListViewBinding.inflate(inflater)
        },injectData =  { data ->
            val img = data.urlToImage
            if (img != null) {
                itemImg.load(img) {
                    crossfade(true)

                    transformations(CircleCropTransformation())
                }
            }
            itemTitle.text = data.title
        }, idKey = {
            url
        }, onItemClick = {
            mainNavController.navigate(SearchNewsFragmentDirections.actionSearchNewsFragmentToArticleDetailsFragment(it.toEntity()))
        })

        binding.recyclerView2.adapter = adapter
        binding.recyclerView2.layoutManager = GridLayoutManager(requireContext(), 1)

        vModel.searchResults.observe(viewLifecycleOwner, Observer {
            adapter.updateData(it.toArrayList())
        })
    }


}