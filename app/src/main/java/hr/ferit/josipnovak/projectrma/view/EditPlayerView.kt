package hr.ferit.josipnovak.projectrma.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import hr.ferit.josipnovak.projectrma.R
import hr.ferit.josipnovak.projectrma.model.User
import hr.ferit.josipnovak.projectrma.ui.FooterPlayers
import hr.ferit.josipnovak.projectrma.ui.theme.DarkBlue
import hr.ferit.josipnovak.projectrma.viewmodel.PlayersViewModel

@Composable
fun EditPlayerView(modifier: Modifier = Modifier, navController: NavController, playersViewModel: PlayersViewModel, playerId: String) {
    var player by remember { mutableStateOf<User?>(null) }
    var name by remember { mutableStateOf("") }
    var position by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var assists by remember { mutableStateOf("") }
    var matches by remember { mutableStateOf("") }
    var goals by remember { mutableStateOf("") }
    var trainings by remember { mutableStateOf("") }
    var clubId by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }

    LaunchedEffect(playerId) {
        try {
            player = playersViewModel.getPlayerById(playerId)
            player?.let {
                name = it.name
                position = it.position
                assists = it.assists.toString()
                matches = it.matches.toString()
                goals = it.goals.toString()
                trainings = it.trainings.toString()
                clubId = it.clubId
                email = it.email
                role = it.role
            }
        } catch (e: Exception) {
            player = null
        }
    }
    Box(
        modifier = modifier
            .background(color = DarkBlue)
    ) {
        IconButton(
            onClick = { /*TODO*/ },
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
                    text = stringResource(R.string.edit_player),
                    color = Color.White,
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 40.dp, start = 50.dp, end = 50.dp)
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(40.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(text = stringResource(id = R.string.player_name)) },
                    modifier = Modifier
                        .width(300.dp)
                        .height(60.dp),
                    shape = RoundedCornerShape(15.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Box {
                    Button(
                        onClick = { expanded = true },
                        modifier = Modifier
                            .width(300.dp)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(15.dp)
                    ) {
                        Text(
                            text = if (position.isNotEmpty()) position else "Select position",
                            color = Color.Black,
                            fontSize = 16.sp
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        listOf("Goalkeeper", "Defender", "Midfielder", "Attacker").forEach { role ->
                            DropdownMenuItem(
                                text = { Text(text = role) },
                                onClick = {
                                    position = role
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = matches,
                    onValueChange = { matches = it },
                    label = { Text(text = stringResource(id = R.string.matches)) },
                    modifier = Modifier
                        .width(300.dp)
                        .height(60.dp),
                    shape = RoundedCornerShape(15.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = goals,
                    onValueChange = { goals = it },
                    label = { Text(text = stringResource(id = R.string.goals)) },
                    modifier = Modifier
                        .width(300.dp)
                        .height(60.dp),
                    shape = RoundedCornerShape(15.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = assists,
                    onValueChange = { assists = it },
                    label = { Text(text = stringResource(id = R.string.assists)) },
                    modifier = Modifier
                        .width(300.dp)
                        .height(60.dp),
                    shape = RoundedCornerShape(15.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = trainings,
                    onValueChange = { trainings = it },
                    label = { Text(text = stringResource(id = R.string.trainings)) },
                    modifier = Modifier
                        .width(300.dp)
                        .height(60.dp),
                    shape = RoundedCornerShape(15.dp)
                )
                Spacer(modifier = Modifier.height(40.dp))
                Button(
                    onClick = {
                        val updatedPlayer = User(
                            id = playerId,
                            name = name,
                            position = position,
                            matches = matches.toIntOrNull() ?: 0,
                            goals = goals.toIntOrNull() ?: 0,
                            assists = assists.toIntOrNull() ?: 0,
                            trainings = trainings.toIntOrNull() ?: 0,
                            clubId = clubId,
                            email = email,
                            role = role
                        )
                        playersViewModel.updatePlayer(updatedPlayer)
                        navController.navigate("player/${playerId}")
                    },
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
            FooterPlayers(navController = navController)
        }
    }
}