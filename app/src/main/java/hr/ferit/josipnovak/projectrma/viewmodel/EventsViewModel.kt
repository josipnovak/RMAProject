package hr.ferit.josipnovak.projectrma.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location.distanceBetween
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import hr.ferit.josipnovak.projectrma.FirebaseAuth
import hr.ferit.josipnovak.projectrma.model.Event
import hr.ferit.josipnovak.projectrma.model.Location
import hr.ferit.josipnovak.projectrma.model.User
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.text.get
import kotlin.text.set

class EventsViewModel(
    private val fbAuth: FirebaseAuth,
    private val db: FirebaseFirestore,
) : ViewModel() {

    private val _message = MutableStateFlow(" ")
    val message: StateFlow<String> = _message

    fun addNewEvent(
        event: Event,
        onSuccess: (Boolean) -> Unit
    ) {
        if( event.name.isEmpty() || event.date.isEmpty() || event.time.isEmpty()) {
            _message.value = "Please fill in all fields."
            return
        }
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
                                onSuccess(true)
                            }
                            .addOnFailureListener { e ->
                                _message.value = "Error updating club with new event: ${e.message}"
                            }
                    }
                    .addOnFailureListener { e ->
                        _message.value = "Error adding new event: ${e.message}"
                    }
            } else {
                Log.d("AAA", "No Club ID found for the current user.")
            }
        }
    }

    fun deleteEvent(
        eventId: String,
        onSuccess: () -> Unit
    ){
        var clubId: String
        db.collection("events")
            .whereEqualTo("id", eventId)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val user = documents.first()
                    clubId = user.getString("clubId") ?: "No Club ID"
                    db.collection("clubs").document(clubId)
                        .update("events", FieldValue.arrayRemove(eventId))
                        .addOnSuccessListener {
                            db.collection("events").document(eventId)
                                .delete()
                                .addOnSuccessListener {
                                    onSuccess()
                                }
                                .addOnFailureListener { e ->
                                    println("Error deleting event: ${e.message}")
                                }
                        }
                        .addOnFailureListener { e ->
                            println("Error updating club with removed event: ${e.message}")
                        }
                } else {
                    println("event not found")
                }
            }
            .addOnFailureListener { e ->
                println("Error fetching event: ${e.message}")
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

    fun updateEvent(event: Event) {
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

    fun calculateDistanceToLocation(
        targetLocation: Location,
        context: Context,
        onResult: (Double?) -> Unit
    ) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        if (
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("EventsViewModel", "Location permissions not granted")
            onResult(null)
            return
        }

        fusedLocationClient.lastLocation.addOnCompleteListener { task ->
            if (task.isSuccessful && task.result != null) {
                val currentLoc = task.result
                val results = FloatArray(1)
                distanceBetween(
                    currentLoc.latitude,
                    currentLoc.longitude,
                    targetLocation.latitude,
                    targetLocation.longitude,
                    results
                )
                Log.d("EventsViewModel", "Distance to target location: ${results[0]} meters")
                onResult(results[0].toDouble() / 1000)
            } else {
                Log.d("EventsViewModel", "Failed to get last known location")
                onResult(null)
            }
        }
    }
}
