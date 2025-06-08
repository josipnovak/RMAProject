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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
fun PlayerDetailsView(modifier: Modifier = Modifier, navController: NavController, playersViewModel: PlayersViewModel, playerId: String) {
    var player by remember { mutableStateOf<User?>(null) }
    LaunchedEffect(playerId) {
        try {
            player = playersViewModel.getPlayerById(playerId)
        } catch (e: Exception) {
            player = null
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
                    text = stringResource(R.string.player_details),
                    color = Color.White,
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 40.dp, start = 50.dp, end = 50.dp)
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(40.dp))
                Text(
                    text = "${player?.name}",
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 40.dp)
                )
                Text(
                    text = "${player?.position}",
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 40.dp)
                )
                Text(
                    text = "Matches: ${player?.matches}",
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 40.dp)
                )
                Text(
                    text = "Goals: ${player?.goals}",
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 40.dp)
                )
                Text(
                    text = "Assists: ${player?.assists}",
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 40.dp)
                )
                Text(
                    text = "Trainings: ${player?.trainings}",
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 40.dp)
                )
                Spacer(modifier = Modifier.height(40.dp))
                Row() {
                    Button(
                        onClick = { /*TODO*/ },
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
                        onClick = { navController.navigate("edit_player/${player?.id}") },
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
            FooterPlayers(navController = navController)
        }
    }
}