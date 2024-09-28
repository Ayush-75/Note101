package com.example.note101.fragments.add

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.note101.R
import com.example.note101.data.models.NotesData
import com.example.note101.data.models.Priority
import com.example.note101.data.viewmodel.NotesViewModel
import com.example.note101.data.viewmodel.SharedViewModel
import com.example.note101.databinding.FragmentAddBinding


class AddFragment : Fragment() {
    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private val notesViewModel: NotesViewModel by viewModels()
    private val sharedViewModel:SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding =  FragmentAddBinding.inflate(layoutInflater, container, false)
        setHasOptionsMenu(true)

        binding.prioritiesSpinner.onItemSelectedListener = sharedViewModel.listener

        return binding.root
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_add){
            insertDataToDb()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertDataToDb() {
        with(binding){
        val title = titleEt.text.toString()
        val priority = prioritiesSpinner.selectedItem.toString()
        val description = descriptionEt.text.toString()
        val validation = sharedViewModel.verifyDataFromUser(title, description)

            if (validation){
                val newData = NotesData(
                    0,
                    title,
                    sharedViewModel.parsePriority(priority),
                    description

                )
                notesViewModel.insertData(newData)
                Toast.makeText(context, "Successfully Added", Toast.LENGTH_SHORT).show()
                // navigate back
                findNavController().navigate(R.id.action_addFragment_to_listFragment)
            }
            else
                Toast.makeText(context, "Please fill out all fields", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
