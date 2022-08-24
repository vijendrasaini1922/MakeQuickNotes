package com.vijay.takenotes

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        // Binding elements
        recyclerView = binding.recyclerView
        floatingButton = binding.floatingButton

        // View Model Creating function
        createViewModel()

        // Model Observer
        viewModelObserver()

        // Floating button action on click
        onClickFloatingButton()
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
        this.finish()
    }

    private fun createViewModel(){
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
            .getInstance(application)).get(NoteViewModel::class.java)
    }

    private fun viewModelObserver(){
        recyclerView.layoutManager = LinearLayoutManager(this)
        val recyclerViewAdapter = NoteRecyclerViewAdapter(this, this, this)
        recyclerView.adapter = recyclerViewAdapter
        viewModel.allNote.observe(this, Observer { list->
            list?.let {
                recyclerViewAdapter.updateList(it)
            }
        })
    }

    private fun onClickFloatingButton(){
        floatingButton.setOnClickListener{
            startActivity(Intent(this, AddEditNoteActivity::class.java ))
            this.finish()
        }
    }
}