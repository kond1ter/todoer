package com.example.todoer

import android.os.Bundle
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.todoer.databinding.ActivityMainBinding
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainTitle: TextView
    private lateinit var progressDir: String
    private lateinit var completedDir: String
    private var progressList = ArrayList<TodoItem>()
    private var completedList = ArrayList<TodoItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        progressDir = "${filesDir?.path}/progress"
        completedDir = "${filesDir?.path}/completed"
        progressList = readArray(progressDir)
        completedList = readArray(completedDir)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)
        mainTitle = binding.mainTitle
    }

    fun saveArray(array: ArrayList<TodoItem>, path: String) {
        try {
            val fos = FileOutputStream(path)
            val oos = ObjectOutputStream(fos)
            oos.writeObject(array)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun readArray(path: String): ArrayList<TodoItem> {
        var array = ArrayList<TodoItem>()
        try {
            val fos = FileInputStream(path)
            val oos = ObjectInputStream(fos)
            array = oos.readObject() as ArrayList<TodoItem>
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return array
    }

    fun setActionBarTitle(text: String) {
        mainTitle.text = text
    }

    fun getProgressList(): ArrayList<TodoItem> {
        return progressList
    }

    fun getCompletedList(): ArrayList<TodoItem> {
        return completedList
    }

    fun getProgressDir(): String {
        return progressDir
    }

    fun getCompletedDir(): String {
        return completedDir
    }
}