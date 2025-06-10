package hr.ferit.josipnovak.projectrma.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location.distanceBetween
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import hr.ferit.josipnovak.projectrma.FirebaseAuth
import hr.ferit.josipnovak.projectrma.model.Event
import hr.ferit.josipnovak.projectrma.model.Location
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EventsViewModel(
    private val fbAuth: FirebaseAuth,
    private val db: FirebaseFirestore,
) : ViewModel(){

    private val _message = MutableStateFlow(" ")
    val message: StateFlow<String> = _message

    fun getEvents(
        onSuccess: (List<Event>) -> Unit
    ){
        val todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        Log.d("EventsViewModel", "Today's date: $todayDate")
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
                                .whereLessThan("date", todayDate)
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
                        }
                    }
            }
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

    fun getCurrentLocation(
        context: Context,
        onSuccess: (Double, Double) -> Unit
    ){
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        if (
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("EventsViewModel", "Location permissions not granted")
            onSuccess(0.0, 0.0)
            return
        }

        fusedLocationClient.lastLocation.addOnCompleteListener { task ->
            if (task.isSuccessful && task.result != null) {
                val currentLoc = task.result
                onSuccess(currentLoc.latitude, currentLoc.longitude)
            }
        }
    }

    fun calculateDistanceToLocation(
        targetLocation: Location,
        context: Context,
        onSuccess: (Double?) -> Unit
    ) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        if (
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("EventsViewModel", "Location permissions not granted")
            onSuccess(null)
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
                onSuccess(results[0].toDouble() / 1000)
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
                        }
                }
            }
    }

}