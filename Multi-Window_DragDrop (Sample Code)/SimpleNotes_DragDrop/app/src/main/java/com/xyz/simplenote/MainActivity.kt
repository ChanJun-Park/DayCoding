package com.xyz.simplenote

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
	lateinit var notesAdapter: NotesAdapter
	var notesList = ArrayList<NoteModel>()
	private val databaseHandler: DatabaseHandler = DatabaseHandler(this)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		supportActionBar!!.hide()

		val textView1: TextView = findViewById(R.id.textView2)

		val listView: GridView = findViewById(R.id.listView)
		notesList.addAll(databaseHandler.viewNote())
		if (notesList.size != 0)
			textView1.visibility = View.GONE
		notesAdapter = NotesAdapter(this, notesList)
		listView.adapter = notesAdapter

		listView.onItemClickListener =
			AdapterView.OnItemClickListener { _, _, i, _ ->
				val intent = Intent(this, NewNote::class.java)
				intent.putExtra("ID", notesList.get(i).id)
				startActivity(intent)
			}
		listView.onItemLongClickListener =
			AdapterView.OnItemLongClickListener { _, _, i, _ ->
				// To delete the data from the App
				AlertDialog.Builder(this@MainActivity)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle("Are you sure?")
					.setMessage("Do you want to delete this note?")
					.setPositiveButton(
						"Yes",
						DialogInterface.OnClickListener { _, _ ->
							databaseHandler.deleteNote(notesList[i])
							notesList.removeAt(i)
							notesAdapter.notifyDataSetChanged()
							if (notesList.size == 0)
								textView1.visibility = View.VISIBLE

						}).setNegativeButton("No", null).show()
				true
			}

		val button: View = findViewById(R.id.newnote)
		button.setOnClickListener {
			val intent = Intent(this, NewNote::class.java)
			startActivity(intent)
			listView.adapter = null;
			notesList.clear()
			notesList.addAll(databaseHandler.viewNote())
			textView1.visibility = View.GONE
			notesAdapter = NotesAdapter(this, notesList)
			listView.adapter = notesAdapter
		}
	}

	override fun onResume() {
		super.onResume()
		val listView: GridView = findViewById(R.id.listView)
		listView.adapter = null;
		notesList.clear()
		notesList.addAll(databaseHandler.viewNote())
		notesAdapter = NotesAdapter(this, notesList)
		listView.adapter = notesAdapter
	}

	override fun onBackPressed() {
		AlertDialog.Builder(this)
			.setMessage("Are you sure you want to exit?")
			.setCancelable(false)
			.setPositiveButton(
				"Yes"
			) { _, _ -> this@MainActivity.finish() }
			.setNegativeButton("No", null)
			.show()
	}
}
