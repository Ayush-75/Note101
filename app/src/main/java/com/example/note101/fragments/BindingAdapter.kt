package com.example.note101.fragments

import android.view.View
import android.widget.Spinner
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.example.note101.R
import com.example.note101.data.models.NotesData
import com.example.note101.data.models.Priority
import com.example.note101.fragments.list.ListFragmentDirections
import com.google.android.material.floatingactionbutton.FloatingActionButton

@BindingAdapter("navigateToAddFragment")
fun FloatingActionButton.navigateToAddFragment(navigate: Boolean) {
    setOnClickListener {
        if (navigate) {
            it.findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }
    }
}

@BindingAdapter("emptyDataBase")
fun View.emptyDataBase(emptyDatabase: MutableLiveData<Boolean>) {
    when (emptyDatabase.value) {
        true -> this.visibility = View.VISIBLE
        else -> this.visibility = View.INVISIBLE
    }
}

@BindingAdapter("parsePriorityToInt")
fun Spinner.parsePriorityToInt(priority: Priority?) {
    when (priority) {
        Priority.HIGH -> {
            this.setSelection(0)
        }

        Priority.MEDIUM -> {
            this.setSelection(1)
        }

        Priority.LOW -> {
            this.setSelection(2)
        }

        null -> this.setSelection(0)
    }
}

@BindingAdapter("parsePriorityColor")
fun CardView.parsePriorityColor(priority: Priority) {
    when (priority) {
        Priority.HIGH -> setCardBackgroundColor(this.context.getColor(R.color.red))
        Priority.MEDIUM -> setCardBackgroundColor(this.context.getColor(R.color.yellow))
        Priority.LOW -> setCardBackgroundColor(this.context.getColor(R.color.green))
    }
}

@BindingAdapter("sendDataToUpdateFragment")
fun ConstraintLayout.sendDataToUpdateFragment(item: NotesData) {
    setOnClickListener {
        val action = ListFragmentDirections.actionListFragmentToUpdateFragment(item)
        findNavController().navigate(action)
    }
}