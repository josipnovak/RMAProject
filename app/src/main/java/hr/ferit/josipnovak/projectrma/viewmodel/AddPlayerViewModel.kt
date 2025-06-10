package hr.ferit.josipnovak.projectrma.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import hr.ferit.josipnovak.projectrma.FirebaseAuth
import hr.ferit.josipnovak.projectrma.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AddPlayerViewModel(
    private val fbAuth: FirebaseAuth,
    private val db: FirebaseFirestore,
): ViewModel() {
    private val _message = MutableStateFlow(" ")
    val message: StateFlow<String> = _message

    fun getUserDetails(
        onSuccess: (String, String, String) -> Unit
    ) {
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
                            val userId = user.id
                            val clubId = user.getString("clubId") ?: "No Club ID"
                            val role = user.getString("role") ?: "No Role"
                            onSuccess(userId, clubId, role)
                        }
                    }
            }
        }
    }

    fun addNewPlayer(player: User, clubId: String, onSuccess: () -> Unit) {
        if(player.position == "" || player.name == "") {
            _message.value = "Name and position cannot be empty"
            return
        }
        val newPlayerRef = db.collection("users").document()
        player.id = newPlayerRef.id
        newPlayerRef.set(player)
            .addOnSuccessListener {
                db.collection("clubs").document(clubId)
                    .update("players", FieldValue.arrayUnion(player.id))
                    .addOnSuccessListener {
                        onSuccess()
                    }
            }
    }
}