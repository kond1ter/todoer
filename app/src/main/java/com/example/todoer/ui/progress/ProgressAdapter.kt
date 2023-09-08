package com.example.todoer.ui.progress

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todoer.R
import com.example.todoer.TodoItem

class ProgressAdapter(private val progressList: MutableList<TodoItem>) :
    RecyclerView.Adapter<ProgressAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val progressText: TextView = itemView.findViewById(R.id.todoText)
        val progressTime: TextView = itemView.findViewById(R.id.todoTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.progressText.text = progressList[position].text
        holder.progressTime.text = "Time here soon"
    }

    override fun getItemCount(): Int {
        return progressList.size
    }
}