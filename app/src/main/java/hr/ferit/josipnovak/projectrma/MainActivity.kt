package hr.ferit.josipnovak.projectrma

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DataUsage
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.rpc.context.AttributeContext.Auth
import hr.ferit.josipnovak.projectrma.ui.theme.D9
import hr.ferit.josipnovak.projectrma.ui.theme.DarkBlue
import hr.ferit.josipnovak.projectrma.ui.theme.LightBlue
import hr.ferit.josipnovak.projectrma.ui.theme.ProjectRMATheme
import hr.ferit.josipnovak.projectrma.view.LoginScreenView
import hr.ferit.josipnovak.projectrma.view.MainScreenView
import hr.ferit.josipnovak.projectrma.view.PlayersView
import hr.ferit.josipnovak.projectrma.view.RegisterCoachView
import hr.ferit.josipnovak.projectrma.view.RegisterPlayerView
import java.util.Calendar
import hr.ferit.josipnovak.projectrma.view.StartScreenView
import hr.ferit.josipnovak.projectrma.view.UpcomingEventsView
import hr.ferit.josipnovak.projectrma.view.AccountDetailsView
import hr.ferit.josipnovak.projectrma.view.AddNewEventView
import hr.ferit.josipnovak.projectrma.view.AddNewPlayerView
import hr.ferit.josipnovak.projectrma.view.EditEventView
import hr.ferit.josipnovak.projectrma.view.EditPlayerView
import hr.ferit.josipnovak.projectrma.view.EventDetailsView
import hr.ferit.josipnovak.projectrma.view.PlayerDetailsView
import hr.ferit.josipnovak.projectrma.viewmodel.AuthViewModel
import hr.ferit.josipnovak.projectrma.viewmodel.EventsViewModel
import hr.ferit.josipnovak.projectrma.viewmodel.PlayersViewModel


class MainActivity : ComponentActivity() {
    @SuppressLint("ViewModelConstructorInComposable")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val fbAuth = FirebaseAuth
            val db = FirebaseFirestore.getInstance()
            val authViewModel = AuthViewModel(fbAuth, db, this)
            val playersViewModel = PlayersViewModel(fbAuth, db)
            val eventsViewModel = EventsViewModel(fbAuth, db)
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
                        composable("main") { MainScreenView(modifier = Modifier.padding(bottom = 25.dp), navController = navController) }
                        composable("upcoming_events") { UpcomingEventsView(modifier = Modifier.padding(bottom = 25.dp), navController = navController, eventsViewModel = eventsViewModel) }
                        composable("players") { PlayersView(modifier = Modifier.padding(bottom = 25.dp), navController = navController, playersViewModel = playersViewModel) }
                        composable("account_details") { AccountDetailsView(modifier = Modifier.padding(bottom = 25.dp), navController = navController, authViewModel = authViewModel) }
                        composable("player/{playerId}") { backStackEntry ->
                            val playerId = backStackEntry.arguments?.getString("playerId")
                            if (playerId != null) {
                                PlayerDetailsView(modifier = Modifier.padding(bottom = 25.dp), navController = navController, playersViewModel = playersViewModel, playerId = playerId)
                            }
                        }
                        composable("add_new_player") { AddNewPlayerView(modifier = Modifier.padding(bottom = 25.dp), navController = navController, playersViewModel = playersViewModel) }
                        composable("edit_player/{playerId}") { backStackEntry ->
                            val playerId = backStackEntry.arguments?.getString("playerId")
                            if (playerId != null) {
                                EditPlayerView(modifier = Modifier.padding(bottom = 25.dp), navController = navController, playersViewModel = playersViewModel, playerId = playerId)
                            }
                        }
                        composable("add_event") { AddNewEventView(modifier = Modifier.padding(bottom = 25.dp), navController = navController, eventsViewModel = eventsViewModel) }
                        composable("event/{eventId}") { backStackEntry ->
                            val eventId = backStackEntry.arguments?.getString("eventId")
                            if (eventId != null) {
                                EventDetailsView(modifier = Modifier.padding(bottom = 25.dp), navController = navController, eventsViewModel = eventsViewModel, eventId = eventId, )
                            }
                        }
                        composable("edit_event/{eventId}") { backStackEntry ->
                            val eventId = backStackEntry.arguments?.getString("eventId")
                            if (eventId != null) {
                                EditEventView(modifier = Modifier.padding(bottom = 25.dp), navController = navController, eventsViewModel = eventsViewModel, eventId = eventId)
                            }
                        }
                    }
                }
            }
        }
    }
}