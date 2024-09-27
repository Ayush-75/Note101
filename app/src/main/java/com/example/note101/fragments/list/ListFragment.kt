package com.example.note101.fragments.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.note101.R
import com.example.note101.data.models.NotesData
import com.example.note101.data.viewmodel.NotesViewModel
import com.example.note101.data.viewmodel.SharedViewModel
import com.example.note101.databinding.FragmentListBinding
import com.example.note101.fragments.list.adapter.ListAdapter
import com.example.note101.utils.observeOnce
import com.google.android.material.snackbar.Snackbar

class ListFragment : Fragment(), SearchView.OnQueryTextListener {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val notesViewModel:NotesViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()
    private val adapter: ListAdapter by lazy { ListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding =  FragmentListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.shareViewModel = sharedViewModel

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)


        notesViewModel.getAllData.observe(viewLifecycleOwner) { data ->
            sharedViewModel.checkIfDatabaseEmpty(data)
            adapter.setData(data)
            binding.recyclerView.scheduleLayoutAnimation()
        }

        swipeToDelete(binding.recyclerView)


        setHasOptionsMenu(true)

        return binding.root
    }

    private fun swipeToDelete(recyclerView: RecyclerView){
        val swipeToDeleteCallBack = object :SwipeToDelete(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = adapter.dataList[viewHolder.adapterPosition]
                notesViewModel.delete(deletedItem)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)
                Toast.makeText(context, "Successfully Removed: ${deletedItem.title}", Toast.LENGTH_SHORT).show()
                // restore deleted item
                restoreDeletedData(viewHolder.itemView,deletedItem)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallBack)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreDeletedData(view: View, deletedItem: NotesData){
        val snackBar = Snackbar.make(view, "Deleted '${deletedItem.title}'", Snackbar.LENGTH_LONG)
        snackBar.setAction("Undo"){
            notesViewModel.insertData(deletedItem)
        }
        snackBar.show()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)
        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete_all -> confirmRemoval()
            R.id.menu_priority_high -> notesViewModel.sortByHighPriority().observe(viewLifecycleOwner) { adapter.setData(it) }
            R.id.menu_priority_low -> notesViewModel.sortByLowPriority().observe(viewLifecycleOwner) { adapter.setData(it) }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun confirmRemoval() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_,_->
            notesViewModel.deleteAll()
            Toast.makeText(context, "Successfully removed EveryThing!", Toast.LENGTH_SHORT).show()
        }
        builder.setNeutralButton("No"){_,_ ->}
        builder.setTitle("Delete All notes?")
        builder.setMessage("Are you sure you want to delete all Notes?")
        builder.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query!=null){
            searchThroughDatabase(query)
        }
        return true
    }
    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText!=null){
            searchThroughDatabase(newText)
        }
        return true    }

    private fun searchThroughDatabase(query: String) {
        val searchQuery = "%$query%"

        notesViewModel.searchDatabase(searchQuery).observeOnce(viewLifecycleOwner) { list ->
            list?.let {
                adapter.setData(it)
            }
        }
    }
}
