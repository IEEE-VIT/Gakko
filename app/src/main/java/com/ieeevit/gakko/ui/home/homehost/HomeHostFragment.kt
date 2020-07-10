package com.ieeevit.gakko.ui.home.homehost

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.viewpager2.widget.ViewPager2

import com.ieeevit.gakko.R
import com.ieeevit.gakko.adapters.FragmentAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.ieeevit.gakko.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.trial_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

    class HomeHostFragment : ScopedFragment(), KodeinAware {
    private lateinit var viewPager2: ViewPager2
    private lateinit var navController: NavController
    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: HomeHostViewModelFactory by instance()

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
        navController = Navigation.findNavController(view)
        viewPager2 = pager
        val titles = arrayOf("Threads","Pinboard","Members")
        viewPager2.adapter = FragmentAdapter(this)
        TabLayoutMediator(tab_layout, viewPager2) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this,viewModelFactory).get(HomeHostViewModel::class.java)
        viewModel.setClassroomSwitch(false)
        goBackToClassroomListener()
    }

    private fun goBackToClassroomListener() = launch{
        viewModel.goBackToClassroomDisplay.observe(viewLifecycleOwner, Observer {
            if(it){
                navController.navigate(R.id.action_homeHostFragment_to_classroomDisplay)
            }
        })
    }

}
