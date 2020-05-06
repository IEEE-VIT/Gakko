package com.benrostudios.gakko.ui.home.pinboard

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.TypedValue
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
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel
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
    private var showPinboardButtonOptions: Boolean = true

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
        if(utils.retrieveCurrentTeacher() == utils.retrieveMobile()) {
            add_work_button.visibility = View.VISIBLE
        } else {
            add_work_button.visibility = View.GONE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getMaterials(utils.retrieveCurrentClassroom()!!)
        pinboard_progress_bar.visibility = View.VISIBLE

        add_work_button.setOnClickListener {
            showPinboardButtonOptions = if (showPinboardButtonOptions) {
                revealOptions()
                false
            } else {
                revealOptions()
                true
            }
        }

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
        if(materialsList.isNullOrEmpty()) {
            default_pinboard_image_view.visibility = View.VISIBLE
            default_pinboard_text_view.visibility = View.VISIBLE
            pinboard_recycler_view.visibility = View.GONE
            pinboard_progress_bar.visibility = View.GONE
        } else {
            default_pinboard_image_view.visibility = View.GONE
            default_pinboard_text_view.visibility = View.GONE
            pinboard_recycler_view.visibility = View.VISIBLE
            val adapter = PinboardDisplayAdapter(materialsList)
            pinboard_recycler_view.adapter = adapter
            pinboard_recycler_view.addItemDecoration(DividerItemDecoration(requireActivity(), LinearLayout.VERTICAL))
            pinboard_recycler_view.layoutManager = LinearLayoutManager(requireContext())
            pinboard_progress_bar.visibility = View.GONE
        }
    }

    private fun revealOptions(){
        val fourteenDp = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 14f,
            context?.resources?.displayMetrics
        )
        val animationPos: Float
        if (showPinboardButtonOptions) {
            animationPos = (material_type_assignment_button.top.toFloat() - add_work_button.bottom.toFloat())
            ObjectAnimator.ofFloat(add_work_button, "translationY", animationPos).apply {
                duration = 100
                start()
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        add_work_button.shapeAppearanceModel = ShapeAppearanceModel().toBuilder()
                            .setTopLeftCorner(CornerFamily.ROUNDED, fourteenDp)
                            .setTopRightCorner(CornerFamily.ROUNDED, fourteenDp)
                            .setBottomLeftCorner(CornerFamily.CUT, 0f)
                            .setBottomRightCorner(CornerFamily.CUT, 0f)
                            .build()
                    }
                })
            }
            material_type_assignment_button.visibility = View.VISIBLE
            material_type_material_button.visibility = View.VISIBLE
            material_type_question_button.visibility = View.VISIBLE
        } else {
            animationPos =
                (add_work_button.bottom.toFloat() - material_type_material_button.bottom.toFloat())
            ObjectAnimator.ofFloat(add_work_button, "translationY", animationPos).apply {
                duration = 100
                start()
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        add_work_button.shapeAppearanceModel = ShapeAppearanceModel().toBuilder()
                            .setTopLeftCorner(CornerFamily.ROUNDED, fourteenDp)
                            .setTopRightCorner(CornerFamily.ROUNDED, fourteenDp)
                            .setBottomLeftCorner(CornerFamily.ROUNDED, fourteenDp)
                            .setBottomRightCorner(CornerFamily.ROUNDED, fourteenDp)
                            .build()
                    }
                })
            }
            material_type_material_button.visibility = View.INVISIBLE
            material_type_assignment_button.visibility = View.GONE
            material_type_question_button.visibility = View.INVISIBLE
        }
    }
}
