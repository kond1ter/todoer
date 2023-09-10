package com.example.todoer.ui.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.todoer.MainActivity
import com.example.todoer.TodoItem
import com.example.todoer.databinding.FragmentStatisticsBinding
import java.util.Date
import java.util.stream.Collectors

class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView
        (inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        ViewModelProvider(this)[StatisticsViewModel::class.java]
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initFragment()
        return root
    }

    @Suppress("DEPRECATION")
    private fun initFragment() {
        val timestamp = Date().date
        val progressList: ArrayList<TodoItem> = (activity as MainActivity).getProgressList()
        val completedList: ArrayList<TodoItem> = (activity as MainActivity).getCompletedList()

        val progressNow: TextView = binding.inProgressCount
        val completedAllTime: TextView = binding.completedCount
        val completedDay: TextView = binding.completedCountDay
        val completedWeek: TextView = binding.completedCountWeek
        val completedMonth: TextView = binding.completedCountMonth

        progressNow.text = progressList.size.toString()
        completedAllTime.text = completedList.size.toString()

        completedDay.text = "${completedList.stream()
            .filter { i -> timestamp - i.date.time < 86400000 }.collect(Collectors.toList()).size}"
        completedWeek.text = "${completedList.stream()
            .filter { i -> timestamp - i.date.time < 604800000 }.collect(Collectors.toList()).size}"
        completedMonth.text = "${completedList.stream()
            .filter { i -> timestamp - i.date.time < 2592000000 }.collect(Collectors.toList()).size}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}