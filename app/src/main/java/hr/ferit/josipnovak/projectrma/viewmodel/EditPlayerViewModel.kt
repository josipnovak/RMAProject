package hr.ferit.josipnovak.projectrma.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import hr.ferit.josipnovak.projectrma.FirebaseAuth
import hr.ferit.josipnovak.projectrma.model.User
import kotlinx.coroutines.tasks.await

class EditPlayerViewModel(
    private val fbAuth: FirebaseAuth,
    private val db: FirebaseFirestore,
) : ViewModel() {

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