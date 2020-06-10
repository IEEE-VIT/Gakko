package com.ieeevit.gakko.ui.home.homehost

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2

import com.ieeevit.gakko.R
import com.ieeevit.gakko.adapters.FragmentAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.trial_fragment.*

class HomeHostFragment : Fragment() {
    private lateinit var viewPager2: ViewPager2

    companion object {
        fun newInstance() = HomeHostFragment()
    }

    private lateinit var viewModel: HomeHostViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.trial_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager2 = pager
        val titles = arrayOf("Threads","Pinboard","Members")
        viewPager2.adapter = FragmentAdapter(this)
        TabLayoutMediator(tab_layout, viewPager2) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeHostViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
