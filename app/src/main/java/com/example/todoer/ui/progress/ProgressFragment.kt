package com.example.todoer.ui.progress

import android.app.Dialog
import android.graphics.Canvas
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoer.MainActivity
import com.example.todoer.R
import com.example.todoer.TodoItem
import com.example.todoer.databinding.FragmentProgressBinding
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import java.util.Date

class ProgressFragment : Fragment() {
    private lateinit var progressRecyclerView: RecyclerView
    private lateinit var addTodoBtn: Button
    private lateinit var dialog: Dialog
    private lateinit var dialogSubmitBtn: Button
    private lateinit var dialogEditText: EditText
    private lateinit var dialogTitle: TextView

    private lateinit var progressDir: String
    private lateinit var completedDir: String
    private lateinit var progressList: ArrayList<TodoItem>
    private lateinit var completedList: ArrayList<TodoItem>

    private var _binding: FragmentProgressBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView
        (inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        ViewModelProvider(this)[ProgressViewModel::class.java]
        _binding = FragmentProgressBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initFragment()
        return root
    }

    private fun initFragment() {
        progressRecyclerView = binding.progressRecyclerView
        addTodoBtn = binding.addTodoBtn
        addTodoBtn.setOnClickListener { addTodo() }

        progressDir = (activity as MainActivity).getProgressDir()
        completedDir = (activity as MainActivity).getCompletedDir()
        progressList = (activity as MainActivity).getProgressList()
        completedList = (activity as MainActivity).getCompletedList()

        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_add_todo)
        dialogSubmitBtn = dialog.findViewById(R.id.submitBtn)
        dialogEditText = dialog.findViewById(R.id.todoEditText)
        dialogTitle = dialog.findViewById(R.id.addTodoTitle)

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        progressRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        progressRecyclerView.adapter = ProgressAdapter(progressList, requireContext())
        progressRecyclerView.addItemDecoration(divider)
        itemTouchHelper.attachToRecyclerView(progressRecyclerView)

        dialogEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                dialogSubmitBtn.isEnabled = p0.toString().isNotBlank() }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun afterTextChanged(p0: Editable?) { }
        })
    }

    private var simpleCallback = object : ItemTouchHelper.SimpleCallback
        (0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder,
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.absoluteAdapterPosition
            val item = progressList[position]

            when (direction) {
                ItemTouchHelper.LEFT -> {
                    progressList.removeAt(position)
                    item.date = Date()
                    completedList.add(item)
                    progressRecyclerView.adapter?.notifyItemRemoved(position)
                    (activity as MainActivity).saveArray(progressList, progressDir)
                    (activity as MainActivity).saveArray(completedList, completedDir)
                }
                ItemTouchHelper.RIGHT -> {
                    dialog.show()
                    dialogEditText.setText(item.text)
                    dialogEditText.setSelection(dialogEditText.length())
                    dialogTitle.setText(R.string.edit_todo_title)

                    dialogSubmitBtn.setOnClickListener {
                        val text: String = dialogEditText.text.toString()
                        progressList[position].text = text
                        (activity as MainActivity).saveArray(progressList, progressDir)
                        progressRecyclerView.adapter?.notifyItemChanged(position)
                        progressRecyclerView.adapter = ProgressAdapter(progressList, requireContext())
                        dialog.cancel()
                    }
                }
            }
        }

        override fun onChildDraw(
            c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
            dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean,
        ) {
            RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                .addSwipeLeftBackgroundColor(ContextCompat.getColor(requireContext(), R.color.accent))
                .addSwipeLeftLabel(requireContext().getString(R.string.swipe_complete_text))
                .setSwipeLeftLabelColor(ContextCompat.getColor(requireContext(), R.color.white))

                .addSwipeRightBackgroundColor(ContextCompat.getColor(requireContext(), R.color.accent))
                .addSwipeRightLabel(requireContext().getString(R.string.swipe_edit_text))
                .setSwipeRightLabelColor(ContextCompat.getColor(requireContext(), R.color.white))

                .create()
                .decorate()
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }

    private fun addTodo() {
        dialog.show()
        dialogEditText.setText("")
        dialogTitle.setText(R.string.add_todo_title)

        dialogSubmitBtn.setOnClickListener {
            val text: String = dialogEditText.text.toString()
            progressList.add(TodoItem(text, Date()))
            (activity as MainActivity).saveArray(progressList, progressDir)
            dialog.cancel()
        }
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