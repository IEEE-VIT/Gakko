package com.benrostudios.gakko.ui.home.pinboard

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.benrostudios.gakko.R
import com.benrostudios.gakko.internal.Utils
import com.benrostudios.gakko.ui.base.ScopedFragment
import com.benrostudios.gakko.ui.home.material.MaterialFragment
import kotlinx.android.synthetic.main.pinboard_fragment.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class PinboardFragment : ScopedFragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()
    private val utils: Utils by instance()

    companion object {
        fun newInstance() = PinboardFragment()
    }

    private val bottomSheetFragment = MaterialFragment()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        material_type_assignment_button.setOnClickListener {
            utils.saveMaterialType("Assignment")
            bottomSheetFragment.show(activity?.supportFragmentManager!!, bottomSheetFragment.tag)
        }

        material_type_material_button.setOnClickListener {
            utils.saveMaterialType("Material")
            bottomSheetFragment.show(activity?.supportFragmentManager!!, bottomSheetFragment.tag)
        }

        material_type_question_button.setOnClickListener {
            utils.saveMaterialType("Questions")
            bottomSheetFragment.show(activity?.supportFragmentManager!!, bottomSheetFragment.tag)
        }

    }
}
