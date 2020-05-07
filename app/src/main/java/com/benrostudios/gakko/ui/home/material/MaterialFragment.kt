package com.benrostudios.gakko.ui.home.material

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.benrostudios.gakko.R
import com.benrostudios.gakko.data.models.Material
import com.benrostudios.gakko.internal.Utils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.material_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.io.File
import java.sql.Timestamp
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class MaterialFragment : BottomSheetDialogFragment(), KodeinAware {

    companion object {
        fun newInstance() = MaterialFragment()
        const val PICK_PDF_CODE = 1
    }

    private lateinit var viewModel: MaterialViewModel
    private lateinit var uri: Uri
    private var displayName: String? = null
    private var stringUri: String? = null
    override val kodein: Kodein by closestKodein()
    private val utils: Utils by instance()
    private val viewModelFactory: MaterialViewModelFactory by instance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.material_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MaterialViewModel::class.java)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        choose_material_image_view.setOnClickListener {
            getPDF()
        }

        upload_material_image_view.setOnClickListener{
            if(material_topic_edit_text.text.isNullOrEmpty() || material_due_date_edit_text.text.isNullOrEmpty()
                || material_title_edit_text.text.isNullOrEmpty() || stringUri.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Enter all fields and choose a material pdf first", Toast.LENGTH_SHORT).show()
            }
            else if (!dateValidation(material_due_date_edit_text.text.toString())) {
                Toast.makeText(requireContext(), "Please enter valid date in the required format (dd/mm/yyyy)", Toast.LENGTH_SHORT).show()
            }
            else {
                uploadMaterial()
            }
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun getPDF() {
        val intent = Intent()
        intent.type = "application/pdf"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), Companion.PICK_PDF_CODE)
    }

    private fun uploadMaterial() = viewLifecycleOwner.lifecycleScope.launch {
        material_uploading_progress_bar.visibility = View.VISIBLE
        material_due_date_edit_text.isFocusable = false
        material_title_edit_text.isFocusable = false
        material_topic_edit_text.isFocusable = false

        viewModel.uploadMaterial(uri, utils.retrieveMobile()!!, displayName!!)
        viewModel.downloadURL.observe(viewLifecycleOwner, Observer {
            if(!it.isNullOrEmpty()) {
                stringUri = it
                uploadMaterialInformation()
            }
            else {
                Toast.makeText(requireContext(), "Material Not Submitted, Try Again.", Toast.LENGTH_SHORT).show()
                material_uploading_progress_bar.visibility = View.GONE
                material_due_date_edit_text.isFocusable = true
                material_title_edit_text.isFocusable = true
                material_topic_edit_text.isFocusable = true
                dismissFunction()
            }
        })
    }

    @SuppressLint("SimpleDateFormat")
    private fun uploadMaterialInformation() = viewLifecycleOwner.lifecycleScope.launch {
        val strDate: String = material_due_date_edit_text.text.toString()
        val formatter: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        val date = formatter.parse(strDate)
        val material: Material = Material("pdf",date!!.time / 1000, material_title_edit_text.text.toString(),
            utils.retrieveMaterialType()!!, utils.retrieveMobile()!!, System.currentTimeMillis(), stringUri!!, utils.retrieveCurrentSubject()!!)

        viewModel.uploadMaterialInformation(material, utils.retrieveCurrentClassroom()!!)
        Toast.makeText(requireContext(), "Material Submitted", Toast.LENGTH_SHORT).show()
        material_uploading_progress_bar.visibility = View.GONE
        material_due_date_edit_text.isFocusable = true
        material_title_edit_text.isFocusable = true
        material_topic_edit_text.isFocusable = true
        dismissFunction()
    }

    @SuppressLint("SimpleDateFormat")
    private fun dateValidation(dueDateString: String): Boolean {
        val answer: Boolean
        if(dueDateString.matches("(0?[1-9]|[12][0-9]|3[01])[\\/](0?[1-9]|1[012])[\\/]\\d{4}".toRegex())) {
            val sdf: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
            val currentDateTime: Date = Calendar.getInstance().time
            val currentDateString: String = sdf.format(currentDateTime)
            val currentDate: Date = sdf.parse(currentDateString)!!
            val dueDate: Date = sdf.parse(dueDateString)!!
            answer = dueDate.after(currentDate)
        }
        else {
            answer = false
        }
        return answer
    }

    private fun dismissFunction() {
        stringUri = null
        dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissFunction()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data != null && data.data != null) { //if a file is selected
            if (data.data != null) {
                uri = data.data!!
                stringUri = uri.toString()
                val myFile = File(stringUri!!)
                displayName = null

                if (stringUri!!.startsWith("content://")) {
                    var cursor: Cursor? = null
                    try {
                        cursor = activity?.contentResolver?.query(uri, null, null, null, null)
                        if (cursor != null && cursor.moveToFirst()) {
                            displayName =
                                cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                        }
                    } finally {
                        cursor?.close();
                    }
                } else if (stringUri!!.startsWith("file://")) {
                    displayName = myFile.name;
                }
                Toast.makeText(requireContext(), "$displayName file chosen", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            Toast.makeText(requireContext(), "No file chosen", Toast.LENGTH_LONG).show()
        }
    }
}