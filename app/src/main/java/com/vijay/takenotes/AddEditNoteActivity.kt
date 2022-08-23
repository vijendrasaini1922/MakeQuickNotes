package com.vijay.takenotes

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.vijay.takenotes.Notes.Note
import com.vijay.takenotes.ViewModel.NoteViewModel
import com.vijay.takenotes.databinding.ActivityAddEditNoteBinding
import java.text.SimpleDateFormat
import java.util.*

class AddEditNoteActivity : AppCompatActivity() {
    private lateinit var binding:ActivityAddEditNoteBinding
    private lateinit var viewModel: NoteViewModel
    private lateinit var btn:Button
    private lateinit var noteTitleEdit:EditText
    private lateinit var noteDescriptionEdit:EditText
    private var noteID = -1

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_edit_note)

        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
            .getInstance(application)).get(NoteViewModel::class.java)

        btn = binding.addButton
        noteTitleEdit = binding.editNoteTitle
        noteDescriptionEdit = binding.editNoteDescription

        val noteType = intent.getStringExtra("noteType")
        if(noteType.equals("Edit")) {
            val noteTitle = intent.getStringExtra("noteTitle")
            val noteDescription = intent.getStringExtra("noteDescription")
            noteTitleEdit.setText(noteTitle)
            noteDescriptionEdit.setText(noteDescription)
            btn.setText("Update Content")
            noteID = intent.getIntExtra("noteId", -1)
        } else{
            btn.setText("Save Note")
        }
        btn.setOnClickListener {
            val noteTitle = noteTitleEdit.text.toString()
            val noteDescription = noteDescriptionEdit.text.toString()
            if(noteType.equals("Edit")){
                if(noteTitle.isNotEmpty()){
                    val currentTime = SimpleDateFormat("dd MMM,  yyyy - HH:mm").format(Date())
                    val updateNote = Note(noteTitle, noteDescription, currentTime)
                    updateNote.id = noteID
                    viewModel.updateNote(updateNote)
                    Toast.makeText(this, "Content Updated", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this, "Please Give Some Title Name.", Toast.LENGTH_SHORT).show()
                }
            }else{
                if(noteTitle.isNotEmpty()){
                    val currentTime = SimpleDateFormat("dd MMM,  yyyy - HH:mm").format(Date())
                    viewModel.addNote(Note(noteTitle, noteDescription, currentTime))
                    Toast.makeText(this, "Content Added", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this, "Please Give Some Title Name.", Toast.LENGTH_SHORT).show()
                }
            }
            startActivity(Intent(this, MainActivity::class.java))
            this.finish()
        }
    }
}