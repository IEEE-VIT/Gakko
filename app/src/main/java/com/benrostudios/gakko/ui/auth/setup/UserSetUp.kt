package com.benrostudios.gakko.ui.auth.setup

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.benrostudios.gakko.R
import com.benrostudios.gakko.ui.base.ScopedFragment
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein

class UserSetUp : ScopedFragment(),KodeinAware {
    override val kodein: Kodein by closestKodein()

    companion object {
        fun newInstance() = UserSetUp()
    }

    private lateinit var viewModel: UserSetUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.user_set_up_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(UserSetUpViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
