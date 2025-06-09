package hr.ferit.josipnovak.projectrma.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import hr.ferit.josipnovak.projectrma.FirebaseAuth
import hr.ferit.josipnovak.projectrma.model.Event
import hr.ferit.josipnovak.projectrma.model.Location
import kotlinx.coroutines.tasks.await

class EditEventViewModel (
    private val fbAuth: FirebaseAuth,
    private val db: FirebaseFirestore,
) : ViewModel(){

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

    fun updateEvent(event: Event) {
        val eventRef = db.collection("events").document(event.id)
        eventRef.set(event)
    }
}