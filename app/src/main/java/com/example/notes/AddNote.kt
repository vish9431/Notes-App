package com.example.notes

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.KeyEvent
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.text.HtmlCompat
import com.example.notes.Models.Note
import com.example.notes.databinding.ActivityAddNoteBinding
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

class AddNote : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var note:Note
    private lateinit var old_note:Note
    var isUpdate = false

    @SuppressLint("SuspiciousIndentation", "SdCardPath")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)



        try{
            old_note = intent.getSerializableExtra("current_note") as Note
            binding.etTitle.setText(old_note.title)
            binding.etNote.setText(old_note.note)
            isUpdate=true
        }
        catch(e : Exception){
            e.printStackTrace()
        }

        binding.imgCheck.setOnClickListener{
            val title=binding.etTitle.text.toString()
            val note_desc=binding.etNote.text.toString()
            if(title.isNotEmpty() || note_desc.isNotEmpty()){
                val formatter = SimpleDateFormat("EEE,d MMM yyyy HH:mm a")

                if(isUpdate){
                    note=Note(
                        old_note.id,title,note_desc,formatter.format(Date())
                    )
                }
                else{
                    note=Note(
                        null,title,note_desc,formatter.format(Date())
                    )
                }
                val intent= Intent()
                intent.putExtra("note",note)
                setResult(RESULT_OK,intent)
                finish()
            }
            else{
                Toast.makeText(this@AddNote,"Please enter some data",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
        }
    }

        binding.imgBackArrow.setOnClickListener {
            onBackPressed()
        }

        binding.imgShare.setOnClickListener{
            val title_item=binding.etTitle.text.toString()
            val note_item=binding.etNote.text.toString()
            val final_note="$title_item  \n\n$note_item"
                if (!title_item.isEmpty() && !note_item.isEmpty()) {
                    val file: File = File(this.getFilesDir(), "text")
                    if (!file.exists()) {
                        file.mkdir()
                    }
                    try {
                        val gpxfile = File(file, "$title_item.txt")
                        val writer = FileWriter(gpxfile)
                        writer.append(final_note)
                        writer.flush()
                        writer.close()
                    }
                    catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

            val ffile = File(filesDir, "text/$title_item.txt")
                    val uri = FileProvider.getUriForFile(this, "com.example.notes.fileprovider", ffile)
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.type = "text/plain"
                    intent.putExtra(Intent.EXTRA_STREAM, uri)
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    startActivity(Intent.createChooser(intent, "Share File"))
                }
        val etNotefi: EditText = findViewById(R.id.et_note)
        Linkify.addLinks(etNotefi,Linkify.WEB_URLS)
        etNotefi.movementMethod = LinkMovementMethod.getInstance()

        }
    }

