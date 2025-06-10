package hr.ferit.josipnovak.projectrma.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import hr.ferit.josipnovak.projectrma.FirebaseAuth
import hr.ferit.josipnovak.projectrma.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await

class PlayersViewModel(
    private val fbAuth: FirebaseAuth,
    private val db: FirebaseFirestore,
) : ViewModel() {

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

    fun getPlayers(
        clubId: String,
        onSuccess: (Map<String, List<User>>) -> Unit
    ) {
        db.collection("clubs").document(clubId)
            .get()
            .addOnSuccessListener { document ->
                val playerIds = document.get("players") as? List<String> ?: emptyList()
                val players = mutableListOf<User>()

                if (playerIds.isEmpty()) {
                    onSuccess(emptyMap())
                    return@addOnSuccessListener
                }

                val tasks = playerIds.map { playerId ->
                    db.collection("users").document(playerId).get()
                }

                Tasks.whenAllSuccess<DocumentSnapshot>(tasks)
                    .addOnSuccessListener { snapshots ->
                        snapshots.forEach { snapshot ->
                            val user = snapshot.toObject(User::class.java)
                            if (user != null) {
                                players.add(user.copy(id = snapshot.id))
                            }
                        }

                        val groupedPlayers = players.groupBy { it.position }

                        onSuccess(groupedPlayers)
                    }
            }
    }

    suspend fun getPlayerById(playerId: String): User {
        return try {
            val documents = db.collection("users")
                .whereEqualTo("id", playerId)
                .get()
                .await()

            if (!documents.isEmpty) {
                documents.first().toObject(User::class.java).copy(id = documents.first().id) ?: User()
            } else {
                throw Exception("Player not found")
            }
        } catch (e: Exception) {
            throw Exception(e.message ?: "Error fetching player details")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun deletePlayer(
        playerId: String,
        onSuccess : () -> Unit,
    ){
        var clubId: String
        db.collection("users")
            .whereEqualTo("id", playerId)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val user = documents.first()
                    clubId = user.getString("clubId") ?: "No Club ID"
                    db.collection("clubs").document(clubId)
                        .update("players", FieldValue.arrayRemove(playerId))
                        .addOnSuccessListener {
                            db.collection("users").document(playerId)
                                .delete()
                                .addOnSuccessListener {
                                    onSuccess()
                                }
                        }
                }
            }
    }
    fun isCoach(onSuccess: (Boolean) -> Unit) {
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
                            Log.d("EventsViewModel", "User document: ${user.getString("role")}")
                            val role = user.getString("role") ?: "No Role"
                            onSuccess(role == "coach")
                        }
                    }
            }
        }
        onSuccess(false)
    }
}
