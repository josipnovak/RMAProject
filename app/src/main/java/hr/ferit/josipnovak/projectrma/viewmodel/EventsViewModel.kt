package hr.ferit.josipnovak.projectrma.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import hr.ferit.josipnovak.projectrma.FirebaseAuth
import hr.ferit.josipnovak.projectrma.model.Event
import kotlin.text.get
import kotlin.text.set

class EventsViewModel(
    private val fbAuth: FirebaseAuth,
    private val db: FirebaseFirestore,
) : ViewModel(){
    fun addNewEvent(event: Event){
        getClubId {clubId ->
            if (clubId != "No Club ID") {
                val newEventRef = db.collection("events").document()
                event.id = newEventRef.id
                newEventRef.set(event)
                    .addOnSuccessListener {
                        db.collection("clubs").document(clubId)
                            .update("events", FieldValue.arrayUnion(event.id))
                            .addOnSuccessListener {
                                println("Event added successfully to the club.")
                            }
                            .addOnFailureListener { e ->
                                println("Error updating club with new event: ${e.message}")
                            }
                    }
                    .addOnFailureListener { e ->
                        println("Error adding new event: ${e.message}")
                    }
            } else {
                Log.d("AAA", "No Club ID found for the current user.")
            }
        }
    }

    private fun getClubId(callback: (String) -> Unit) {
        val currentUser = fbAuth.getCurrentUser()
        if (currentUser != null) {
            val email = currentUser.email
            if (email != null) {
                db.collection("users")
                    .whereEqualTo("email", email)
                    .get()
                    .addOnSuccessListener { documents ->
                        if (!documents.isEmpty) {
                            val user = documents.first()
                            val clubId = user.getString("clubId") ?: "No Club ID"
                            callback(clubId)
                        } else {
                            callback("No Club ID")
                        }
                    }
                    .addOnFailureListener {
                        callback("No Club ID")
                    }
            } else {
                callback("No Club ID")
            }
        } else {
            callback("No Club ID")
        }
    }
}