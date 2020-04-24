package com.benrostudios.gakko.ui.classroom.classroomdisplay

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.benrostudios.gakko.R
import com.benrostudios.gakko.adapters.ClassroomDisplayAdapter
import com.benrostudios.gakko.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.classroom_display_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class ClassroomDisplay : ScopedFragment(), KodeinAware {
    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: ClassroomDisplayViewModelFactory by instance()
    private lateinit var adapter: ClassroomDisplayAdapter

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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchClassrooms()
    }

    private fun fetchClassrooms() = launch {
        viewModel.test()
        viewModel.classroom.observe(viewLifecycleOwner, Observer {
            if(it.isNotEmpty()) {
                adapter = ClassroomDisplayAdapter(it)
                populateUI(adapter)
            }else{
                Toast.makeText(context,"No Class Found",Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun populateUI(adapter: ClassroomDisplayAdapter) {
        classroom_display_recycler.layoutManager = LinearLayoutManager(context)
        classroom_display_recycler.adapter = adapter
    }
}
