package dev.skyit.yournews.ui.main.search

import android.R
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.GridLayout
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import coil.api.load
import coil.transform.CircleCropTransformation
import coil.transition.CrossfadeTransition
import dev.skyit.yournews.BaseFragment
import dev.skyit.yournews.api.models.headlines.ArticleDTO
import dev.skyit.yournews.databinding.SearchItemListViewBinding
import dev.skyit.yournews.databinding.SearchNewsFragmentBinding
import dev.skyit.yournews.ui.utils.RecyclerAdapter
import dev.skyit.yournews.utils.toArrayList
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchNewsFragment : BaseFragment() {

    private lateinit var binding: SearchNewsFragmentBinding

    private val vModel: SearchNewsViewModel by viewModel()
    private lateinit var adapter: RecyclerAdapter<ArticleDTO, SearchItemListViewBinding>

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
        }

        binding.searchEditText.requestFocus()
        val imm: InputMethodManager? = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.showSoftInput(binding.searchEditText, InputMethodManager.SHOW_IMPLICIT)

        binding.searchEditText.addTextChangedListener {
            val str = it?.toString() ?: return@addTextChangedListener
            vModel.newSearch(str)

        }

        adapter = RecyclerAdapter({inflater ->
            SearchItemListViewBinding.inflate(inflater)
        }, { data ->
            val img = data.urlToImage
            if (img != null) {
                itemImg.load(img) {
                    crossfade(true)

                    transformations(CircleCropTransformation())
                }
            }
            itemTitle.text = data.title
        })

        binding.recyclerView2.adapter = adapter
        binding.recyclerView2.layoutManager = GridLayoutManager(requireContext(), 1)

        vModel.searchResults.observe(viewLifecycleOwner, Observer {
            adapter.updateData(it.toArrayList())
        })
    }


}