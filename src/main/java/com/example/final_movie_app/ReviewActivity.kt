package com.example.final_movie_app

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class ReviewActivity : AppCompatActivity() {

    private lateinit var reviewEditText: EditText
    private lateinit var addReviewButton: Button
    private lateinit var reviewContainer: LinearLayout
    private lateinit var updateButton: Button
    private lateinit var deleteButton: Button

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private var currentUser: FirebaseUser? = null

    private var reviewId: String? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        // Initialize Firebase
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser

        // Get current user ID
        val currentUserId = currentUser?.uid

        // Find views
        reviewEditText = findViewById(R.id.reviewEditText)
        addReviewButton = findViewById(R.id.addReviewButton)
        reviewContainer = findViewById(R.id.reviewContainer)
        updateButton = findViewById(R.id.updateButton)
        deleteButton = findViewById(R.id.deleteButton)

        // Set background color
        findViewById<View>(R.id.reviewLayout).setBackgroundColor(Color.parseColor("#FFFFFF"))

        // Set border color for edit text
        reviewEditText.backgroundTintList = resources.getColorStateList(R.color.purple)

        // Set text color for buttons
        addReviewButton.setTextColor(Color.WHITE)
        updateButton.setTextColor(Color.WHITE)
        deleteButton.setTextColor(Color.WHITE)

        // Set background color for buttons
        addReviewButton.backgroundTintList = resources.getColorStateList(R.color.purple)
        updateButton.backgroundTintList = resources.getColorStateList(R.color.purple)
        deleteButton.backgroundTintList = resources.getColorStateList(R.color.purple)

        // Get the review ID from the previous activity (or generate a new one if necessary)
        reviewId = intent.getStringExtra("reviewId")
        if (reviewId == null) {
            reviewId = firestore.collection("reviews").document().id
        }

        // Retrieve and display the review data from Firestore
        retrieveReviewData(currentUserId)

        // Add review button click listener
        addReviewButton.setOnClickListener {
            val reviewText = reviewEditText.text.toString().trim()
            if (reviewText.isNotEmpty()) {
                // Save the review to Firestore
                val review = Review(currentUserId!!, reviewText)
                firestore.collection("reviews").document(reviewId!!)
                    .set(review)
                    .addOnSuccessListener {
                        reviewEditText.text.clear()
                        showToast("Review added successfully")

                        // Create a new TextView to display the review
                        val reviewTextView = TextView(this)
                        reviewTextView.text = reviewText

                        // Add the review TextView to the review container
                        reviewContainer.addView(reviewTextView)

                        // Hide the addReviewButton and show the updateButton and deleteButton
                        addReviewButton.visibility = View.GONE
                        updateButton.visibility = View.VISIBLE
                        deleteButton.visibility = View.VISIBLE
                    }
                    .addOnFailureListener { exception ->
                        showToast("Error: ${exception.message}")
                    }
            }
        }

        // Update button click listener
        updateButton.setOnClickListener {
            val reviewText = reviewEditText.text.toString().trim()
            if (reviewText.isNotEmpty()) {
                // Update the review in Firestore
                val review = Review(currentUserId!!, reviewText)
                firestore.collection("reviews").document(reviewId!!)
                    .set(review)
                    .addOnSuccessListener {
                        reviewEditText.text.clear()
                        showToast("Review updated successfully")

                        // Update the displayed review
                        val reviewTextView = reviewContainer.getChildAt(0) as TextView
                        reviewTextView.text = reviewText
                    }
                    .addOnFailureListener { exception ->
                        showToast("Error: ${exception.message}")
                    }
            }
        }

        // Delete button click listener
        deleteButton.setOnClickListener {
            // Delete the review from Firestore
            firestore.collection("reviews").document(reviewId!!)
                .delete()
                .addOnSuccessListener {
                    reviewContainer.removeAllViews()
                    updateButton.visibility = View.GONE
                    deleteButton.visibility = View.GONE
                    addReviewButton.visibility = View.VISIBLE
                    showToast("Review deleted successfully")
                }
                .addOnFailureListener { exception ->
                    showToast("Error: ${exception.message}")
                }
        }
    }

    private fun retrieveReviewData(currentUserId: String?) {
        // Retrieve the review data from Firestore
        firestore.collection("reviews").document(reviewId!!)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val review = documentSnapshot.toObject(Review::class.java)
                if (review != null) {
                    // Set the existing review text in the EditText
                    reviewEditText.setText(review.reviewText)

                    // Show update and delete buttons if the current user wrote the review
                    if (review.userId == currentUserId) {
                        updateButton.visibility = View.VISIBLE
                        deleteButton.visibility = View.VISIBLE
                    } else {
                        // Show add review button if no review is found
                        addReviewButton.visibility = View.VISIBLE
                    }
                } else {
                    // Hide the update and delete buttons
                    updateButton.visibility = View.GONE
                    deleteButton.visibility = View.GONE

                    // Show the add review button
                    addReviewButton.visibility = View.VISIBLE
                }
            }
            .addOnFailureListener { exception ->
                showToast("Error: ${exception.message}")
            }
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
