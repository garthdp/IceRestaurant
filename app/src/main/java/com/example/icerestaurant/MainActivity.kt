package com.example.icerestaurant

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue

var userName : String = ""
var userFood : String = ""
class MainActivity : AppCompatActivity() {
    private lateinit var rootNode: FirebaseDatabase
    private lateinit var userReference : DatabaseReference
    private lateinit var listView : ListView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val txtNameInput : EditText = findViewById(R.id.txtNameInput)
        val txtFoodInput : EditText = findViewById(R.id.txtFoodInput)
        val btnAdd : Button = findViewById(R.id.btnAdd)

        listView = findViewById(R.id.lsOutput)
        rootNode = FirebaseDatabase.getInstance()
        userReference = rootNode.getReference("users")

        btnAdd.setOnClickListener{
            val dc = DataClass(txtNameInput.text.toString(), txtFoodInput.text.toString())
            userReference.child(dc.name).setValue(dc)
        }

        val list = ArrayList<String>()
        val adapter = ArrayAdapter<String>(this, R.layout.listitem, list)

        listView.adapter = adapter

        userReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot)
            {
                list.clear()
                for (snapshot1 in snapshot.children){
                    val dc2  = snapshot1.getValue(DataClass::class.java)
                    val txt  = "Name: ${dc2?.name}, Food: ${dc2?.food}"
                    txt?.let { list.add(it) }
                }
                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(error:DatabaseError)
            {

            }
        })
        listView.setOnClickListener{
            val intent = Intent(this, item_delete::class.java)
            startActivity(intent)
        }
    }
}