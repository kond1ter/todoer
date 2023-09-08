package com.example.todoer
import java.util.Date

class TodoItem(val text: String = "", val date: Date = Date()) {
    private var _text = text
        get() { return field }
        set(value) { field = value }
    private var _date = date
        get() { return field }
        set(value) { field = value }
}
