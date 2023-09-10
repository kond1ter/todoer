package com.example.todoer.ui.completed

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.todoer.databinding.FragmentCompletedBinding
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import java.util.Date

class CompletedFragment : Fragment() {
    private lateinit var completedRecyclerView: RecyclerView

    private lateinit var progressDir: String
    private lateinit var completedDir: String
    private lateinit var progressList: ArrayList<TodoItem>
    private lateinit var completedList: ArrayList<TodoItem>

    private var _binding: FragmentCompletedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView
        (inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        ViewModelProvider(this)[CompletedViewModel::class.java]
        _binding = FragmentCompletedBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initFragment()
        return root
    }

    private fun initFragment() {
        completedRecyclerView = binding.completedRecyclerView

        progressDir = (activity as MainActivity).getProgressDir()
        completedDir = (activity as MainActivity).getCompletedDir()
        progressList = (activity as MainActivity).getProgressList()
        completedList = (activity as MainActivity).getCompletedList()

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        completedRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        completedRecyclerView.adapter = CompleteAdapter(completedList, requireContext())
        completedRecyclerView.addItemDecoration(divider)
        itemTouchHelper.attachToRecyclerView(completedRecyclerView)
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
            val item = completedList[position]

            when (direction) {
                ItemTouchHelper.LEFT -> {
                    completedList.removeAt(position)
                    completedRecyclerView.adapter?.notifyItemRemoved(position)
                    (activity as MainActivity).saveArray(completedList, completedDir)
                }
                ItemTouchHelper.RIGHT -> {
                    completedList.removeAt(position)
                    item.date = Date()
                    progressList.add(item)
                    completedRecyclerView.adapter?.notifyItemRemoved(position)
                    (activity as MainActivity).saveArray(progressList, progressDir)
                    (activity as MainActivity).saveArray(completedList, completedDir)
                }
            }
        }

        override fun onChildDraw(
            c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
            dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean,
        ) {
            RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                .addSwipeLeftBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
                .addSwipeLeftLabel(requireContext().getString(R.string.swipe_delete_text))
                .setSwipeLeftLabelColor(ContextCompat.getColor(requireContext(), R.color.white))

                .addSwipeRightBackgroundColor(ContextCompat.getColor(requireContext(), R.color.accent))
                .addSwipeRightLabel(requireContext().getString(R.string.swipe_return_text))
                .setSwipeRightLabelColor(ContextCompat.getColor(requireContext(), R.color.white))

                .create()
                .decorate()
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity).setActionBarTitle(resources.getString(R.string.title_completed))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}