package hr.ferit.josipnovak.projectrma.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import hr.ferit.josipnovak.projectrma.ui.theme.DarkBlue

@Composable
fun FooterHome(modifier: Modifier = Modifier, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 0.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        listOf(
            Icons.Filled.Home to "main",
            Icons.Filled.Notifications to "upcoming_events",
            Icons.Filled.Groups to "players",
            Icons.Filled.Settings to "account_details"
        ).forEachIndexed { index, (icon, route) ->
            val isSelected = index == 0
            IconButton(onClick = { navController.navigate(route) }) {
                Box(
                    modifier = if (isSelected) Modifier
                        .size(50.dp)
                        .background(DarkBlue, shape = RectangleShape) else Modifier
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (isSelected) Color.White else Color.Black,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

        }
    }
}

@Composable
fun FooterEvent(modifier: Modifier = Modifier, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 0.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        listOf(
            Icons.Filled.Home to "main",
            Icons.Filled.Notifications to "upcoming_events",
            Icons.Filled.Groups to "players",
            Icons.Filled.Settings to "account_details"
        ).forEachIndexed { index, (icon, route) ->
            val isSelected = index == 1
            IconButton(onClick = { navController.navigate(route) }) {
                Box(
                    modifier = if (isSelected) Modifier
                        .size(50.dp)
                        .background(DarkBlue, shape = RectangleShape) else Modifier
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (isSelected) Color.White else Color.Black,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

        }
    }
}

@Composable
fun FooterPlayers(modifier: Modifier = Modifier, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 0.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        listOf(
            Icons.Filled.Home to "main",
            Icons.Filled.Notifications to "upcoming_events",
            Icons.Filled.Groups to "players",
            Icons.Filled.Settings to "account_details"
        ).forEachIndexed { index, (icon, route) ->
            val isSelected = index == 2
            IconButton(onClick = { navController.navigate(route) }) {
                Box(
                    modifier = if (isSelected) Modifier
                        .size(50.dp)
                        .background(DarkBlue, shape = RectangleShape) else Modifier
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (isSelected) Color.White else Color.Black,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

        }
    }
}

@Composable
fun FooterUser(modifier: Modifier = Modifier, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 0.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        listOf(
            Icons.Filled.Home to "main",
            Icons.Filled.Notifications to "upcoming_events",
            Icons.Filled.Groups to "players",
            Icons.Filled.Settings to "account_details"
        ).forEachIndexed { index, (icon, route) ->
            val isSelected = index == 3
            IconButton(onClick = { navController.navigate(route) }) {
                Box(
                    modifier = if (isSelected) Modifier
                        .size(50.dp)
                        .background(DarkBlue, shape = RectangleShape) else Modifier
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (isSelected) Color.White else Color.Black,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

        }
    }
}