package com.example.note101.fragments.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.note101.data.models.NotesData
import com.example.note101.databinding.RowLayoutBinding

class ListAdapter : RecyclerView.Adapter<ListAdapter.ViewHolder>() {
//    var dataList = emptyList<NotesData>()

     val differCallBack = object : DiffUtil.ItemCallback<NotesData>() {
        override fun areItemsTheSame(oldItem: NotesData, newItem: NotesData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NotesData, newItem: NotesData): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<NotesData>) {
        differ.submitList(list)
    }

    class ViewHolder(var binding: RowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: NotesData) {
            binding.notesData = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val binding =
                    RowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ViewHolder(binding)
            }
        }
    }

//    fun setData(notesData: List<NotesData>){
//        val noteDiffUtil = NotesDiffUtil(dataList,notesData)
//        val noteDiffResult = DiffUtil.calculateDiff(noteDiffUtil)
//        this.dataList = notesData
//        noteDiffResult.dispatchUpdatesTo(this)
//
//    }

}