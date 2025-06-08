package hr.ferit.josipnovak.projectrma.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import hr.ferit.josipnovak.projectrma.ui.FooterPlayers
import hr.ferit.josipnovak.projectrma.R
import hr.ferit.josipnovak.projectrma.model.User
import hr.ferit.josipnovak.projectrma.ui.theme.DarkBlue
import hr.ferit.josipnovak.projectrma.ui.theme.LightBlue
import hr.ferit.josipnovak.projectrma.viewmodel.PlayersViewModel
import kotlin.text.matches

@Composable
fun PlayersView(modifier: Modifier = Modifier, navController: NavController, playersViewModel: PlayersViewModel) {
    var userId by remember { mutableStateOf("") }
    var clubId by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }
    var players by remember { mutableStateOf<Map<String, List<User>>>(emptyMap()) }

    var isFilterDialogVisible by remember { mutableStateOf(false) }
    var filterPosition by remember { mutableStateOf("") }
    var sortDescending by remember { mutableStateOf(false) }

    val filteredAndSortedPlayers = players
        .filter { (position, _) -> filterPosition.isEmpty() || position == filterPosition }
        .mapValues { (_, playerList) ->
            if (sortDescending) playerList.sortedByDescending { it.matches }
            else playerList.sortedBy { it.matches }
        }

    LaunchedEffect(Unit){
        playersViewModel.getUserDetails(
            onSuccess = { fetchedId, fetchedClubIdOrRole, fetchedRole ->
                if (fetchedRole == "player") {
                    userId = fetchedId
                } else if (fetchedRole == "coach") {
                    clubId = fetchedClubIdOrRole
                    playersViewModel.getPlayers(
                        clubId = clubId,
                        onSuccess = { groupedPlayers ->
                            players = groupedPlayers
                            Log.d("Players", "Fetched players: $players")
                        },
                        onError = { errorMessage ->
                            println("Error: $errorMessage")
                        }
                    )
                }
                role = fetchedRole
            },
            onError = { error ->

            }
        )
    }
    Box(
        modifier = modifier
            .background(color = DarkBlue )
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
            if(role == "coach") {
                Text(
                    text = stringResource(R.string.players),
                    color = Color.White,
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 40.dp, start = 50.dp, end = 50.dp)
                        .fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(40.dp))

                Row(
                    modifier = Modifier
                        .width(350.dp)
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { isFilterDialogVisible = true },
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
                    if (isFilterDialogVisible) {
                        Dialog(onDismissRequest = { isFilterDialogVisible = false }) {
                            Box(
                                modifier = Modifier
                                    .background(Color.White, shape = RoundedCornerShape(15.dp))
                                    .padding(16.dp)
                            ) {
                                Column {
                                    Text("Filter by Position", fontSize = 18.sp, color = Color.Black)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    listOf("Remove filter", "Goalkeeper", "Defender", "Midfielder", "Attacker",).forEach { position ->
                                        Text(
                                            text = position,
                                            modifier = Modifier
                                                .padding(8.dp)
                                                .clickable {
                                                    if (position == "Remove filter") {
                                                        filterPosition = ""
                                                    } else {
                                                        filterPosition = position
                                                    }
                                                    isFilterDialogVisible = false
                                                },
                                            color = if (filterPosition == position || (filterPosition == "" && position == "Remove filter")) LightBlue else Color.Black
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))

                                    Text("Sort by Matches", fontSize = 18.sp, color = Color.Black, textAlign = TextAlign.Center)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row {
                                        Text(
                                            text = "Ascending",
                                            modifier = Modifier
                                                .padding(8.dp)
                                                .clickable {
                                                    sortDescending = false
                                                    isFilterDialogVisible = false
                                                },
                                            color = if (!sortDescending) LightBlue else Color.Black,
                                        )
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Text(
                                            text = "Descending",
                                            modifier = Modifier
                                                .padding(8.dp)
                                                .clickable {
                                                    sortDescending = true
                                                    isFilterDialogVisible = false
                                                },
                                            color = if (sortDescending) LightBlue else Color.Black
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(15.dp))

                    IconButton(
                        onClick = { navController.navigate("add_new_player") },
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

                LazyColumn(
                    modifier = Modifier
                        .width(350.dp)
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    filteredAndSortedPlayers.forEach { (position, playerList) ->
                        item {
                            Text(
                                text = position,
                                fontSize = 24.sp,
                                color = Color.White,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        items(playerList.size) { index ->
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = LightBlue),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(80.dp)
                                    .padding(vertical = 8.dp),
                                onClick = { navController.navigate("player/${playerList[index].id}") }
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Top
                                ) {
                                    Text(
                                        text = playerList[index].name,
                                        fontSize = 18.sp,
                                        color = Color.White,
                                        modifier = Modifier.padding(
                                            horizontal = 16.dp,
                                            vertical = 4.dp
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Matches: ${playerList[index].matches}",
                                        fontSize = 14.sp,
                                        color = Color.White,
                                        modifier = Modifier.padding(
                                            horizontal = 16.dp,
                                            vertical = 4.dp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }else{
                /* TODO */
            }
            FooterPlayers(navController = navController)
        }
    }
}