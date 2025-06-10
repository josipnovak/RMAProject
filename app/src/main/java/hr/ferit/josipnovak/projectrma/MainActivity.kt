package hr.ferit.josipnovak.projectrma

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.firestore.FirebaseFirestore
import hr.ferit.josipnovak.projectrma.ui.theme.ProjectRMATheme
import hr.ferit.josipnovak.projectrma.view.LoginScreenView
import hr.ferit.josipnovak.projectrma.view.MainScreenView
import hr.ferit.josipnovak.projectrma.view.PlayersView
import hr.ferit.josipnovak.projectrma.view.RegisterCoachView
import hr.ferit.josipnovak.projectrma.view.RegisterPlayerView
import hr.ferit.josipnovak.projectrma.view.StartScreenView
import hr.ferit.josipnovak.projectrma.view.AccountDetailsView
import hr.ferit.josipnovak.projectrma.view.AddNewEventView
import hr.ferit.josipnovak.projectrma.view.AddNewPlayerView
import hr.ferit.josipnovak.projectrma.view.EditEventView
import hr.ferit.josipnovak.projectrma.view.EditPlayerView
import hr.ferit.josipnovak.projectrma.view.EventDetailsView
import hr.ferit.josipnovak.projectrma.view.EventsView
import hr.ferit.josipnovak.projectrma.view.PlayerDetailsView
import hr.ferit.josipnovak.projectrma.viewmodel.AccountDetailsViewModel
import hr.ferit.josipnovak.projectrma.viewmodel.AddEventViewModel
import hr.ferit.josipnovak.projectrma.viewmodel.AddPlayerViewModel
import hr.ferit.josipnovak.projectrma.viewmodel.AuthViewModel
import hr.ferit.josipnovak.projectrma.viewmodel.EditEventViewModel
import hr.ferit.josipnovak.projectrma.viewmodel.EditPlayerViewModel
import hr.ferit.josipnovak.projectrma.viewmodel.EventsViewModel
import hr.ferit.josipnovak.projectrma.viewmodel.MainScreenViewModel
import hr.ferit.josipnovak.projectrma.viewmodel.PlayersViewModel
import java.util.concurrent.TimeUnit

@RequiresApi(Build.VERSION_CODES.Q)
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ViewModelConstructorInComposable")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestLocationPermissions(this)
        requestNotificationPermission()
        createNotificationChannel(this)

        val workRequest = PeriodicWorkRequestBuilder<EventsWorker>(15, TimeUnit.MINUTES).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "EventsWorker",
            androidx.work.ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )

        val onStartRequest = OneTimeWorkRequestBuilder<EventsWorker>().build()
        WorkManager.getInstance(this).enqueue(onStartRequest)

        setContent {
            val navController = rememberNavController()
            val fbAuth = FirebaseAuth
            val db = FirebaseFirestore.getInstance()
            val authViewModel = AuthViewModel(fbAuth, db, this)
            val mainScreenViewModel = MainScreenViewModel(fbAuth, db)
            val playersViewModel1 = PlayersViewModel(fbAuth, db)
            val addPlayerViewModel = AddPlayerViewModel(fbAuth, db)
            val editPlayerViewModel = EditPlayerViewModel(db)
            val eventsViewModel1 = EventsViewModel(fbAuth, db)
            val addEventViewModel = AddEventViewModel(fbAuth, db)
            val editEventViewModel = EditEventViewModel(db)
            val accountDetailsViewModel = AccountDetailsViewModel(fbAuth, db)
            val startDestination = if (fbAuth.getCurrentUser() != null) "main" else "start"
            ProjectRMATheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(navController = navController, startDestination = startDestination,
                        modifier = Modifier.padding(bottom = 25.dp)
                    ) {
                        composable("start") { StartScreenView(modifier = Modifier.padding(bottom = 25.dp), navController = navController) }
                        composable("login") { LoginScreenView(modifier = Modifier.padding(bottom = 25.dp), navController = navController, authViewModel = authViewModel) }
                        composable("register_coach") { RegisterCoachView(modifier = Modifier.padding(bottom = 25.dp), navController = navController, authViewModel = authViewModel) }
                        composable("register_player") { RegisterPlayerView(modifier = Modifier.padding(bottom = 25.dp), navController = navController, authViewModel = authViewModel) }
                        composable("main") { MainScreenView(modifier = Modifier.padding(bottom = 25.dp), navController = navController, mainScreenViewModel = mainScreenViewModel) }
                        composable("upcoming_events") { EventsView(modifier = Modifier.padding(bottom = 25.dp), navController = navController, eventsViewModel = eventsViewModel1) }
                        composable("players") { PlayersView(modifier = Modifier.padding(bottom = 25.dp), navController = navController, playersViewModel = playersViewModel1) }
                        composable("account_details") { AccountDetailsView(modifier = Modifier.padding(bottom = 25.dp), navController = navController, accountDetailsViewModel = accountDetailsViewModel) }
                        composable("player/{playerId}") { backStackEntry ->
                            val playerId = backStackEntry.arguments?.getString("playerId")
                            if (playerId != null) {
                                PlayerDetailsView(modifier = Modifier.padding(bottom = 25.dp), navController = navController, playersViewModel = playersViewModel1, playerId = playerId)
                            }
                        }
                        composable("add_new_player") { AddNewPlayerView(modifier = Modifier.padding(bottom = 25.dp), navController = navController, addPlayerViewModel = addPlayerViewModel) }
                        composable("edit_player/{playerId}") { backStackEntry ->
                            val playerId = backStackEntry.arguments?.getString("playerId")
                            if (playerId != null) {
                                EditPlayerView(modifier = Modifier.padding(bottom = 25.dp), navController = navController, editPlayerViewModel = editPlayerViewModel, playerId = playerId)
                            }
                        }
                        composable("event/{eventId}") { backStackEntry ->
                            val eventId = backStackEntry.arguments?.getString("eventId")
                            if (eventId != null) {
                                EventDetailsView(modifier = Modifier.padding(bottom = 25.dp), navController = navController, eventsViewModel = eventsViewModel1, eventId = eventId, )
                            }
                        }
                        composable("add_event") { AddNewEventView(modifier = Modifier.padding(bottom = 25.dp), navController = navController, addEventViewModel = addEventViewModel) }
                        composable("edit_event/{eventId}") { backStackEntry ->
                            val eventId = backStackEntry.arguments?.getString("eventId")
                            if (eventId != null) {
                                EditEventView(modifier = Modifier.padding(bottom = 25.dp), navController = navController, editEventViewModel = editEventViewModel, eventId = eventId)
                            }
                        }
                    }
                }
            }
        }
    }
    private fun requestLocationPermissions(activity: Activity) {
        val permissionsToRequest = mutableListOf<String>()

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(activity, permissionsToRequest.toTypedArray(), 1001)
        }
    }
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun requestNotificationPermission() {
        val permissionsToRequest = mutableListOf<String>()
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACTIVITY_RECOGNITION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsToRequest.add(Manifest.permission.ACTIVITY_RECOGNITION)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), 1)
        }
    }

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "events_channel"
            val channelName = "Events Notifications"
            val channel = NotificationChannel(
                channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            Log.d("MainActivity", "Notification channel created: $channelId")
        }
    }
}