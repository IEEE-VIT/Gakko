package com.benrostudios.gakko.ui.home.pinboard

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

import com.benrostudios.gakko.R
import com.benrostudios.gakko.adapters.PinboardDisplayAdapter
import com.benrostudios.gakko.data.models.Material
import com.benrostudios.gakko.internal.Utils
import com.benrostudios.gakko.ui.base.ScopedFragment
import com.benrostudios.gakko.ui.home.material.MaterialFragment
import kotlinx.android.synthetic.main.pinboard_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class PinboardFragment : ScopedFragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()
    private val utils: Utils by instance()
    private val viewModelFactory: PinboardViewModelFactory by instance()
    private lateinit var viewModel: PinboardViewModel
    private val bottomSheetFragment = MaterialFragment()

    companion object {
        fun newInstance() = PinboardFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.pinboard_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PinboardViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getMaterials(utils.retrieveCurrentClassroom()!!)

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

    private fun getMaterials(classId: String) = launch {
        viewModel.getMaterialsList(classId)
        viewModel.materialsList.observe(viewLifecycleOwner, Observer {
            updateUI(it)
        })
    }

    private fun updateUI(materialsList: List<Material>) {
        val adapter: PinboardDisplayAdapter = PinboardDisplayAdapter(materialsList)
        pinboard_recycler_view.adapter = adapter
        pinboard_recycler_view.addItemDecoration(DividerItemDecoration(requireActivity(), LinearLayout.VERTICAL))
        pinboard_recycler_view.layoutManager = LinearLayoutManager(requireContext())
    }
}
