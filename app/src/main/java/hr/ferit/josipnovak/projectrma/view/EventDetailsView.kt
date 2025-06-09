package hr.ferit.josipnovak.projectrma.view


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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import hr.ferit.josipnovak.projectrma.R
import hr.ferit.josipnovak.projectrma.model.Event
import hr.ferit.josipnovak.projectrma.model.User
import hr.ferit.josipnovak.projectrma.ui.theme.DarkBlue
import hr.ferit.josipnovak.projectrma.ui.FooterEvent
import hr.ferit.josipnovak.projectrma.viewmodel.EventsViewModel


@Composable
fun EventDetailsView(modifier: Modifier = Modifier, navController: NavController, eventsViewModel: EventsViewModel, eventId: String) {
    var event by remember { mutableStateOf<Event?>(null) }
    var isCoach by remember { mutableStateOf(false) }
    var currentLocation by remember { mutableStateOf<Pair<Double, Double>?>(null) }
    val context = LocalContext.current
    var isMapVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        try {
            event = eventsViewModel.getEventById(eventId)
        } catch (e: Exception) {
            event = null
        }
        eventsViewModel.isCoach { coachStatus ->
            isCoach = coachStatus
        }
        eventsViewModel.getCurrentLocation(context) { lat, long ->
            currentLocation = Pair(lat, long)
        }
    }
    Box(
        modifier = modifier
            .background(color = DarkBlue)
    ) {
        IconButton(
            onClick = { navController.navigateUp() },
            modifier = Modifier
                .size(60.dp)
                .padding(top = 40.dp, start = 10.dp)
                .background(color = Color.White, shape = RoundedCornerShape(15.dp)),
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier
            )
        }
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.event_details),
                    color = Color.White,
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 40.dp, start = 50.dp, end = 50.dp)
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(40.dp))
                Text(
                    text = "${event?.type}: ${event?.name}",
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 40.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "${event?.time}",
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 40.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "${event?.date}",
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 40.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "${event?.location?.name}",
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 40.dp)
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
                    Dialog(
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
                                cameraPositionState = cameraPositionState
                            ) {
                                currentLocation?.let { (lat, long) ->
                                    val currentPosition = LatLng(lat, long)
                                    cameraPositionState.position = CameraPosition.fromLatLngZoom(currentPosition, 10f)

                                    Marker(
                                        state = MarkerState(position = currentPosition,),
                                        title = "Current Location",
                                    )
                                }
                                event?.location?.let { location ->
                                    val eventPosition = LatLng(location.latitude, location.longitude)
                                    val eventMarkerState = remember { MarkerState(position = eventPosition) }
                                    eventMarkerState.showInfoWindow()
                                    Marker(
                                        state = eventMarkerState,
                                        title = "Event location"
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
                Spacer(modifier = Modifier.height(20.dp))
                if(isCoach) {
                    Spacer(modifier = Modifier.height(40.dp))
                    Row() {
                        Button(
                            onClick = {
                                eventsViewModel.deleteEvent(event?.id ?: ""){
                                    navController.navigate("upcoming_events")
                                }
                            },
                            modifier = Modifier
                                .width(125.dp)
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            shape = RoundedCornerShape(15.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.delete),
                                color = Color.Black,
                                fontSize = 20.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(20.dp))
                        Button(
                            onClick = { navController.navigate("edit_event/${event?.id}") },
                            modifier = Modifier
                                .width(125.dp)
                                .height(50.dp),
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
                }
            }
            FooterEvent(navController = navController)
        }
    }
}