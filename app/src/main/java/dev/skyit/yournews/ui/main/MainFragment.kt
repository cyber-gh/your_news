package dev.skyit.yournews.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import dev.skyit.yournews.BaseFragment
import dev.skyit.yournews.R
import dev.skyit.yournews.databinding.MainFragmentBinding
import kotlinx.android.synthetic.main.main_fragment.view.*

class MainFragment : BaseFragment() {
    private lateinit var binding: MainFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MainFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navView = binding.bottomNavigationView
        val controller = Navigation.findNavController(requireActivity(), R.id.main_tabs_fragment_host)

        navView.setupWithNavController(controller)


        binding.appToolbar.onSearch {
            findNavController().navigate(R.id.action_mainFragment_to_searchNewsFragment)
        }

        binding.appToolbar.onOpenSettings {
            findNavController().navigate(R.id.action_mainFragment_to_settingsFragment)
        }

        controller.addOnDestinationChangedListener { controller, destination, arguments ->
            if (destination.id == R.id.navigation_all_news) {
                binding.appToolbar.elevation = 0f
            } else {
                binding.appToolbar.elevation = 4f
            }
        }
    }


}
