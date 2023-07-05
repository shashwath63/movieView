package com.example.final_movie_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.content.Intent
import android.widget.Button

data class Student(val name: String, val usn: String)

class HomePage : AppCompatActivity() {

    private lateinit var btnEnter: Button

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StudentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Create a list of students
        val students = listOf(
            Student("Jss academy of technical education,","Bangalore"),
            Student("Shashwath BN", "1JS20CS148"),
            Student("Sharath kumar HV", "1JS20CS147"),
            Student("Under the guidence of  ","Namitha SJ")
        )

        adapter = StudentAdapter(students)
        recyclerView.adapter = adapter
        btnEnter = findViewById(R.id.btn_enter)
        btnEnter.setOnClickListener {
            // Start the home page activity
            val intent = Intent(this@HomePage, login::class.java)
            startActivity(intent)
        }
    }
}

class StudentAdapter(private val students: List<Student>) :
    RecyclerView.Adapter<StudentAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.tv_name)
        val usnTextView: TextView = view.findViewById(R.id.tv_usn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_student, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val student = students[position]
        holder.nameTextView.text = student.name
        holder.usnTextView.text = student.usn
    }

    override fun getItemCount(): Int {
        return students.size
    }

}
