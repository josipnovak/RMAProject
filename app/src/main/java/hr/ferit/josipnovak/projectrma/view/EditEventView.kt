package hr.ferit.josipnovak.projectrma.view

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
import hr.ferit.josipnovak.projectrma.R
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
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.runtime.LaunchedEffect
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
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.rpc.context.AttributeContext.Auth
import hr.ferit.josipnovak.projectrma.model.Event
import hr.ferit.josipnovak.projectrma.model.Location
import hr.ferit.josipnovak.projectrma.ui.FooterEvent
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
import hr.ferit.josipnovak.projectrma.view.AccountDetailsView
import hr.ferit.josipnovak.projectrma.viewmodel.AuthViewModel
import hr.ferit.josipnovak.projectrma.viewmodel.EditEventViewModel


@Composable
fun EditEventView(modifier: Modifier = Modifier, navController: NavController, editEventViewModel: EditEventViewModel, eventId: String) {
    var event by remember { mutableStateOf<Event?>(null) }
    var eventType by remember { mutableStateOf("") }
    var eventName by remember { mutableStateOf("") }
    var eventDate by remember { mutableStateOf("") }
    var eventTime by remember { mutableStateOf("") }
    var isMapVisible by remember { mutableStateOf(false) }
    var locationName by remember { mutableStateOf("") }
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }
    LaunchedEffect(eventId) {
        try {
            event = editEventViewModel.getEventById(eventId)
            event?.let {
                eventType = it.type
                eventName = it.name
                eventDate = it.date
                eventTime = it.time
                locationName = it.location.name
                selectedLocation = it.location.let { LatLng(it.latitude, it.longitude) }
            }
        } catch (e: Exception) {
            event = null
        }
    }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            eventDate = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            eventTime = String.format("%02d:%02d", hourOfDay, minute)
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    )

    Box(
        modifier = modifier
            .background(color = DarkBlue)
    ) {
        IconButton(
            onClick = { navController.navigateUp() },
            modifier = Modifier
                .size(60.dp)
                .padding(top = 40.dp, start = 20.dp)
                .background(color = Color.White, shape = RoundedCornerShape(15.dp)),
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
            )
        }
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(R.string.add_new_event),
                    color = Color.White,
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 40.dp, start = 50.dp, end = 50.dp)
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(40.dp))

                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { eventType = "Training" },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (eventType == "Training") Color.White else DarkBlue
                        ),
                        shape = RoundedCornerShape(15.dp)
                    ) {
                        Text(text = "Training", color = if (eventType == "Training") Color.Black else Color.White)
                    }

                    Button(
                        onClick = { eventType = "Match" },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (eventType == "Match") Color.White else DarkBlue
                        ),
                        shape = RoundedCornerShape(15.dp)
                    ) {
                        Text(text = "Match", color = if (eventType == "Match") Color.Black else Color.White)
                    }

                    Button(
                        onClick = { eventType = "Other" },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (eventType == "Other") Color.White else DarkBlue,

                            ),
                        shape = RoundedCornerShape(15.dp)
                    ) {
                        Text(text = "Other", color = if (eventType == "Other") Color.Black else Color.White)
                    }
                }

                OutlinedTextField(
                    value = eventName,
                    onValueChange = { eventName = it },
                    label = { Text(text = stringResource(id = R.string.event_name)) },
                    modifier = Modifier
                        .width(300.dp)
                        .height(60.dp),
                    shape = RoundedCornerShape(15.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { datePickerDialog.show() },
                    modifier = Modifier
                        .width(200.dp)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Text(
                        text = if (eventDate.isEmpty()) stringResource(R.string.pick_date) else eventDate,
                        color = Color.Black,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { timePickerDialog.show() },
                    modifier = Modifier
                        .width(200.dp)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Text(
                        text = if (eventTime.isEmpty()) stringResource(R.string.pick_time) else eventTime,
                        color = Color.Black,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = locationName,
                    onValueChange = { locationName = it },
                    label = { Text(text = stringResource(id = R.string.location_name)) },
                    modifier = Modifier
                        .width(300.dp)
                        .height(60.dp),
                    shape = RoundedCornerShape(15.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { isMapVisible = !isMapVisible },
                    modifier = Modifier
                        .width(200.dp)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Text(
                        text = if (isMapVisible) "Close map" else "Open map",
                        color = Color.Black,
                        fontSize = 16.sp
                    )
                }

                if (isMapVisible) {
                    androidx.compose.ui.window.Dialog(
                        onDismissRequest = { isMapVisible = false }
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White)
                        ) {
                            val cameraPositionState = rememberCameraPositionState {
                                position = CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 2f)
                            }

                            GoogleMap(
                                modifier = Modifier.fillMaxSize(),
                                cameraPositionState = cameraPositionState,
                                onMapClick = { latLng ->
                                    selectedLocation = latLng
                                }
                            ) {
                                selectedLocation?.let {
                                    Marker(
                                        state = MarkerState(position = it),
                                        title = "Selected Location"
                                    )
                                }
                            }

                            IconButton(
                                onClick = { isMapVisible = false },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(16.dp)
                                    .size(20.dp)
                                    .background(DarkBlue, shape = CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close Map",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    onClick = {
                        val location = Location(
                            name = locationName,
                            latitude = selectedLocation?.latitude ?: 0.0,
                            longitude = selectedLocation?.longitude ?: 0.0
                        )
                        val event = Event(
                            id = event?.id ?: "",
                            clubId = event?.clubId ?: "",
                            type = eventType,
                            name = eventName,
                            date = eventDate,
                            time = eventTime,
                            createdAt = Timestamp.now(),
                            location = location
                        )
                        Log.d("AddNewEventView", "Editing event: $event")
                        editEventViewModel.updateEvent(event)
                        Log.d("AddNewEventView", "Event edited: $event")
                        navController.navigate("upcoming_events")
                    },
                    modifier = Modifier
                        .width(150.dp)
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Text(
                        text = stringResource(R.string.edit),
                        color = Color.Black,
                        fontSize = 20.sp
                    )
                }
            }
            FooterEvent(navController = navController)
        }
    }
}