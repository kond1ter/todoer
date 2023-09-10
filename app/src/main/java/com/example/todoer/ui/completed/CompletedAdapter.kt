package com.example.todoer.ui.completed

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todoer.R
import com.example.todoer.TodoItem

class CompleteAdapter(private val progressList: ArrayList<TodoItem>, private val context: Context) :
    RecyclerView.Adapter<CompleteAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val progressText: TextView = itemView.findViewById(R.id.todoText)
        val progressTime: TextView = itemView.findViewById(R.id.todoTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return ViewHolder(itemView)
    }

    @Suppress("DEPRECATION")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = progressList[position]
        holder.progressText.text = progressList[position].text
        holder.progressTime.text = context.resources.getString(
            R.string.todo_item_date, item.date.hours,
            item.date.minutes, item.date.date,
            item.date.month + 1, item.date.year + 1900
        )
    }

    override fun getItemCount(): Int {
        return progressList.size
    }
}
