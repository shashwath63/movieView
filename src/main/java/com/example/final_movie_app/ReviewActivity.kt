import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class ReviewActivity : AppCompatActivity() {

    private lateinit var reviewTextView: TextView
    private lateinit var reviewEditText: EditText
    private lateinit var addButton: Button
    private lateinit var deleteButton: Button
    private lateinit var updateButton: Button

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var reviewRef: DatabaseReference
    private lateinit var currentUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        reviewTextView = findViewById(R.id.reviewTextView)
        reviewEditText = findViewById(R.id.reviewEditText)
        addButton = findViewById(R.id.addButton)
        deleteButton = findViewById(R.id.deleteButton)
        updateButton = findViewById(R.id.updateButton)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        currentUser = firebaseAuth.currentUser!!

        // Set the review reference to the current user's review data
        reviewRef = firebaseDatabase.getReference("reviews/${currentUser.uid}")

        addButton.setOnClickListener {
            addReview()
        }

        deleteButton.setOnClickListener {
            deleteReview()
        }

        updateButton.setOnClickListener {
            updateReview()
        }

        // Listen for changes in the review data
        reviewRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val review = snapshot.getValue(String::class.java)
                reviewTextView.text = review
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Failed to fetch review data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addReview() {
        val newReview = reviewEditText.text.toString().trim()

        if (newReview.isNotEmpty()) {
            reviewRef.setValue(newReview)
                .addOnSuccessListener {
                    Toast.makeText(this, "Review added", Toast.LENGTH_SHORT).show()
                    reviewEditText.text.clear()
                    reviewTextView.text = newReview // Update the reviewTextView with the new review
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to add review", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Please enter a review", Toast.LENGTH_SHORT).show()
        }
    }


    private fun deleteReview() {
        reviewRef.removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, "Review deleted", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to delete review", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateReview() {
        val updatedReview = reviewEditText.text.toString().trim()

        if (updatedReview.isNotEmpty()) {
            reviewRef.setValue(updatedReview)
                .addOnSuccessListener {
                    Toast.makeText(this, "Review updated", Toast.LENGTH_SHORT).show()
                    reviewEditText.text.clear()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to update review", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Please enter a review", Toast.LENGTH_SHORT).show()
        }
    }
}
