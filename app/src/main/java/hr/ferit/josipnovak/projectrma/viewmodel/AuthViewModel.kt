package hr.ferit.josipnovak.projectrma.viewmodel

import android.content.Context
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import hr.ferit.josipnovak.projectrma.FirebaseAuth
import hr.ferit.josipnovak.projectrma.R
import hr.ferit.josipnovak.projectrma.model.Club
import hr.ferit.josipnovak.projectrma.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class AuthViewModel(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val context: Context
) : ViewModel() {

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message


    fun login(email: String, password: String, onSuccess: () -> Unit, ) {
        if (email.isEmpty() || password.isEmpty()) {
            _message.value = context.getString(R.string.enter_values)
            return
        }

        auth.loginUser(email, password) { success, error ->
            if (success) {
                onSuccess()
            } else {
                _message.value = context.getString(R.string.error_message)
            }

        }
    }

    fun register(
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
        teamName: String,
        role: String,
        clubCode: String?,
        position: String?,
        onSuccess: () -> Unit
    ) {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ||
            (teamName.isEmpty() && role == "coach") ||
            (clubCode?.isEmpty() == true && role == "player" && position?.isEmpty() == true)) {
            _message.value = context.getString(R.string.enter_values)
            return
        }

        if (password != confirmPassword) {
            _message.value = context.getString(R.string.password_error)
            return
        }

        viewModelScope.launch {
            if (role == "player") {
                db.collection("clubs").whereEqualTo("clubCode", clubCode?.toIntOrNull()).get()
                    .addOnSuccessListener { querySnapshot ->
                        if (!querySnapshot.isEmpty) {
                            val clubId = querySnapshot.documents[0].id
                            createUserAccount(name, email, password, role, clubId, position, onSuccess) { userId ->
                                db.collection("clubs").document(clubId)
                                    .update("players", com.google.firebase.firestore.FieldValue.arrayUnion(userId))

                            }
                        }
                    }
            } else if (role == "coach") {
                auth.registerUser(email, password) { success, error ->
                    if (success) {
                        val userId = auth.getCurrentUser()?.uid ?: return@registerUser
                        val clubId = UUID.randomUUID().toString()
                        val generatedClubCode = (10000..99999).random()
                        val club = Club(
                            id = clubId,
                            name = teamName,
                            coachId = userId,
                            players = listOf(),
                            clubCode = generatedClubCode
                        )
                        val user = User(
                            id = userId,
                            name = name,
                            email = email,
                            role = "coach",
                            clubId = clubId
                        )
                        db.collection("clubs").document(clubId).set(club)
                            .addOnSuccessListener {
                                db.collection("users").document(userId).set(user)
                                    .addOnSuccessListener { onSuccess() }
                            }
                    }
                }
            }
        }
    }

    private fun createUserAccount(
        name: String,
        email: String,
        password: String,
        role: String,
        clubId: String,
        position: String?,
        onSuccess: () -> Unit,
        onUserCreated: (String) -> Unit
    ) {
        auth.registerUser(email, password) { success, error ->
            if (success) {
                val userId = auth.getCurrentUser()?.uid ?: return@registerUser
                val user = User(
                    id = userId,
                    name = name,
                    email = email,
                    role = role,
                    clubId = clubId,
                    position = position ?: "",
                )
                db.collection("users").document(userId).set(user)
                    .addOnSuccessListener {
                        onUserCreated(userId)
                        onSuccess()
                    }
                    .addOnFailureListener { e ->
                        _message.value = e.message ?: "Failed to save user"
                    }
            }
        }
    }
}