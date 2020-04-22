package com.benrostudios.gakko.ui.home.pinboard

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.benrostudios.gakko.R

class PinboardFragment : Fragment() {

    companion object {
        fun newInstance() = PinboardFragment()
    }

    private lateinit var viewModel: PinboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.pinboard_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PinboardViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
