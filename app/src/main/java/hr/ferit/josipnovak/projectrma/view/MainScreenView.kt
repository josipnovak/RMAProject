package hr.ferit.josipnovak.projectrma.view

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import hr.ferit.josipnovak.projectrma.ui.FooterHome
import hr.ferit.josipnovak.projectrma.R
import hr.ferit.josipnovak.projectrma.model.Club
import hr.ferit.josipnovak.projectrma.model.Event
import hr.ferit.josipnovak.projectrma.model.User
import hr.ferit.josipnovak.projectrma.ui.theme.DarkBlue
import hr.ferit.josipnovak.projectrma.ui.theme.LightBlue
import hr.ferit.josipnovak.projectrma.viewmodel.EventsViewModel
import hr.ferit.josipnovak.projectrma.viewmodel.PlayersViewModel
import kotlin.compareTo

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreenView(modifier: Modifier = Modifier, navController: NavController, playersViewModel: PlayersViewModel, eventsViewModel: EventsViewModel) {
    var user by remember { mutableStateOf<User?>(null) }
    var club by remember { mutableStateOf<Club?>(null) }
    var events by remember { mutableStateOf<Map<String, Int>>(emptyMap()) }
    var bestPlayers by remember { mutableStateOf<List<User>>(emptyList()) }
    LaunchedEffect(Unit) {
        playersViewModel.getUserAndClubData { fetchedUser, fetchedClub ->
            user = fetchedUser
            club = fetchedClub
        }
        playersViewModel.getBestPlayers { fetchedPlayers ->
            bestPlayers = fetchedPlayers
        }
        eventsViewModel.getEventsForThisWeek { fetchedEvents ->
            events = fetchedEvents
            Log.d("MainScreenView", "Fetched events: $events")
        }
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = DarkBlue)
    )
    {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.welcome, "${user?.name}"),
                color = Color.White,
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 40.dp, start = 50.dp, end = 50.dp)
                    .fillMaxWidth(),
            )
            Column(
                modifier = modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .width(300.dp)
                        .height(225.dp)
                        .padding(vertical = 8.dp),
                    onClick = {
                        navController.navigate("upcoming_events")
                    }
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxSize()
                    ) {
                        Text("Weekly summary", fontSize = 20.sp, color = Color.Black)
                        Spacer(Modifier.height(12.dp))
                        events.forEach { (eventType, count) ->
                            Text("$eventType: $count", fontSize = 16.sp, color = Color.Black)
                            Spacer(Modifier.height(8.dp))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .width(300.dp)
                        .height(300.dp)
                        .padding(vertical = 8.dp)
                ) {
                    Column(
                        modifier = modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        if(user?.role == "coach") {
                            Text(
                                text = stringResource(R.string.best_players),
                                color = Color.Black,
                                fontSize = 22.sp,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            bestPlayers.forEach { player ->
                                Row(
                                    modifier = modifier
                                        .width(250.dp)
                                        .height(40.dp)
                                        .background(color = LightBlue)
                                        .padding(horizontal = 10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = player.name, color = Color.Black, fontSize = 18.sp)
                                    Text(text = "${player.goals + player.assists}/${player.matches}", color = Color.Black, fontSize = 18.sp)
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                            }

                        }else{
                            Text(
                                text = stringResource(R.string.statistic),
                                color = Color.Black,
                                fontSize = 22.sp,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            Row(
                                modifier = modifier
                                    .width(250.dp)
                                    .height(40.dp)
                                    .background(color = LightBlue)
                                    .padding(horizontal = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "Ime prezime", color = Color.Black, fontSize = 18.sp)
                                Text(text = "25/5", color = Color.Black, fontSize = 18.sp)
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = modifier
                                    .width(250.dp)
                                    .height(40.dp)
                                    .background(color = LightBlue)
                                    .padding(horizontal = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "Ime prezime", color = Color.Black, fontSize = 18.sp)
                                Text(text = "25/5", color = Color.Black, fontSize = 18.sp)
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = modifier
                                    .width(250.dp)
                                    .height(40.dp)
                                    .background(color = LightBlue)
                                    .padding(horizontal = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "Ime prezime", color = Color.Black, fontSize = 18.sp)
                                Text(text = "25/5", color = Color.Black, fontSize = 18.sp)
                            }
                        }
                    }
                }
            }
            FooterHome(navController = navController)
        }
    }
}