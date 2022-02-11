package com.xyz.simplenote

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteException

//creating the database logic, extending the SQLiteOpenHelper base class
class DatabaseHandler(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "NotesDB"
        private val TABLE_NOTE = "NoteTable"
        private val KEY_ID = "id"
        private val KEY_TITLE = "title"
        private val KEY_DESC = "desc"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        //creating table with fields
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_NOTE + "("
                + KEY_ID + " INTEGER PRIMARY KEY autoincrement," + KEY_TITLE + " TEXT,"
                + KEY_DESC + " TEXT" + ")")
        db?.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTE)
        onCreate(db)
    }


    //method to insert data
    fun addNote(note: NoteModel):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE, note.title)
        contentValues.put(KEY_DESC,note.desc )
        // Inserting Row
        val success = db.insert(TABLE_NOTE, null, contentValues)
        db.close() // Closing database connection
        return success
    }
    //method to read data
    fun viewNote():List<NoteModel>{
        val noteList:ArrayList<NoteModel> = ArrayList<NoteModel>()
        val selectQuery = "SELECT  * FROM $TABLE_NOTE"
        val db = this.readableDatabase
        var cursor: Cursor?
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var id: Int
        var title: String
        var desc: String
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex("id"))
                title = cursor.getString(cursor.getColumnIndex("title"))
                desc = cursor.getString(cursor.getColumnIndex("desc"))
                val note= NoteModel(id = id, title = title, desc = desc)
                noteList.add(note)
            } while (cursor.moveToNext())
        }
        return noteList
    }

    //method to get data
    fun getNote(id: Int):NoteModel{
        var noteID:Int=0
        var noteTitle:String=""
        var noteDesc:String =""
        val db = this.readableDatabase
        val ID = "id"
        val selectQuery = "SELECT  * FROM " + TABLE_NOTE + " WHERE " + ID + " = '" + id + "'"
        // Updating Row
        val cursor = db.rawQuery(selectQuery, null)

        try {
            if (cursor.getCount() != 0) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    do {
                         noteID = cursor.getInt(cursor.getColumnIndex(ID))
                         noteTitle = cursor.getString(cursor.getColumnIndex("title"))
                         noteDesc = cursor.getString(cursor.getColumnIndex("desc"))
                    } while ((cursor.moveToNext()));
                }
            }
        } finally {
            cursor.close();
        }
        return NoteModel(noteID, noteTitle, noteDesc)
    }



    //method to update data
    fun updateNote(note: NoteModel):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, note.id)
        contentValues.put(KEY_TITLE, note.title)
        contentValues.put(KEY_DESC,note.desc )

        // Updating Row
        val success = db.update(TABLE_NOTE, contentValues,"id="+note.id,null)
        db.close() // Closing database connection
        return success
    }
    //method to delete data
    fun deleteNote(note: NoteModel):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, note.id)
        // Deleting Row
        val success = db.delete(TABLE_NOTE,"id="+note.id,null)
        db.close() // Closing database connection
        return success
    }
}
