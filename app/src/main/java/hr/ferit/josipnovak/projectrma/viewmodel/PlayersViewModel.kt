package hr.ferit.josipnovak.projectrma.viewmodel

import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import hr.ferit.josipnovak.projectrma.FirebaseAuth
import hr.ferit.josipnovak.projectrma.model.User
import kotlinx.coroutines.tasks.await

class PlayersViewModel(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
) : ViewModel() {

    fun getUserDetails(
        onSuccess: (String, String, String) -> Unit,
        onError: (String) -> Unit
    ) {
        val currentUser = auth.getCurrentUser()
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
                        } else {
                            onError("User not found in database")
                        }
                    }
                    .addOnFailureListener { exception ->
                        onError(exception.message ?: "Error fetching user details")
                    }
            } else {
                onError("Email not found for the current user")
            }
        } else {
            onError("No user is logged in")
        }
    }


    fun fetchAndGroupPlayers(
        clubId: String,
        onSuccess: (Map<String, List<User>>) -> Unit,
        onError: (String) -> Unit
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
                    .addOnFailureListener { exception ->
                        onError(exception.message ?: "Error fetching player details")
                    }
            }
            .addOnFailureListener { exception ->
                onError(exception.message ?: "Error fetching players array")
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

    fun addNewPlayer(player: User, clubId: String){
        val newPlayerRef = db.collection("users").document()
        player.id = newPlayerRef.id
        newPlayerRef.set(player)
            .addOnSuccessListener {
                db.collection("clubs").document(clubId)
                    .update("players", FieldValue.arrayUnion(player.id))
                    .addOnSuccessListener {
                        println("Player added successfully")
                    }
                    .addOnFailureListener { e ->
                        println("Error updating club with new player: ${e.message}")
                    }
            }
            .addOnFailureListener { e ->
                println("Error adding new player: ${e.message}")
            }
    }

    fun updatePlayer(player: User) {
        val playerRef = db.collection("users").document(player.id)
        playerRef.set(player)
            .addOnSuccessListener {
                println("Player updated successfully")
            }
            .addOnFailureListener { e ->
                println("Error updating player: ${e.message}")
            }
    }
}
