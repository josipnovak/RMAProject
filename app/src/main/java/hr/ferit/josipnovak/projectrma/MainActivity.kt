package hr.ferit.josipnovak.projectrma

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val fbAuth = FirebaseAuth
            val startDestination = if (fbAuth.getCurrentUser() != null) "main" else "start"
            ProjectRMATheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(navController = navController, startDestination = startDestination,
                        modifier = Modifier.padding(bottom = 25.dp)
                    ) {
                        composable("start") { StartScreenView(modifier = Modifier.padding(bottom = 25.dp), navController = navController) }
                        composable("login") { LoginScreenView(modifier = Modifier.padding(bottom = 25.dp), navController = navController, fbAuth = fbAuth) }
                        composable("register_coach") { RegisterCoachView(modifier = Modifier.padding(bottom = 25.dp), navController = navController, fbAuth = fbAuth) }
                        composable("register_player") { RegisterPlayerView(modifier = Modifier.padding(bottom = 25.dp), navController = navController, fbAuth = fbAuth) }
                        composable("main") { MainScreenView(modifier = Modifier.padding(bottom = 25.dp), navController = navController) }
                        composable("upcoming_events") { UpcomingEventsView(modifier = Modifier.padding(bottom = 25.dp), navController = navController) }
                        composable("players") { PlayersView(modifier = Modifier.padding(bottom = 25.dp), navController = navController) }
                        composable("account_details") { AccountDetailsView(modifier = Modifier.padding(bottom = 25.dp), navController = navController, fbAuth = fbAuth) }
                    }
                }
            }
        }
    }
}

@Composable
fun FooterHome(modifier: Modifier = Modifier, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(0.dp),
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
            .padding(0.dp),
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
            .padding(0.dp),
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
            .padding(0.dp),
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

//@Composable
//fun EventDetails(modifier: Modifier = Modifier) {
//    Box(
//        modifier = modifier
//            .background(color = DarkBlue)
//    ) {
//        IconButton(
//            onClick = { /*TODO*/ },
//            modifier = Modifier
//                .size(60.dp)
//                .padding(top = 40.dp, start = 10.dp)
//                .background(color = Color.White, shape = RoundedCornerShape(15.dp)),
//        ) {
//            Icon(
//                imageVector = Icons.Filled.ArrowBack,
//                contentDescription = null,
//                tint = Color.Black,
//                modifier = Modifier
//            )
//        }
//        Column(
//            modifier = modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.SpaceBetween,
//            horizontalAlignment = Alignment.CenterHorizontally
//        )
//        {
//            Column(
//                verticalArrangement = Arrangement.Top,
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Text(
//                    text = stringResource(R.string.event_details),
//                    color = Color.White,
//                    fontSize = 30.sp,
//                    textAlign = TextAlign.Center,
//                    modifier = Modifier
//                        .padding(top = 40.dp, start = 50.dp, end = 50.dp)
//                        .fillMaxWidth()
//                )
//                Spacer(modifier = Modifier.height(40.dp))
//                Text(
//                    text = "Name",
//                    color = Color.White,
//                    fontSize = 30.sp,
//                    modifier = Modifier.padding(top = 40.dp)
//                )
//                Spacer(modifier = Modifier.height(20.dp))
//                Text(
//                    text = "Time",
//                    color = Color.White,
//                    fontSize = 30.sp,
//                    modifier = Modifier.padding(top = 40.dp)
//                )
//                Spacer(modifier = Modifier.height(20.dp))
//                Text(
//                    text = "Date",
//                    color = Color.White,
//                    fontSize = 30.sp,
//                    modifier = Modifier.padding(top = 40.dp)
//                )
//                Spacer(modifier = Modifier.height(20.dp))
//                Text(
//                    text = "Location",
//                    color = Color.White,
//                    fontSize = 30.sp,
//                    modifier = Modifier.padding(top = 40.dp)
//                )
//                Spacer(modifier = Modifier.height(40.dp))
//                Row() {
//                    Button(
//                        onClick = { /*TODO*/ },
//                        modifier = Modifier
//                            .width(125.dp)
//                            .height(50.dp),
//                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
//                        shape = RoundedCornerShape(15.dp)
//                    ) {
//                        Text(
//                            text = stringResource(R.string.delete),
//                            color = Color.Black,
//                            fontSize = 20.sp
//                        )
//                    }
//                    Spacer(modifier = Modifier.width(20.dp))
//                    Button(
//                        onClick = { /*TODO*/ },
//                        modifier = Modifier
//                            .width(125.dp)
//                            .height(50.dp),
//                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
//                        shape = RoundedCornerShape(15.dp)
//                    ) {
//                        Text(
//                            text = stringResource(R.string.edit),
//                            color = Color.Black,
//                            fontSize = 20.sp
//                        )
//                    }
//                }
//            }
//            FooterEvent()
//        }
//    }
//}
//
//@Composable
//fun AddNewEvent(modifier: Modifier = Modifier) {
//    var eventName by remember { mutableStateOf("") }
//    var eventDate by remember { mutableStateOf("") }
//    var eventTime by remember { mutableStateOf("") }
//    var eventLocation by remember { mutableStateOf("") }
//
//    val context = LocalContext.current
//    val calendar = Calendar.getInstance()
//
//    val datePickerDialog = DatePickerDialog(
//        context,
//        { _, year, month, dayOfMonth ->
//            eventDate = "$dayOfMonth/${month + 1}/$year"
//        },
//        calendar.get(Calendar.YEAR),
//        calendar.get(Calendar.MONTH),
//        calendar.get(Calendar.DAY_OF_MONTH)
//    )
//
//    val timePickerDialog = TimePickerDialog(
//        context,
//        { _, hourOfDay, minute ->
//            eventTime = String.format("%02d:%02d", hourOfDay, minute)
//        },
//        calendar.get(Calendar.HOUR_OF_DAY),
//        calendar.get(Calendar.MINUTE),
//        true
//    )
//
//    Box(
//        modifier = modifier
//            .background(color = DarkBlue)
//    ) {
//        IconButton(
//            onClick = { /*TODO*/ },
//            modifier = Modifier
//                .size(60.dp)
//                .padding(top = 40.dp, start = 20.dp)
//                .background(color = Color.White, shape = RoundedCornerShape(15.dp)),
//        ) {
//            Icon(
//                imageVector = Icons.Filled.ArrowBack,
//                contentDescription = null,
//                tint = Color.Black,
//                modifier = Modifier.size(24.dp)
//            )
//        }
//        Column(
//            modifier = modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.SpaceBetween,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Column(
//                verticalArrangement = Arrangement.Top,
//                horizontalAlignment = Alignment.CenterHorizontally,
//            ) {
//                Text(
//                    text = stringResource(R.string.add_new_event),
//                    color = Color.White,
//                    fontSize = 30.sp,
//                    textAlign = TextAlign.Center,
//                    modifier = Modifier
//                        .padding(top = 40.dp, start = 50.dp, end = 50.dp)
//                        .fillMaxWidth()
//                )
//                Spacer(modifier = Modifier.height(40.dp))
//
//                OutlinedTextField(
//                    value = eventName,
//                    onValueChange = { eventName = it },
//                    label = { Text(text = stringResource(id = R.string.event_name)) },
//                    modifier = Modifier
//                        .width(300.dp)
//                        .height(60.dp),
//                    shape = RoundedCornerShape(15.dp)
//                )
//
//                Spacer(modifier = Modifier.height(20.dp))
//
//                Button(
//                    onClick = { datePickerDialog.show() },
//                    modifier = Modifier
//                        .width(200.dp)
//                        .height(50.dp),
//                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
//                    shape = RoundedCornerShape(15.dp)
//                ) {
//                    Text(
//                        text = if (eventDate.isEmpty()) stringResource(R.string.pick_date) else eventDate,
//                        color = Color.Black,
//                        fontSize = 16.sp
//                    )
//                }
//
//                Spacer(modifier = Modifier.height(20.dp))
//
//                Button(
//                    onClick = { timePickerDialog.show() },
//                    modifier = Modifier
//                        .width(200.dp)
//                        .height(50.dp),
//                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
//                    shape = RoundedCornerShape(15.dp)
//                ) {
//                    Text(
//                        text = if (eventTime.isEmpty()) stringResource(R.string.pick_time) else eventTime,
//                        color = Color.Black,
//                        fontSize = 16.sp
//                    )
//                }
//
//                Spacer(modifier = Modifier.height(20.dp))
//
//                /*TODO LOCATION PICKER*/
//
//                Spacer(modifier = Modifier.height(40.dp))
//
//                Button(
//                    onClick = { /*TODO*/ },
//                    modifier = Modifier
//                        .width(150.dp)
//                        .height(40.dp),
//                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
//                    shape = RoundedCornerShape(15.dp)
//                ) {
//                    Text(
//                        text = stringResource(R.string.add),
//                        color = Color.Black,
//                        fontSize = 20.sp
//                    )
//                }
//            }
//            FooterEvent()
//        }
//    }
//}
//
//@Composable
//fun EditEvent(modifier: Modifier = Modifier) {
//    var eventName by remember { mutableStateOf("") }
//    var eventDate by remember { mutableStateOf("") }
//    var eventTime by remember { mutableStateOf("") }
//    var eventLocation by remember { mutableStateOf("") }
//
//    val context = LocalContext.current
//    val calendar = Calendar.getInstance()
//
//    val datePickerDialog = DatePickerDialog(
//        context,
//        { _, year, month, dayOfMonth ->
//            eventDate = "$dayOfMonth/${month + 1}/$year"
//        },
//        calendar.get(Calendar.YEAR),
//        calendar.get(Calendar.MONTH),
//        calendar.get(Calendar.DAY_OF_MONTH)
//    )
//
//    val timePickerDialog = TimePickerDialog(
//        context,
//        { _, hourOfDay, minute ->
//            eventTime = String.format("%02d:%02d", hourOfDay, minute)
//        },
//        calendar.get(Calendar.HOUR_OF_DAY),
//        calendar.get(Calendar.MINUTE),
//        true
//    )
//
//    Box(
//        modifier = modifier
//            .background(color = DarkBlue)
//    ) {
//        IconButton(
//            onClick = { /*TODO*/ },
//            modifier = Modifier
//                .size(60.dp)
//                .padding(top = 40.dp, start = 20.dp)
//                .background(color = Color.White, shape = RoundedCornerShape(15.dp)),
//        ) {
//            Icon(
//                imageVector = Icons.Filled.ArrowBack,
//                contentDescription = null,
//                tint = Color.Black,
//                modifier = Modifier.size(24.dp)
//            )
//        }
//        Column(
//            modifier = modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.SpaceBetween,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Column(
//                verticalArrangement = Arrangement.Top,
//                horizontalAlignment = Alignment.CenterHorizontally,
//            ) {
//                Text(
//                    text = stringResource(R.string.add_new_event),
//                    color = Color.White,
//                    fontSize = 30.sp,
//                    textAlign = TextAlign.Center,
//                    modifier = Modifier
//                        .padding(top = 40.dp, start = 50.dp, end = 50.dp)
//                        .fillMaxWidth()
//                )
//                Spacer(modifier = Modifier.height(40.dp))
//
//                OutlinedTextField(
//                    value = eventName,
//                    onValueChange = { eventName = it },
//                    label = { Text(text = stringResource(id = R.string.event_name)) },
//                    modifier = Modifier
//                        .width(300.dp)
//                        .height(60.dp),
//                    shape = RoundedCornerShape(15.dp)
//                )
//
//                Spacer(modifier = Modifier.height(20.dp))
//
//                Button(
//                    onClick = { datePickerDialog.show() },
//                    modifier = Modifier
//                        .width(200.dp)
//                        .height(50.dp),
//                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
//                    shape = RoundedCornerShape(15.dp)
//                ) {
//                    Text(
//                        text = if (eventDate.isEmpty()) stringResource(R.string.pick_date) else eventDate,
//                        color = Color.Black,
//                        fontSize = 16.sp
//                    )
//                }
//
//                Spacer(modifier = Modifier.height(20.dp))
//
//                Button(
//                    onClick = { timePickerDialog.show() },
//                    modifier = Modifier
//                        .width(200.dp)
//                        .height(50.dp),
//                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
//                    shape = RoundedCornerShape(15.dp)
//                ) {
//                    Text(
//                        text = if (eventTime.isEmpty()) stringResource(R.string.pick_time) else eventTime,
//                        color = Color.Black,
//                        fontSize = 16.sp
//                    )
//                }
//
//                Spacer(modifier = Modifier.height(20.dp))
//
//                /*TODO LOCATION PICKER*/
//
//                Spacer(modifier = Modifier.height(40.dp))
//
//                Button(
//                    onClick = { /*TODO*/ },
//                    modifier = Modifier
//                        .width(150.dp)
//                        .height(40.dp),
//                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
//                    shape = RoundedCornerShape(15.dp)
//                ) {
//                    Text(
//                        text = stringResource(R.string.add),
//                        color = Color.Black,
//                        fontSize = 20.sp
//                    )
//                }
//            }
//            FooterEvent()
//        }
//    }
//}
//
//@Composable
//fun PlayerDetails(modifier: Modifier = Modifier) {
//    Box(
//        modifier = modifier
//            .background(color = DarkBlue)
//    ) {
//        IconButton(
//            onClick = { /*TODO*/ },
//            modifier = Modifier
//                .size(60.dp)
//                .padding(top = 40.dp, start = 10.dp)
//                .background(color = Color.White, shape = RoundedCornerShape(15.dp)),
//        ) {
//            Icon(
//                imageVector = Icons.Filled.ArrowBack,
//                contentDescription = null,
//                tint = Color.Black,
//                modifier = Modifier
//            )
//        }
//        Column(
//            modifier = modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.SpaceBetween,
//            horizontalAlignment = Alignment.CenterHorizontally
//        )
//        {
//            Column(
//                verticalArrangement = Arrangement.Top,
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Text(
//                    text = stringResource(R.string.player_details),
//                    color = Color.White,
//                    fontSize = 30.sp,
//                    textAlign = TextAlign.Center,
//                    modifier = Modifier
//                        .padding(top = 40.dp, start = 50.dp, end = 50.dp)
//                        .fillMaxWidth()
//                )
//                Spacer(modifier = Modifier.height(40.dp))
//                Text(
//                    text = "Name",
//                    color = Color.White,
//                    fontSize = 30.sp,
//                    modifier = Modifier.padding(top = 40.dp)
//                )
//                Text(
//                    text = "Position",
//                    color = Color.White,
//                    fontSize = 30.sp,
//                    modifier = Modifier.padding(top = 40.dp)
//                )
//                Text(
//                    text = "Age",
//                    color = Color.White,
//                    fontSize = 30.sp,
//                    modifier = Modifier.padding(top = 40.dp)
//                )
//                Text(
//                    text = "Matches Played",
//                    color = Color.White,
//                    fontSize = 30.sp,
//                    modifier = Modifier.padding(top = 40.dp)
//                )
//                Text(
//                    text = "Goals",
//                    color = Color.White,
//                    fontSize = 30.sp,
//                    modifier = Modifier.padding(top = 40.dp)
//                )
//                Text(
//                    text = "Trainings",
//                    color = Color.White,
//                    fontSize = 30.sp,
//                    modifier = Modifier.padding(top = 40.dp)
//                )
//                Spacer(modifier = Modifier.height(40.dp))
//                Row() {
//                    Button(
//                        onClick = { /*TODO*/ },
//                        modifier = Modifier
//                            .width(125.dp)
//                            .height(50.dp),
//                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
//                        shape = RoundedCornerShape(15.dp)
//                    ) {
//                        Text(
//                            text = stringResource(R.string.delete),
//                            color = Color.Black,
//                            fontSize = 20.sp
//                        )
//                    }
//                    Spacer(modifier = Modifier.width(20.dp))
//                    Button(
//                        onClick = { /*TODO*/ },
//                        modifier = Modifier
//                            .width(125.dp)
//                            .height(50.dp),
//                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
//                        shape = RoundedCornerShape(15.dp)
//                    ) {
//                        Text(
//                            text = stringResource(R.string.edit),
//                            color = Color.Black,
//                            fontSize = 20.sp
//                        )
//                    }
//                }
//            }
//            FooterPlayers()
//        }
//    }
//}
//
//@Composable
//fun EditPlayer(modifier: Modifier = Modifier) {
//    var name by remember { mutableStateOf("") }
//    var position by remember { mutableStateOf("") }
//    var age by remember { mutableStateOf("") }
//    var matchesPlayed by remember { mutableStateOf("") }
//    var goals by remember { mutableStateOf("") }
//    var trainings by remember { mutableStateOf("") }
//    Box(
//        modifier = modifier
//            .background(color = DarkBlue)
//    ) {
//        IconButton(
//            onClick = { /*TODO*/ },
//            modifier = Modifier
//                .size(60.dp)
//                .padding(top = 40.dp, start = 10.dp)
//                .background(color = Color.White, shape = RoundedCornerShape(15.dp)),
//        ) {
//            Icon(
//                imageVector = Icons.Filled.ArrowBack,
//                contentDescription = null,
//                tint = Color.Black,
//                modifier = Modifier
//            )
//        }
//        Column(
//            modifier = modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.SpaceBetween,
//            horizontalAlignment = Alignment.CenterHorizontally
//        )
//        {
//            Column(
//                verticalArrangement = Arrangement.Top,
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Text(
//                    text = stringResource(R.string.edit_player),
//                    color = Color.White,
//                    fontSize = 30.sp,
//                    textAlign = TextAlign.Center,
//                    modifier = Modifier
//                        .padding(top = 40.dp, start = 50.dp, end = 50.dp)
//                        .fillMaxWidth()
//                )
//                Spacer(modifier = Modifier.height(40.dp))
//                OutlinedTextField(
//                    value = name,
//                    onValueChange = { name = it },
//                    label = { Text(text = stringResource(id = R.string.player_name)) },
//                    modifier = Modifier
//                        .width(300.dp)
//                        .height(60.dp),
//                    shape = RoundedCornerShape(15.dp)
//                )
//                Spacer(modifier = Modifier.height(10.dp))
//                OutlinedTextField(
//                    value = position,
//                    onValueChange = { position = it },
//                    label = { Text(text = stringResource(id = R.string.position)) },
//                    modifier = Modifier
//                        .width(300.dp)
//                        .height(60.dp),
//                    shape = RoundedCornerShape(15.dp)
//                )
//                Spacer(modifier = Modifier.height(10.dp))
//                OutlinedTextField(
//                    value = age,
//                    onValueChange = { age = it },
//                    label = { Text(text = stringResource(id = R.string.age)) },
//                    modifier = Modifier
//                        .width(300.dp)
//                        .height(60.dp),
//                    shape = RoundedCornerShape(15.dp)
//                )
//                Spacer(modifier = Modifier.height(10.dp))
//                OutlinedTextField(
//                    value = matchesPlayed,
//                    onValueChange = { matchesPlayed = it },
//                    label = { Text(text = stringResource(id = R.string.matches)) },
//                    modifier = Modifier
//                        .width(300.dp)
//                        .height(60.dp),
//                    shape = RoundedCornerShape(15.dp)
//                )
//                Spacer(modifier = Modifier.height(10.dp))
//                OutlinedTextField(
//                    value = goals,
//                    onValueChange = { goals = it },
//                    label = { Text(text = stringResource(id = R.string.goals)) },
//                    modifier = Modifier
//                        .width(300.dp)
//                        .height(60.dp),
//                    shape = RoundedCornerShape(15.dp)
//                )
//                Spacer(modifier = Modifier.height(10.dp))
//                OutlinedTextField(
//                    value = trainings,
//                    onValueChange = { trainings = it },
//                    label = { Text(text = stringResource(id = R.string.trainings)) },
//                    modifier = Modifier
//                        .width(300.dp)
//                        .height(60.dp),
//                    shape = RoundedCornerShape(15.dp)
//                )
//                Spacer(modifier = Modifier.height(40.dp))
//                Button(
//                    onClick = { /*TODO*/ },
//                    modifier = Modifier
//                        .width(125.dp)
//                        .height(50.dp),
//                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
//                    shape = RoundedCornerShape(15.dp)
//                ) {
//                    Text(
//                        text = stringResource(R.string.edit),
//                        color = Color.Black,
//                        fontSize = 20.sp
//                    )
//                }
//            }
//            FooterPlayers()
//        }
//    }
//}
//
//@Composable
//fun AddNewPlayer(modifier: Modifier = Modifier) {
//    var name by remember { mutableStateOf("") }
//    var position by remember { mutableStateOf("") }
//    var age by remember { mutableStateOf("") }
//    var matchesPlayed by remember { mutableStateOf("") }
//    var goals by remember { mutableStateOf("") }
//    var trainings by remember { mutableStateOf("") }
//    Box(
//        modifier = modifier
//            .background(color = DarkBlue)
//    ) {
//        IconButton(
//            onClick = { /*TODO*/ },
//            modifier = Modifier
//                .size(60.dp)
//                .padding(top = 40.dp, start = 10.dp)
//                .background(color = Color.White, shape = RoundedCornerShape(15.dp)),
//        ) {
//            Icon(
//                imageVector = Icons.Filled.ArrowBack,
//                contentDescription = null,
//                tint = Color.Black,
//                modifier = Modifier
//            )
//        }
//        Column(
//            modifier = modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.SpaceBetween,
//            horizontalAlignment = Alignment.CenterHorizontally
//        )
//        {
//            Column(
//                verticalArrangement = Arrangement.Top,
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Text(
//                    text = stringResource(R.string.add_new_player),
//                    color = Color.White,
//                    fontSize = 30.sp,
//                    textAlign = TextAlign.Center,
//                    modifier = Modifier
//                        .padding(top = 40.dp, start = 50.dp, end = 50.dp)
//                        .fillMaxWidth()
//                )
//                Spacer(modifier = Modifier.height(40.dp))
//                OutlinedTextField(
//                    value = name,
//                    onValueChange = { name = it },
//                    label = { Text(text = stringResource(id = R.string.player_name)) },
//                    modifier = Modifier
//                        .width(300.dp)
//                        .height(60.dp),
//                    shape = RoundedCornerShape(15.dp)
//                )
//                Spacer(modifier = Modifier.height(10.dp))
//                OutlinedTextField(
//                    value = position,
//                    onValueChange = { position = it },
//                    label = { Text(text = stringResource(id = R.string.position)) },
//                    modifier = Modifier
//                        .width(300.dp)
//                        .height(60.dp),
//                    shape = RoundedCornerShape(15.dp)
//                )
//                Spacer(modifier = Modifier.height(10.dp))
//                OutlinedTextField(
//                    value = age,
//                    onValueChange = { age = it },
//                    label = { Text(text = stringResource(id = R.string.age)) },
//                    modifier = Modifier
//                        .width(300.dp)
//                        .height(60.dp),
//                    shape = RoundedCornerShape(15.dp)
//                )
//                Spacer(modifier = Modifier.height(10.dp))
//                OutlinedTextField(
//                    value = matchesPlayed,
//                    onValueChange = { matchesPlayed = it },
//                    label = { Text(text = stringResource(id = R.string.matches)) },
//                    modifier = Modifier
//                        .width(300.dp)
//                        .height(60.dp),
//                    shape = RoundedCornerShape(15.dp)
//                )
//                Spacer(modifier = Modifier.height(10.dp))
//                OutlinedTextField(
//                    value = goals,
//                    onValueChange = { goals = it },
//                    label = { Text(text = stringResource(id = R.string.goals)) },
//                    modifier = Modifier
//                        .width(300.dp)
//                        .height(60.dp),
//                    shape = RoundedCornerShape(15.dp)
//                )
//                Spacer(modifier = Modifier.height(10.dp))
//                OutlinedTextField(
//                    value = trainings,
//                    onValueChange = { trainings = it },
//                    label = { Text(text = stringResource(id = R.string.trainings)) },
//                    modifier = Modifier
//                        .width(300.dp)
//                        .height(60.dp),
//                    shape = RoundedCornerShape(15.dp)
//                )
//                Spacer(modifier = Modifier.height(40.dp))
//                Button(
//                    onClick = { /*TODO*/ },
//                    modifier = Modifier
//                        .width(125.dp)
//                        .height(50.dp),
//                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
//                    shape = RoundedCornerShape(15.dp)
//                ) {
//                    Text(
//                        text = stringResource(R.string.add),
//                        color = Color.Black,
//                        fontSize = 20.sp
//                    )
//                }
//            }
//            FooterPlayers()
//        }
//    }
//}