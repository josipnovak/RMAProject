package hr.ferit.josipnovak.projectrma.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import hr.ferit.josipnovak.projectrma.FirebaseAuth
import hr.ferit.josipnovak.projectrma.model.Club
import hr.ferit.josipnovak.projectrma.model.User
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainScreenViewModel(
    private val fbAuth: FirebaseAuth,
    private val db: FirebaseFirestore,
) : ViewModel() {
    fun getUserAndClubData(callback: (User, Club) -> Unit) {
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
                            db.collection("clubs").document(clubId)
                                .get()
                                .addOnSuccessListener { clubDocument ->
                                    val club = clubDocument.toObject(Club::class.java) ?: Club()
                                    club.id = clubDocument.id
                                    val userObj = user.toObject(User::class.java) ?: User()
                                    userObj.id = user.id
                                    callback(userObj, club)
                                }
                                .addOnFailureListener {
                                    callback(User(), Club())
                                }
                            callback(User(), Club())
                        } else {
                            callback(User(), Club())
                        }
                    }
                    .addOnFailureListener {
                        callback(User(), Club())
                    }
            } else {
                callback(User(), Club())
            }
        } else {
            callback(User(), Club())
        }
    }
    fun getBestPlayers(
        onSuccess: (List<User>) -> Unit
    ){
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
                            db.collection("clubs").document(clubId)
                                .get()
                                .addOnSuccessListener { document ->
                                    val playerIds = document.get("players") as? List<String> ?: emptyList()
                                    val tasks = playerIds.map { playerId ->
                                        db.collection("users").document(playerId).get()
                                    }

                                    Tasks.whenAllSuccess<DocumentSnapshot>(tasks)
                                        .addOnSuccessListener { snapshots ->
                                            val players = snapshots.mapNotNull { snapshot ->
                                                snapshot.toObject(User::class.java)?.copy(id = snapshot.id)
                                            }
                                            onSuccess(players.sortedByDescending { it.goals + it.assists }.take(3))
                                        }
                                }
                        }
                    }
            }
        }
        onSuccess(emptyList())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getEventsForThisWeek(
        onSuccess: (Map<String, Int>) -> Unit
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
                            val clubId = user.getString("clubId") ?: "No Club ID"
                            db.collection("events")
                                .whereEqualTo("clubId", clubId)
                                .get()
                                .addOnSuccessListener { documents ->
                                    val formatter = DateTimeFormatter.ofPattern("d/M/yyyy")
                                    val today = LocalDate.now()
                                    val nextWeek = today.plusDays(7)

                                    val eventCounts = documents.mapNotNull { document ->
                                        val eventDate = document.getString("date")?.let {
                                            try {
                                                LocalDate.parse(it, formatter)
                                            } catch (e: Exception) {
                                                null
                                            }
                                        }
                                        if (eventDate != null && (eventDate.isEqual(today) || (eventDate.isAfter(
                                                today
                                            ) && eventDate.isBefore(nextWeek)))
                                        ) {
                                            document.getString("type")
                                        } else {
                                            null
                                        }
                                    }.groupingBy { it }.eachCount()

                                    onSuccess(eventCounts)
                                }
                        }
                    }
            }
        }
    }
}