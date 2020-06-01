package dev.skyit.yournews.ui.utils

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import dev.skyit.yournews.BaseFragment
import dev.skyit.yournews.R

val BaseFragment.mainNavController: NavController
    get() = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)