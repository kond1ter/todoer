package com.example.todoer
import java.io.Serializable
import java.util.Date

class TodoItem(var text: String = "", var date: Date = Date()) : Serializable
