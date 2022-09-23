package com.vijay.takenotes

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
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
    private lateinit var preferences:SharedPreferences
    var noteID = -1

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_edit_note)
        preferences = PreferenceManager.getDefaultSharedPreferences(this)

        // calling the action bar
        val actionBar: ActionBar? = supportActionBar
        // showing the back button in action bar
        actionBar?.setDisplayHomeAsUpEnabled(true);

        // Adding Signature
        signatureAdd()

        // Creating View Model
        createViewModel()

        // Binding buttons and TextViews
        btn = binding.addButton
        noteTitleEdit = binding.editNoteTitle
        noteDescriptionEdit = binding.editNoteDescription

        checkForNewNote()
        onClickAddButton(btn)
    }

        private fun signatureAdd(){
            val sign = preferences.getString("signature", "No Owner")
            val sign_text = binding.sign
            if(sign == ""){
                sign_text.setText("Owner : No Owner")
            }
            else{
                sign_text.setText("Owner : " + sign)
            }
        }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                startActivity(Intent(this@AddEditNoteActivity, MainActivity::class.java))
                this.finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createViewModel() {
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
            .getInstance(application)).get(NoteViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    private fun checkForNewNote(){
        val noteType = intent.getStringExtra("noteType")
        // If Pre Created Note clicke

        if(noteType.equals("Edit")) {
            // Getting Value of noteTitle and noteDescription from intent( extracting from database )
            val noteTitle = intent.getStringExtra("noteTitle")
            val noteDescription = intent.getStringExtra("noteDescription")

            noteTitleEdit.setText(noteTitle)
            noteDescriptionEdit.setText(noteDescription)
            btn.setText("Update Content")
            noteID = intent.getIntExtra("noteID", -1)
        }
        // If New Note is Creating
        else{
            btn.setText("Save Note")
        }
    }

    private fun onClickAddButton(btn:Button){
        val noteType = intent.getStringExtra("noteType")
        btn.setOnClickListener {
            val noteTitle = noteTitleEdit.text.toString()
            val noteDescription = noteDescriptionEdit.text.toString()
            if(noteType.equals("Edit")){
                if(noteTitle.isNotEmpty()){
                    val currentTime = SimpleDateFormat("dd MMM,  yyyy - HH:mm").format(Date())
                    val updateNote = Note(noteTitle, noteDescription, currentTime)
                    updateNote.id = noteID

                    viewModel.updateNote(updateNote)
                    //Toast.makeText(this, "Content Updated", Toast.LENGTH_SHORT).show()
                    Toast.makeText(this, noteTitle + "Note Updated", Toast.LENGTH_SHORT).show()
                } else{
                    // Will give Title name first 16 letters of description
                }
            }else{
                if(noteTitle.isNotEmpty()){
                    val currentTime = SimpleDateFormat("dd MMM,  yyyy - HH:mm").format(Date())
                    viewModel.addNote(Note(noteTitle, noteDescription, currentTime))
                    Toast.makeText(this, "Content Added", Toast.LENGTH_SHORT).show()
                }
                else{
                    // Will give Title name first 16 letters of description
                }
            }
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}