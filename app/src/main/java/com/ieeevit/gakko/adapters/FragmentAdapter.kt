package com.ieeevit.gakko.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ieeevit.gakko.ui.home.members.MembersFragment
import com.ieeevit.gakko.ui.home.pinboard.PinboardFragment
import com.ieeevit.gakko.ui.home.threads.ThreadsFragment

class FragmentAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3
    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> ThreadsFragment()
            1 -> PinboardFragment()
            2 -> MembersFragment()
            else -> ThreadsFragment()
        }
    }
}