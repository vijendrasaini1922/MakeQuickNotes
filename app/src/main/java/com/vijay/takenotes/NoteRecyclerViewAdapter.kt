package com.vijay.takenotes

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vijay.takenotes.Notes.Note

class NoteRecyclerViewAdapter(
    val ctx: Context,
    val noteClickInterface: NoteClickInterface,
    val noteClickDeleteInterface: NoteClickDeleteInterface
    ): RecyclerView.Adapter<NoteRecyclerViewAdapter.ViewHolder>() {

        val allNotes = ArrayList<Note>()

        inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            val noteTitleView = itemView.findViewById<TextView>(R.id.noteTitle)
            val noteTimeView = itemView.findViewById<TextView>(R.id.noteTime)
            val noteDeleteImage = itemView.findViewById<ImageView>(R.id.noteDelete)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(ctx).inflate(R.layout.note_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.noteTitleView.setText(allNotes.get(position).noteTitle)
        holder.noteTimeView.setText("Last Updated : " + allNotes.get(position).noteTime)

        holder.noteDeleteImage.setOnClickListener{
            noteClickDeleteInterface.onDeleteIconClick(allNotes.get(position))
        }

        holder.itemView.setOnClickListener {
            noteClickInterface.onNoteClick(allNotes.get(position))
        }
    }

    override fun getItemCount(): Int {
        return allNotes.size
    }

    fun updateList(newList: List<Note>){
        allNotes.clear()
        allNotes.addAll(newList)
        notifyDataSetChanged()
    }
}

interface NoteClickDeleteInterface{
    fun onDeleteIconClick(note: Note)
}

interface NoteClickInterface{
    fun onNoteClick(note: Note)
}