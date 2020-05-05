package com.benrostudios.gakko.ui.classroom.classroomdisplay

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.benrostudios.gakko.R
import com.benrostudios.gakko.adapters.ClassroomDisplayAdapter
import com.benrostudios.gakko.internal.Utils
import com.benrostudios.gakko.ui.base.ScopedFragment
import com.benrostudios.gakko.ui.classroom.createclassroom.CreateClassroom
import com.benrostudios.gakko.ui.classroom.joinclassroom.JoinClassroom
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel
import kotlinx.android.synthetic.main.classroom_display_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class ClassroomDisplay : ScopedFragment(), KodeinAware {
    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: ClassroomDisplayViewModelFactory by instance()
    private val utils: Utils by instance()
    private lateinit var adapter: ClassroomDisplayAdapter
    private lateinit var navController: NavController
    private var showClassroomButtonOptions: Boolean = true

    companion object {
        fun newInstance() =
            ClassroomDisplay()
    }

    private lateinit var viewModel: ClassroomDisplayViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.classroom_display_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel =
            ViewModelProvider(this, viewModelFactory).get(ClassroomDisplayViewModel::class.java)

        add_classroom_btn.setOnClickListener {
            showClassroomButtonOptions = if (showClassroomButtonOptions) {
                showAddClassroomOptions()
                false
            } else {
                showAddClassroomOptions()
                true
            }
        }

        var options: RequestOptions = RequestOptions()
            .error(R.drawable.ic_defualt_profile_pic)
            .placeholder(R.drawable.ic_defualt_profile_pic)
            .circleCrop()

        Glide.with(this)
            .load(utils.retrieveProfilePic())
            .apply(options)
            .placeholder(R.drawable.ic_defualt_profile_pic)
            .into(usr_profile_pic)


        join_classroom_btn.setOnClickListener {
            val bottomSheetFragment = JoinClassroom()
            bottomSheetFragment.show(
                activity?.supportFragmentManager!!,
                bottomSheetFragment.tag
            )
        }

        create_classroom_btn.setOnClickListener {
            //navController.navigate(R.id.action_classroomDisplay_to_createClassroom)
            val bottomSheetFragment = CreateClassroom()
            bottomSheetFragment.show(
                activity?.supportFragmentManager!!,
                bottomSheetFragment.tag
            )

        }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchClassrooms()
        navController = Navigation.findNavController(view)
    }

    private fun fetchClassrooms() = launch {
        viewModel.test()
        viewModel.classroom.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                not_part_of_any_class_image.visibility = View.GONE
                not_part_of_class_title.visibility = View.GONE
                adapter = ClassroomDisplayAdapter(it)
                populateUI(adapter)
            } else {
                not_part_of_any_class_image.visibility = View.VISIBLE
                not_part_of_class_title.visibility = View.VISIBLE
            }
        })

    }

    private fun populateUI(adapter: ClassroomDisplayAdapter) {
        classroom_display_recycler.layoutManager = LinearLayoutManager(context)
        classroom_display_recycler.adapter = adapter
    }

    private fun showAddClassroomOptions() {
        val fourteenDp = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 14f,
            context?.resources?.displayMetrics
        )
        val animationPos: Float
        if (showClassroomButtonOptions) {
            animationPos = (join_classroom_btn.top.toFloat() - add_classroom_btn.bottom.toFloat())
            ObjectAnimator.ofFloat(add_classroom_btn, "translationY", animationPos).apply {
                duration = 100
                start()
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        add_classroom_btn.shapeAppearanceModel = ShapeAppearanceModel().toBuilder()
                            .setTopLeftCorner(CornerFamily.ROUNDED, fourteenDp)
                            .setTopRightCorner(CornerFamily.ROUNDED, fourteenDp)
                            .setBottomLeftCorner(CornerFamily.CUT, 0f)
                            .setBottomRightCorner(CornerFamily.CUT, 0f)
                            .build()
                    }
                })
            }
            join_classroom_btn.visibility = View.VISIBLE
            line_division.visibility = View.VISIBLE
            create_classroom_btn.visibility = View.VISIBLE
        } else {
            animationPos =
                (add_classroom_btn.bottom.toFloat() - create_classroom_btn.bottom.toFloat())
            ObjectAnimator.ofFloat(add_classroom_btn, "translationY", animationPos).apply {
                duration = 100
                start()
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        add_classroom_btn.shapeAppearanceModel = ShapeAppearanceModel().toBuilder()
                            .setTopLeftCorner(CornerFamily.ROUNDED, fourteenDp)
                            .setTopRightCorner(CornerFamily.ROUNDED, fourteenDp)
                            .setBottomLeftCorner(CornerFamily.ROUNDED, fourteenDp)
                            .setBottomRightCorner(CornerFamily.ROUNDED, fourteenDp)
                            .build()
                    }
                })
            }
            join_classroom_btn.visibility = View.INVISIBLE
            line_division.visibility = View.GONE
            create_classroom_btn.visibility = View.INVISIBLE

        }
    }

}
