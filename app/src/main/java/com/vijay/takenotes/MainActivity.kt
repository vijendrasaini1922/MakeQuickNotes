package com.vijay.takenotes

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.vijay.takenotes.Notes.Note
import com.vijay.takenotes.ViewModel.NoteViewModel
import com.vijay.takenotes.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), NoteClickInterface, NoteClickDeleteInterface {
    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerView:RecyclerView
    private lateinit var floatingButton: FloatingActionButton
    private lateinit var viewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        recyclerView = binding.recyclerView
        floatingButton = binding.floatingButton
        recyclerView.layoutManager = LinearLayoutManager(this)
        val recyclerViewAdapter = NoteRecyclerViewAdapter(this, this, this)
        recyclerView.adapter = recyclerViewAdapter

        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
            .getInstance(application)).get(NoteViewModel::class.java)

        viewModel.allNote.observe(this, Observer { list->
            list?.let {
                recyclerViewAdapter.updateList(it)
            }
        })
        floatingButton.setOnClickListener{
            startActivity(Intent(this, AddEditNoteActivity::class.java ))
            this.finish()
        }
    }

    override fun onDeleteIconClick(note: Note) {
        viewModel.deleteNote(note)
    }

    override fun onNoteClick(note: Note) {
        val intent = Intent(this, AddEditNoteActivity::class.java )
        intent.putExtra("noteType", "Edit")
        intent.putExtra("noteTitle", note.noteTitle)
        intent.putExtra("noteDescription", note.noteDescription)
        intent.putExtra("noteID", note.id)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.aboutPage -> {
                Toast.makeText(applicationContext, "About Page", Toast.LENGTH_LONG).show()
                true
            }
            R.id.shareButton ->{
                Toast.makeText(applicationContext, "Share the note", Toast.LENGTH_LONG).show()
                return true
            }
            R.id.whatsNew ->{
                Toast.makeText(applicationContext, "What's New", Toast.LENGTH_LONG).show()
                return true
            }
            R.id.nextVersion ->{
                Toast.makeText(applicationContext, "Upcoming Version", Toast.LENGTH_LONG).show()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}