@file:Suppress("DEPRECATION", "NAME_SHADOWING")

package com.xyz.simplenote

import android.content.*
import android.content.ClipData.*
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class NewNote : AppCompatActivity() {

	var ID_FLAG = -1
	private lateinit var title: EditText
	private lateinit var desc: EditText


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_new_note)
		supportActionBar!!.hide()

		title = findViewById(R.id.title)
		desc = findViewById(R.id.desc)
		val pageTitle = findViewById<TextView>(R.id.pageTitle)

		/* Register a drag event listener */
		title.setOnDragListener(onDragListener)
		desc.setOnDragListener(onDragListenerDescription)

		title.setTextIsSelectable(true)
		desc.setTextIsSelectable(true)


		val intent: Intent = intent
		val id = intent.getIntExtra("ID", -1)
		if (id != -1) {
			ID_FLAG = -2
			pageTitle.text = "Update Note"
			val databaseHandler = DatabaseHandler(this)
			val note: NoteModel = databaseHandler.getNote(id)
			title.setText(note.title)
			desc.setText(note.desc)

		}

		if (savedInstanceState != null) {
			title.setText(savedInstanceState.getString("title"))
			desc.setText(savedInstanceState.getString("desc"))
		}

		val saveBtn: Button = findViewById(R.id.save)

		saveBtn.setOnClickListener {
			val titleValue: String = title.text.toString()
			if (titleValue.trim() == "") {
				Toast.makeText(this, "please input title, title cannot be blank", Toast.LENGTH_LONG).show()
				return@setOnClickListener
			}

			val descValue: String = desc.text.toString()
			if (descValue.trim() == "") {
				Toast.makeText(this, "please input description, description cannot be blank", Toast.LENGTH_LONG).show()
				return@setOnClickListener
			}

			val databaseHandler: DatabaseHandler = DatabaseHandler(this)
			if (-1 == ID_FLAG) {
				val status = databaseHandler.addNote(NoteModel(-1, titleValue, descValue))
				if (status > -1) {
					Toast.makeText(applicationContext, "record saved", Toast.LENGTH_LONG).show()
					title.text.clear()
					desc.text.clear()
				}
			}
			if (-2 == ID_FLAG) {
				val status = databaseHandler.updateNote(NoteModel(id, titleValue, descValue))
				if (status > -1) {
					Toast.makeText(applicationContext, "record updated", Toast.LENGTH_LONG).show()
					title.text.clear()
					desc.text.clear()
				}
			}

			finish()
		}
	}

	override fun onBackPressed() {
		finish()
	}

	override fun onSaveInstanceState(outState: Bundle) {
		val title = findViewById(R.id.title) as EditText
		val desc = findViewById(R.id.desc) as EditText

		outState.putString("title", title.getText().toString())
		outState.putString("desc", desc.getText().toString())
		super.onSaveInstanceState(outState)
	}


	private val onDragListenerDescription = View.OnDragListener { view, event ->
		/* Store the action type to a variable */

		when (event.action) {
			DragEvent.ACTION_DRAG_STARTED -> {
				// Determines if this View can accept the dragged data
				event.clipDescription ?: return@OnDragListener false
				return@OnDragListener event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)
			}
			DragEvent.ACTION_DRAG_ENTERED -> {
				// Applies a GRAY or any color tint to the View. Return true; the return value is ignored.
				view.background.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN)
				// Invalidate the view to force a redraw in the new tint
				view.invalidate()
				return@OnDragListener true
			}
			DragEvent.ACTION_DRAG_LOCATION ->                 // Ignore the event
				return@OnDragListener true
			DragEvent.ACTION_DRAG_EXITED -> {
				//It will clear a color filter .
				view.background.clearColorFilter()
				// Invalidate the view to force a redraw in the new tint
				view.invalidate()
				return@OnDragListener true
			}
			DragEvent.ACTION_DROP -> {
				val dragData: String
				/* Get and drop the text data */
				val item: Item = event.clipData.getItemAt(0)
				val mType = event.clipDescription.getMimeType(0)

				if (mType == "text/plain" || mType == "text/html") {
					// Gets the text data from the item.
					dragData = item.text.toString()
				} else {
					Toast.makeText(applicationContext, "Operation not allowed $mType", Toast.LENGTH_LONG).show()
					return@OnDragListener true
				}

				// Displays a message containing the dragged data.
				//Toast.makeText(this, "Dragged data is $dragData", Toast.LENGTH_LONG).show()
				// Turns off any color tints
				view.background.clearColorFilter()
				/* Request for drag & drop permission */
				val descEditText = findViewById<EditText>(R.id.desc)
				val tempData = descEditText.text.toString()
				descEditText.setText("$tempData $dragData")
				descEditText.setSelection(descEditText.text.length)
				return@OnDragListener true
			}
			DragEvent.ACTION_DRAG_ENDED -> {
				// Turns off any color tinting
				view.background.clearColorFilter()
				// Invalidates the view to force a redraw
				view.invalidate()

				return@OnDragListener true
			}
			else -> Log.e("Simple Note", "Unknown action type received by OnDragListener.")
		}
		return@OnDragListener false
	}

	private val onDragListener = View.OnDragListener { view, event ->

		/* Store the action type to a variable */
		when (event.action) {
			DragEvent.ACTION_DRAG_STARTED -> {
				val clipDesc = event.clipDescription ?: return@OnDragListener false
				// Determines if this View can accept the dragged data
				return@OnDragListener event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)
				// Returns false. During the current drag and drop operation, this View will
				// not receive events again until ACTION_DRAG_ENDED is sent.
			}
			DragEvent.ACTION_DRAG_ENTERED -> {
				// Applies a GRAY or any color tint to the View. Return true; the return value is ignored.
				view.background.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN)
				// Invalidate the view to force a redraw in the new tint
				view.invalidate()
				return@OnDragListener true
			}
			DragEvent.ACTION_DRAG_LOCATION ->
				return@OnDragListener true
			DragEvent.ACTION_DRAG_EXITED -> {
				//It will clear a color filter .
				view.background.clearColorFilter()
				// Invalidate the view to force a redraw in the new tint
				view.invalidate()
				return@OnDragListener true
			}
			DragEvent.ACTION_DROP -> {
				var dragData: String
				/* Get and drop the text data */
				val item: Item = event.clipData.getItemAt(0)
				val mType = event.clipDescription.getMimeType(0)

				if (mType == "text/plain" || mType == "text/html") {
					// Gets the text data from the item.
					dragData = item.text.toString()
				} else {
					Toast.makeText(applicationContext, "Operation not allowed $mType", Toast.LENGTH_LONG).show()
					return@OnDragListener true
				}

				// Displays a message containing the dragged data.
				// Toast.makeText(this, "Dragged data is $dragData", Toast.LENGTH_LONG).show()
				// Turns off any color tints
				view.background.clearColorFilter()
				/* Request for drag & drop permission */
				requestDragAndDropPermissions(event)
				val titleEditText = findViewById<EditText>(R.id.title)
				val tempData = titleEditText.text.toString()
				titleEditText.setText("$tempData $dragData")
				titleEditText.setSelection(titleEditText.text.length)
				return@OnDragListener true
			}
			DragEvent.ACTION_DRAG_ENDED -> {
				// Turns off any color tinting
				view.background.clearColorFilter()
				// Invalidates the view to force a redraw
				view.invalidate()

				// returns true; the value is ignored.
				return@OnDragListener true
			}
			else -> Log.e("Simple Note", "Unknown action type received by OnDragListener.")
		}
		return@OnDragListener false
	}
}
