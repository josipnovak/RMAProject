package hr.ferit.josipnovak.projectrma.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import hr.ferit.josipnovak.projectrma.FirebaseAuth
import hr.ferit.josipnovak.projectrma.model.Event
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AddEventViewModel(
    private val fbAuth: FirebaseAuth,
    private val db: FirebaseFirestore,
) : ViewModel(){

    private val _message = MutableStateFlow(" ")
    val message: StateFlow<String> = _message

    fun addNewEvent(
        event: Event,
        onSuccess: (Boolean) -> Unit
    ) {
        if (event.name.isEmpty() || event.date.isEmpty() || event.time.isEmpty()) {
            _message.value = "Please fill in all fields."
            return
        }
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
                            val newEventRef = db.collection("events").document()
                            event.id = newEventRef.id
                            event.clubId = clubId
                            newEventRef.set(event)
                                .addOnSuccessListener {
                                    db.collection("clubs").document(clubId)
                                        .update("events", FieldValue.arrayUnion(event.id))
                                        .addOnSuccessListener {
                                            onSuccess(true)
                                        }
                                }
                        }
                    }
            }
        }
    }
}