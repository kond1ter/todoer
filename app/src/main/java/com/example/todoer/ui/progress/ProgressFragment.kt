package com.example.todoer.ui.progress

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoer.FragmentWithSaveListFun
import com.example.todoer.MainActivity
import com.example.todoer.R
import com.example.todoer.TodoItem
import com.example.todoer.databinding.FragmentProgressBinding
import java.util.Date


class ProgressFragment : FragmentWithSaveListFun() {
    private lateinit var progressRecyclerView: RecyclerView
    private lateinit var progressList: MutableList<TodoItem>
    private lateinit var progressDir: String
    private lateinit var addTodoBtn: Button
    private lateinit var dialog: Dialog
    private lateinit var dialogSubmitBtn: Button
    private lateinit var dialogEditText: EditText
    private lateinit var dialogTitle: TextView

    private var _binding: FragmentProgressBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView
        (inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val progressViewModel = ViewModelProvider(this).get(ProgressViewModel::class.java)
        _binding = FragmentProgressBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initFragment(); setInitial()
        return root
    }

    private fun initFragment() {
        progressRecyclerView = binding.progressRecyclerView
        progressList = mutableListOf()
        progressDir = "${context?.filesDir?.path}/progress"
        addTodoBtn = binding.addTodoBtn
        addTodoBtn.setOnClickListener { addTodo() }

        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_add_todo)
        dialogSubmitBtn = dialog.findViewById(R.id.submitBtn)
        dialogEditText = dialog.findViewById(R.id.todoEditText)
        dialogTitle = dialog.findViewById(R.id.addTodoTitle)

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        progressRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        progressRecyclerView.adapter = ProgressAdapter(progressList)
        progressRecyclerView.addItemDecoration(divider)
        itemTouchHelper.attachToRecyclerView(progressRecyclerView)
    }

    var simpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder,
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            val item = progressList[position]

            when (direction) {
                ItemTouchHelper.LEFT -> {
                    progressList.removeAt(position)
                    progressRecyclerView.adapter?.notifyItemRemoved(position)
                }
                ItemTouchHelper.RIGHT -> {

                }
            }
        }
    }

    private fun addTodo() {
        dialog.show()
        dialogEditText.setText("")
        dialogTitle.setText(R.string.add_todo_title)

        dialogSubmitBtn.setOnClickListener {
            val text: String = dialogEditText.text.toString()
            progressList.add(TodoItem(text, Date()))
            saveArray(progressList, progressDir)
            dialog.cancel()
        }
    }

    private fun setInitial() {

    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity).setActionBarTitle(resources.getString(R.string.title_progress))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}