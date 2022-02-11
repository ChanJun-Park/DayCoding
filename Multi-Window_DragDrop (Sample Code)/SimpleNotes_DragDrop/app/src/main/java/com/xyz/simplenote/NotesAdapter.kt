@file:Suppress("NAME_SHADOWING", "DEPRECATION", "DEPRECATED_IDENTITY_EQUALS")

package com.xyz.simplenote

import android.content.Context
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView


class NotesAdapter(context: Context?, val noteList: List<NoteModel>) :
	ArrayAdapter<NoteModel?>(context!!, 0, noteList) {

	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
		// Get the data item for this position
		var convertView: View? = convertView
		val noteModel: NoteModel? = getItem(position)
		// Check if an existing view is being reused, otherwise inflate the view
		if (convertView == null) {
			convertView =
				LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
		}
		// Lookup view for data population
		val noteTitle = convertView?.findViewById(R.id.itemTitle) as TextView
		if (noteModel?.title?.length!! > 15) {
			if (context.getResources().getConfiguration().screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK === Configuration.SCREENLAYOUT_SIZE_LARGE) {
				noteTitle.setText(noteModel.title.substring(0, 15) + "...")
				// on a large screen device ...
			} else
				noteTitle.setText(noteModel.title)
		} else
			noteTitle.setText(noteModel.title)
		val noteDes: TextView = convertView.findViewById(R.id.itemDesc)
		var description: String = noteModel.desc.toString()
		var index: Int
		if (description.contains("\n")) {
			index = description.indexOf("\n", 0)
			if (index > 10)
				noteDes.setText(description.substring(0, 10) + "...")
			else
				noteDes.setText(description.substring(0, index) + "...")
		} else if (description.length > 10)
			noteDes.setText(description.substring(0, 10) + "...")
		else
			noteDes.setText(description)
		return convertView
	}
}
