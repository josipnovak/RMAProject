package hr.ferit.josipnovak.projectrma

import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query.Direction.DESCENDING
import hr.ferit.josipnovak.projectrma.model.Event
import kotlinx.coroutines.tasks.await

class EventsWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val fbAuth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        val currentUser = fbAuth.currentUser ?: return Result.success()
        val email = currentUser.email ?: return Result.success()
        val userDocs = db.collection("users")
            .whereEqualTo("email", email)
            .get()
            .await()
        if (userDocs.isEmpty) return Result.success()
        val user = userDocs.first()
        val clubId = user.getString("clubId") ?: return Result.success()
        val eventDocs = db.collection("events")
            .whereEqualTo("clubId", clubId)
            .orderBy("createdAt", DESCENDING)
            .limit(1)
            .get()
            .await()
        val events = eventDocs.map { document ->
            Event(
                id = document.getString("id") ?: "",
                name = document.getString("name") ?: "",
                type = document.getString("type") ?: ""
            )
        }

        val latestEvent = events.firstOrNull()

        if (latestEvent != null) {
            showEventNotification(applicationContext, latestEvent)
        }
        return Result.success()
    }

    fun showEventNotification(context: Context, event: Event) {
        val channelId = "events_channel"
        val title = "New Event: ${event.type}"
        val message = "Type: ${event.name}"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(event.id.hashCode(), notification)
    }
}