package dev.skyit.yournews.ui.main.sources

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dev.skyit.yournews.BaseFragment
import dev.skyit.yournews.databinding.AllNewsFragmentBinding
import dev.skyit.yournews.ui.main.allnews.ArticleListFragment
import dev.skyit.yournews.ui.main.allnews.ArticlesSource

class AllNewsFragment: BaseFragment() {

    private lateinit var binding: AllNewsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AllNewsFragmentBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = AllArticlesViewPagerAdapter(this)

        binding.viewPager2.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            tab.text = ArticlesSource.OPTIONS[position].name
            binding.viewPager2.setCurrentItem(tab.position, true)
        }.attach()

    }


    private inner class AllArticlesViewPagerAdapter(fa: Fragment) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = ArticlesSource.OPTIONS.count()

        override fun createFragment(position: Int): Fragment {
            return ArticleListFragment.newInstance(ArticlesSource.OPTIONS[position])
        }
    }

}