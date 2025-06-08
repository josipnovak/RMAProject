package hr.ferit.josipnovak.projectrma.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import hr.ferit.josipnovak.projectrma.FirebaseAuth
import hr.ferit.josipnovak.projectrma.model.Event
import hr.ferit.josipnovak.projectrma.model.Location
import hr.ferit.josipnovak.projectrma.model.User
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.text.get
import kotlin.text.set

class EventsViewModel(
    private val fbAuth: FirebaseAuth,
    private val db: FirebaseFirestore,
) : ViewModel() {
    fun addNewEvent(event: Event) {
        getClubId { clubId ->
            if (clubId != "No Club ID") {
                val newEventRef = db.collection("events").document()
                event.id = newEventRef.id
                event.clubId = clubId
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

    fun getUpcomingEvents(
        onSuccess: (List<Event>) -> Unit
    ) {
        getClubId { clubId ->
            if (clubId != "No Club ID") {
                db.collection("events")
                    .whereEqualTo("clubId", clubId)
                    .get()
                    .addOnSuccessListener { documents ->
                        val events = documents.map { document ->
                            val locationMap =
                                document.get("location") as? Map<String, Any> ?: emptyMap()
                            val location = Location(
                                name = locationMap["name"] as? String ?: "",
                                latitude = (locationMap["latitude"] as? Number)?.toDouble() ?: 0.0,
                                longitude = (locationMap["longitude"] as? Number)?.toDouble() ?: 0.0
                            )
                            Event(
                                id = document.id,
                                clubId = document.getString("clubId") ?: "",
                                type = document.getString("type") ?: "",
                                name = document.getString("name") ?: "",
                                date = document.getString("date") ?: "",
                                time = document.getString("time") ?: "",
                                location = location
                            )
                        }
                        onSuccess(events)
                        Log.d("EventsViewModel", "Fetched events: $events")
                    }
                    .addOnFailureListener { e ->
                        Log.e("EventsViewModel", "Error fetching events: ${e.message}")
                    }
            } else {
                Log.d("EventsViewModel", "No Club ID found for the current user.")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getEventsForThisWeek(
        onSuccess: (Map<String, Int>) -> Unit
    ) {
        getClubId { clubId ->
            if (clubId != "No Club ID") {
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
                        Log.d("EventsViewModel", "Event counts for this week: $eventCounts")
                    }
                    .addOnFailureListener { e ->
                        Log.e("EventsViewModel", "Error fetching event counts: ${e.message}")
                    }
            } else {
                Log.d("EventsViewModel", "No Club ID found for the current user.")
            }
        }
    }


    suspend fun getEventById(eventId: String): Event {
        return try {
            val document = db.collection("events").document(eventId).get().await()
            if (document.exists()) {
                val locationMap =
                    document.get("location") as? Map<String, Any> ?: emptyMap()
                val location = Location(
                    name = locationMap["name"] as? String ?: "",
                    latitude = (locationMap["latitude"] as? Number)?.toDouble() ?: 0.0,
                    longitude = (locationMap["longitude"] as? Number)?.toDouble() ?: 0.0
                )
                Event(
                    id = document.id,
                    clubId = document.getString("clubId") ?: "",
                    type = document.getString("type") ?: "",
                    name = document.getString("name") ?: "",
                    date = document.getString("date") ?: "",
                    time = document.getString("time") ?: "",
                    location = location
                )
            } else {
                throw Exception("Event not found")
            }
        } catch (e: Exception) {
            throw Exception(e.message ?: "Error fetching event details")
        }
    }

    fun updatePlayer(event: Event) {
        val eventRef = db.collection("events").document(event.id)
        eventRef.set(event)
            .addOnSuccessListener {
                println("Event updated successfully")
            }
            .addOnFailureListener { e ->
                println("Error updating event: ${e.message}")
            }
    }

    fun isCoach(callback: (Boolean) -> Unit) {
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
                            callback(role == "coach")
                        }
                    }
            }
        }
        callback(false)
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

