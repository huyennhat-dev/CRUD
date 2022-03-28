package com.example.crud

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var edName:EditText
    private lateinit var edEmail:EditText
    private lateinit var edContact:EditText
    private lateinit var edAddress:EditText
    private lateinit var btnAdd:Button
    private lateinit var btnView:Button
    private lateinit var btnUpdate:Button

    private lateinit var sqLiteHelper: SQLiteHelper
    private lateinit var recyclerView: RecyclerView
    private var adapter: StudentAdapter? = null
    private var std:StudentModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        initRecyclerVew()
        sqLiteHelper = SQLiteHelper(this)

        btnAdd.setOnClickListener{addStudent()}
        btnView.setOnClickListener{getStudent()}
        btnUpdate.setOnClickListener{updateStudent()}

        adapter?.setOnClickItem {
            edName.setText(it.name)
            edEmail.setText(it.email)
            edContact.setText(it.contact)
            edAddress.setText(it.address)
            std = it
        }

        adapter?.setOnClickDeleteItem {
            deleteStudent(it.id)
        }
    }

    private fun deleteStudent(id:Int){

        val builder = AlertDialog.Builder(this)
//        builder.setTitle("Android Alert")
        builder.setMessage("Bạn có muốn xóa mục này?")
        builder.setCancelable(true)

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->

            sqLiteHelper.deleteStudentById(id)
            getStudent()

            Toast.makeText(this,"Delete Success!!!", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton(android.R.string.no) { dialog, which ->
            Toast.makeText(this,"Delete Faled...", Toast.LENGTH_SHORT).show()
        }

        val alert = builder.create()
        alert.show()

    }

    private fun updateStudent() {
        val name = edName.text.toString()
        val email = edEmail.text.toString()
        val contact = edContact.text.toString()
        val address = edAddress.text.toString()

        if(name == std?.name && email == std?.email && contact == std?.contact && address == std?.address){
            Toast.makeText(this, "Bạn chưa thay đổi gì cả...", Toast.LENGTH_SHORT).show()
            return
        }

        if(name == "" || email == "" || contact =="" || address == ""){
            Toast.makeText(this, "Vui lòng không bỏ trống!", Toast.LENGTH_SHORT).show()
            return
        }else{
            if(std == null) return

            val std = StudentModel(id= std!!.id, name = name, email = email, contact = contact, address = address)
            val status = sqLiteHelper.updateStudent(std)
            if(status > -1){
                clearEditText()
                getStudent()
            }else{
                Toast.makeText(this, "Update failed...", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun getStudent() {
        val stdList = sqLiteHelper.getAllStudent()
        Log.e("ppp", "${stdList.size}")

        //ok now
        adapter?.addItems(stdList)
    }

    private fun addStudent() {
        val name = edName.text.toString()
        val email = edEmail.text.toString()
        val contact = edContact.text.toString()
        val address = edAddress.text.toString()
        if(name.isEmpty() || email.isEmpty() || contact.isEmpty() || address.isEmpty()){
            Toast.makeText(this, "Vui lòng không bỏ trống!", Toast.LENGTH_LONG).show()
        }else{
            if(name == std?.name && email == std?.email && contact == std?.contact && address == std?.address){
                Toast.makeText(this, "Đã tồn tại rồi...", Toast.LENGTH_SHORT).show()
                return
            }else{
                val std = StudentModel(name = name, email = email, contact = contact, address = address)
                val status = sqLiteHelper.insertStudent(std)
                if(status > -1){
                    Toast.makeText(this, "Student Added...", Toast.LENGTH_LONG).show()
                    clearEditText()
                    getStudent()
                }else{
                    Toast.makeText(this, "Record not saved...", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun clearEditText() {
        edName.setText("")
        edEmail.setText("")
        edContact.setText("")
        edAddress.setText("")
        edName.requestFocus()
    }

    private fun initRecyclerVew() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = StudentAdapter()
        recyclerView.adapter = adapter
    }

    private fun initView() {
        edName = findViewById(R.id.edName)
        edEmail = findViewById(R.id.edEmail)
        edContact = findViewById(R.id.edContact)
        edAddress = findViewById(R.id.edAddress)
        btnAdd = findViewById(R.id.btnAdd)
        btnView = findViewById(R.id.btnView)
        btnUpdate = findViewById(R.id.btnUpdate)
        recyclerView = findViewById(R.id.recyclerView)
    }
}