package com.example.basuapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: TaskAdapter
    private val tasks = mutableListOf<Task>()
    private val prefsName = "basu_todo_prefs"
    private val tasksKey = "tasks_json"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadTasks()

        val recyclerView = findViewById<RecyclerView>(R.id.taskRecyclerView)
        val inputTask = findViewById<EditText>(R.id.inputTask)
        val addButton = findViewById<Button>(R.id.addButton)

        adapter = TaskAdapter(
            tasks,
            onToggle = { position ->
                tasks[position].done = !tasks[position].done
                adapter.notifyItemChanged(position)
                saveTasks()
            },
            onDelete = { position ->
                tasks.removeAt(position)
                adapter.notifyItemRemoved(position)
                saveTasks()
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        addButton.setOnClickListener {
            val text = inputTask.text.toString().trim()
            if (text.isNotEmpty()) {
                tasks.add(0, Task(text, false))
                adapter.notifyItemInserted(0)
                inputTask.text.clear()
                saveTasks()
            }
        }
    }

    private fun saveTasks() {
        val jsonArray = JSONArray()
        for (task in tasks) {
            val obj = JSONObject()
            obj.put("text", task.text)
            obj.put("done", task.done)
            jsonArray.put(obj)
        }
        getSharedPreferences(prefsName, MODE_PRIVATE)
            .edit()
            .putString(tasksKey, jsonArray.toString())
            .apply()
    }

    private fun loadTasks() {
        val json = getSharedPreferences(prefsName, MODE_PRIVATE)
            .getString(tasksKey, null) ?: return
        val jsonArray = JSONArray(json)
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            tasks.add(Task(obj.getString("text"), obj.getBoolean("done")))
        }
    }
}
