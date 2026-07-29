package com.example.basuapp

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class Task(var text: String, var done: Boolean)

class TaskAdapter(
    private val tasks: MutableList<Task>,
    private val onToggle: (Int) -> Unit,
    private val onDelete: (Int) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkbox: CheckBox = view.findViewById(R.id.taskCheckbox)
        val text: TextView = view.findViewById(R.id.taskText)
        val delete: TextView = view.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]

        holder.checkbox.setOnCheckedChangeListener(null)
        holder.checkbox.isChecked = task.done
        holder.text.text = task.text

        if (task.done) {
            holder.text.paintFlags = holder.text.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.text.paintFlags = holder.text.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        holder.checkbox.setOnCheckedChangeListener { _, _ ->
            onToggle(holder.adapterPosition)
        }

        holder.delete.setOnClickListener {
            onDelete(holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int = tasks.size
}
