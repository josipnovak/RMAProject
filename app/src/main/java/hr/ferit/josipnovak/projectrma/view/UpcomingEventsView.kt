package hr.ferit.josipnovak.projectrma.view

import android.util.Log
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import hr.ferit.josipnovak.projectrma.ui.FooterEvent
import hr.ferit.josipnovak.projectrma.R
import hr.ferit.josipnovak.projectrma.model.Event
import hr.ferit.josipnovak.projectrma.ui.theme.DarkBlue
import hr.ferit.josipnovak.projectrma.viewmodel.EventsViewModel
import kotlin.math.roundToInt

@Composable
fun UpcomingEventsView(modifier: Modifier = Modifier, navController: NavController, eventsViewModel: EventsViewModel) {
    var events by remember { mutableStateOf<List<Event>>(emptyList()) }
    var isCoach by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        eventsViewModel.getUpcomingEvents { fetchedEvents ->
            events = fetchedEvents
            Log.d("UpcomingEventsView", "Fetched events: $events")
        }
        eventsViewModel.isCoach { coachStatus ->
            isCoach = coachStatus
        }
    }
    Box(
        modifier = modifier
            .background(color = DarkBlue)
    )
    {
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
                modifier = Modifier
            )
        }
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.upcoming_events),
                color = Color.White,
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 40.dp, start = 50.dp, end = 50.dp)
                    .fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(40.dp))
            if(isCoach) {
                Row(
                    modifier = Modifier
                        .width(350.dp)
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            .size(40.dp)
                            .background(color = Color.White, shape = RoundedCornerShape(15.dp)),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.FilterAlt,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier
                        )
                    }
                    Spacer(modifier = Modifier.width(15.dp))
                    IconButton(
                        onClick = { navController.navigate("add_event") },
                        modifier = Modifier
                            .size(40.dp)
                            .background(color = Color.White, shape = RoundedCornerShape(15.dp)),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier
                        )
                    }

                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            LazyColumn(
                modifier = Modifier
                    .width(350.dp)
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(events.size) { index ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .padding(vertical = 8.dp),
                        onClick = { navController.navigate("event/${events[index].id}") }
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "${events[index].type}: ${events[index].name}",
                                fontSize = 20.sp,
                                color = Color.Black,
                                modifier = Modifier
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = events[index].time,
                                fontSize = 16.sp,
                                color = Color.Gray,
                                modifier = Modifier
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = events[index].date,
                                fontSize = 16.sp,
                                color = Color.Gray,
                                modifier = Modifier
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "${events[index].location.name}, ${events[index].location.latitude.roundToInt()}, ${events[index].location.longitude.roundToInt()}",
                                fontSize = 16.sp,
                                color = Color.Gray,
                                modifier = Modifier
                            )
                        }
                    }
                }
            }
            FooterEvent(navController = navController)
        }
    }
}