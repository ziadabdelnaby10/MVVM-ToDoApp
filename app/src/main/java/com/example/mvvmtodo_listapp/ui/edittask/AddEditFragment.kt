package com.example.mvvmtodo_listapp.ui.edittask

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mvvmtodo_listapp.R
import com.example.mvvmtodo_listapp.databinding.FragmentAddEditBinding
import com.example.mvvmtodo_listapp.ui.base.BaseFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEditFragment : BaseFragment<FragmentAddEditBinding>() {
    private val viewModel by viewModels<AddEditViewModel>()

    override val layoutId: Int
        get() = R.layout.fragment_add_edit

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getViewDataBinding().apply {
            etTaskName.setText(viewModel.taskName)
            checkboxImportance.isChecked = viewModel.taskImportance
            checkboxImportance.jumpDrawablesToCurrentState()
            txtDateCreated.isVisible = viewModel.task != null
            val date = "Created: ${viewModel.task?.createdDateFormatted}"
            txtDateCreated.text = date
            checkboxImportance.jumpDrawablesToCurrentState()
            checkboxImportance.setOnCheckedChangeListener { _, isChecked ->
                viewModel.taskImportance = isChecked
            }
            etTaskName.addTextChangedListener {
                viewModel.taskName = it.toString()
            }
            fabDone.setOnClickListener {
                viewModel.onSaveClick()
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.addEditTaskEvent.collect { event ->
                when(event){
                    is AddEditViewModel.AddEditTaskEvent.ShowInvalidInputMessage -> {
                        Snackbar.make(requireView() , event.msg , Snackbar.LENGTH_LONG).show()
                    }
                    is AddEditViewModel.AddEditTaskEvent.NavigateBackWithResult -> {
                        getViewDataBinding().etTaskName.clearFocus()
                        setFragmentResult(
                            "add_edit_request",
                            bundleOf("add_edit_request" to event.result)
                        )
                        findNavController().navigateUp()
                    }
                }
            }
        }
    }
}