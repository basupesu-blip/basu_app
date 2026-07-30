package com.example.basuapp

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: NoteAdapter
    private val notes = mutableListOf<Note>()
    private val prefsName = "basu_notes_prefs"
    private val notesKey = "notes_json"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadNotes()

        val recyclerView = findViewById<RecyclerView>(R.id.notesRecyclerView)
        val addNoteButton = findViewById<Button>(R.id.addNoteButton)

        adapter = NoteAdapter(notes) { saveNotes() }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        addNoteButton.setOnClickListener {
            notes.add(0, Note(""))
            adapter.notifyItemInserted(0)
            saveNotes()
        }
    }

    private fun saveNotes() {
        val jsonArray = JSONArray()
        for (note in notes) {
            val obj = JSONObject()
            obj.put("text", note.text)
            jsonArray.put(obj)
        }
        getSharedPreferences(prefsName, MODE_PRIVATE)
            .edit()
            .putString(notesKey, jsonArray.toString())
            .apply()
    }

    private fun loadNotes() {
        val json = getSharedPreferences(prefsName, MODE_PRIVATE)
            .getString(notesKey, null) ?: return
        val jsonArray = JSONArray(json)
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            notes.add(Note(obj.getString("text")))
        }
    }
}
