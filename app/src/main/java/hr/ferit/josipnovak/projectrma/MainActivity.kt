package hr.ferit.josipnovak.projectrma

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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hr.ferit.josipnovak.projectrma.ui.theme.D9
import hr.ferit.josipnovak.projectrma.ui.theme.DarkBlue
import hr.ferit.josipnovak.projectrma.ui.theme.LightBlue
import hr.ferit.josipnovak.projectrma.ui.theme.ProjectRMATheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjectRMATheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                }
            }
        }
    }
}


@Composable
fun StartScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBlue)
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .width(200.dp)
                    .height(75.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(15.dp)
            ) {
                Text(text = stringResource(id = R.string.login), color = Color.Black, fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.height(100.dp))
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .width(200.dp)
                    .height(75.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(15.dp)
            ) {
                Text(text = stringResource(id = R.string.register), color = Color.Black, fontSize = 20.sp)
            }
        }
    }
}

@Composable
fun LoginScreen(modifier: Modifier = Modifier) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBlue)
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = stringResource(id = R.string.email)) },
                modifier = Modifier
                    .width(300.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(15.dp)
            )

            Spacer(modifier = Modifier.height(50.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = stringResource(id = R.string.password)) },
                modifier = Modifier
                    .width(300.dp)
                    .height(60.dp)
                    .background(color = DarkBlue),
                shape = RoundedCornerShape(15.dp),
                visualTransformation = if(passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if(passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    val description = if(passwordVisible) "Hide password" else "Show password"
                    IconButton(
                        onClick = { passwordVisible = !passwordVisible },
                    ) {
                        Icon(imageVector = image, contentDescription = description, tint = Color.White)
                    }
                }
            )

            Spacer(modifier = Modifier.height(100.dp))

            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .width(200.dp)
                    .height(75.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(15.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.login),
                    color = Color.Black,
                    fontSize = 20.sp
                )
            }
        }
    }
}

@Composable
fun RegisterScreen(modifier: Modifier = Modifier) {
    var isCoach by remember { mutableStateOf(true) }

    if(isCoach) {
        RegisterCoach(modifier = modifier)
    } else {
        RegisterPlayer(modifier = modifier)
    }
}

@Composable
fun RegisterCoach(modifier: Modifier = Modifier) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var password1 by remember { mutableStateOf("") }
    var password1Visible by remember { mutableStateOf(false) }
    var teamName by remember { mutableStateOf("") }


    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBlue)
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .width(135.dp)
                        .height(45.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Text(text = stringResource(id = R.string.coach), color = Color.Black, fontSize = 20.sp)
                }

                Spacer(modifier = Modifier.width(30.dp))

                OutlinedButton(
                    onClick = { /*TODO*/  },
                    modifier = Modifier
                        .width(135.dp)
                        .height(45.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkBlue),
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Text(text = stringResource(id = R.string.player), color = Color.White, fontSize = 20.sp)
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
            
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(text = stringResource(id = R.string.name)) },
                modifier = Modifier
                    .width(300.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(15.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = stringResource(id = R.string.email)) },
                modifier = Modifier
                    .width(300.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(15.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = stringResource(id = R.string.password)) },
                modifier = Modifier
                    .width(300.dp)
                    .height(60.dp)
                    .background(color = DarkBlue),
                shape = RoundedCornerShape(15.dp),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible) {
                        Icons.Filled.Visibility
                    } else {
                        Icons.Filled.VisibilityOff
                    }
                    val description = if (passwordVisible) "Hide password" else "Show password"
                    IconButton(
                        onClick = { passwordVisible = !passwordVisible },
                    ) {
                        Icon(
                            imageVector = image,
                            contentDescription = description,
                            tint = Color.White
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(30.dp))

            OutlinedTextField(
                value = password1,
                onValueChange = { password1 = it },
                label = { Text(text = stringResource(id = R.string.confirm_password)) },
                modifier = Modifier
                    .width(300.dp)
                    .height(60.dp)
                    .background(color = DarkBlue),
                shape = RoundedCornerShape(15.dp),
                visualTransformation =
                    if (password1Visible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon =
                    {
                        val image =
                            if (password1Visible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        val description =
                            if (password1Visible) "Hide password" else "Show password"
                        IconButton(
                            onClick = { password1Visible = !password1Visible },
                        ) {
                            Icon(
                                imageVector = image,
                                contentDescription = description,
                                tint = Color.White
                            )
                        }
                    }
            )

            Spacer(modifier = Modifier.height(30.dp))

            OutlinedTextField(
                value = teamName,
                onValueChange = { teamName = it },
                label = { Text(text = stringResource(id = R.string.team_name)) },
                modifier = Modifier
                    .width(300.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(15.dp)
            )

            Spacer(modifier = Modifier.height(100.dp))

            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .width(200.dp)
                    .height(75.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(15.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.register),
                    color = Color.Black,
                    fontSize = 20.sp
                )
            }
        }
    }
}

@Composable
fun RegisterPlayer(modifier: Modifier = Modifier) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var password1 by remember { mutableStateOf("") }
    var password1Visible by remember { mutableStateOf(false) }
    var teamCode by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBlue)
    ){
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .width(135.dp)
                        .height(45.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkBlue),
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Text(text = stringResource(id = R.string.coach), color = Color.White, fontSize = 20.sp)
                }

                Spacer(modifier = Modifier.width(30.dp))

                Button(
                    onClick = { /*TODO*/  },
                    modifier = Modifier
                        .width(135.dp)
                        .height(45.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Text(text = stringResource(id = R.string.player), color = Color.Black, fontSize = 20.sp)
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(text = stringResource(id = R.string.name)) },
                modifier = Modifier
                    .width(300.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(15.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = stringResource(id = R.string.email)) },
                modifier = Modifier
                    .width(300.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(15.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = stringResource(id = R.string.password)) },
                modifier = Modifier
                    .width(300.dp)
                    .height(60.dp)
                    .background(color = DarkBlue),
                shape = RoundedCornerShape(15.dp),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible) {
                        Icons.Filled.Visibility
                    } else {
                        Icons.Filled.VisibilityOff
                    }
                    val description = if (passwordVisible) "Hide password" else "Show password"
                    IconButton(
                        onClick = { passwordVisible = !passwordVisible },
                    ) {
                        Icon(
                            imageVector = image,
                            contentDescription = description,
                            tint = Color.White
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(30.dp))

            OutlinedTextField(
                value = password1,
                onValueChange = { password1 = it },
                label = { Text(text = stringResource(id = R.string.confirm_password)) },
                modifier = Modifier
                    .width(300.dp)
                    .height(60.dp)
                    .background(color = DarkBlue),
                shape = RoundedCornerShape(15.dp),
                visualTransformation =
                    if (password1Visible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon =
                    {
                        val image =
                            if (password1Visible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        val description =
                            if (password1Visible) "Hide password" else "Show password"
                        IconButton(
                            onClick = { password1Visible = !password1Visible },
                        ) {
                            Icon(
                                imageVector = image,
                                contentDescription = description,
                                tint = Color.White
                            )
                        }
                    }
            )

            Spacer(modifier = Modifier.height(30.dp))

            OutlinedTextField(
                value = teamCode,
                onValueChange = { teamCode = it },
                label = { Text(text = stringResource(id = R.string.team_code)) },
                modifier = Modifier
                    .width(300.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(15.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(100.dp))

            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .width(200.dp)
                    .height(75.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(15.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.register),
                    color = Color.Black,
                    fontSize = 20.sp
                )
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
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
                text = stringResource(R.string.welcome, "Ime"),
                color = Color.White,
                fontSize = 30.sp,
                modifier = Modifier.padding(top = 40.dp)
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
                        .padding(vertical = 8.dp)
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
                        Text("2 trainings", fontSize = 16.sp)
                        Spacer(Modifier.height(8.dp))
                        Text("1 match", fontSize = 16.sp)
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
                        Text(
                            text = stringResource(R.string.best_players),
                            color = Color.Black,
                            fontSize = 22.sp
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
            FooterHome()
        }
    }
}

@Composable
fun UpcomingEvents(modifier: Modifier = Modifier) {
    var searchQuery by remember { mutableStateOf("") }
    var distanceToStadium by remember { mutableDoubleStateOf(0.0) }
    Box(
        modifier = modifier
            .background(color = DarkBlue)
    )
    {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.upcoming_events),
                color = Color.White,
                fontSize = 30.sp,
                modifier = Modifier.padding(top = 40.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            Row(
                modifier = Modifier
                    .width(350.dp)
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text(text = stringResource(id = R.string.search)) },
                    modifier = Modifier
                        .width(225.dp)
                        .height(60.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Black,
                        unfocusedIndicatorColor = Color.Black,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        cursorColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        unfocusedLabelColor = Color.Black,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    ),
                    shape = RoundedCornerShape(15.dp),
                    trailingIcon = {
                        IconButton(
                            onClick = { /*TODO*/ },
                        ) {
                            Icon(imageVector = Icons.Filled.Search, contentDescription = null, tint = Color.Black)
                        }
                    }
                )

                IconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .size(60.dp)
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
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(10) { index ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .padding(vertical = 8.dp),
                        onClick = { /*TODO*/ }
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Name of the event $index",
                                fontSize = 20.sp,
                                color = Color.Black,
                                modifier = Modifier
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "HH:MM",
                                fontSize = 16.sp,
                                color = Color.Gray,
                                modifier = Modifier
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Day, 27. May",
                                fontSize = 16.sp,
                                color = Color.Gray,
                                modifier = Modifier
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Stadium Name, $distanceToStadium km away",
                                fontSize = 16.sp,
                                color = Color.Gray,
                                modifier = Modifier
                            )
                        }
                    }
                }
            }
            FooterEvent()
        }
    }
}

@Composable
fun Players(modifier: Modifier = Modifier) {
    var searchQuery by remember { mutableStateOf("") }
    val players = mapOf(
        "Goalkeepers" to listOf("Player 1" to 0, "Player 2" to 0),
        "Defenders" to listOf("Player 3" to 7, "Player 4" to 5),
        "Midfielders" to listOf("Player 5" to 5, "Player 6" to 14),
        "Forwards" to listOf("Player 7" to 28, "Player 8" to 25)
    )
    Box(
        modifier = modifier
            .background(color = DarkBlue)
    )
    {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.players),
                color = Color.White,
                fontSize = 30.sp,
                modifier = Modifier.padding(top = 40.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            Row(
                modifier = Modifier
                    .width(350.dp)
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text(text = stringResource(id = R.string.search)) },
                    modifier = Modifier
                        .width(225.dp)
                        .height(60.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Black,
                        unfocusedIndicatorColor = Color.Black,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        cursorColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        unfocusedLabelColor = Color.Black,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    ),
                    shape = RoundedCornerShape(15.dp),
                    trailingIcon = {
                        IconButton(
                            onClick = { /*TODO*/ },
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = null,
                                tint = Color.Black
                            )
                        }
                    }
                )

                IconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .size(60.dp)
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
                players.forEach { (position, playerList) ->
                    item {
                        Text(
                            text = position,
                            fontSize = 24.sp,
                            color = Color.White,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    items(playerList.size) { it ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = LightBlue),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .padding(vertical = 8.dp),
                            onClick = { /*TODO*/ }
                        ) {
                            /*TODO mozda dodaj slike ispred svakog igraca*/
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Top
                            ) {
                                Text(
                                    text = playerList[it].first,
                                    fontSize = 18.sp,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Age: ${playerList[it].second}",
                                    fontSize = 14.sp,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
            FooterPlayers()
        }
    }
}

@Preview(showBackground = true, locale= "en")
@Composable
fun AccountDetails(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(color = DarkBlue)
    ) {
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
                    text = stringResource(R.string.account_details),
                    color = Color.White,
                    fontSize = 30.sp,
                    modifier = Modifier.padding(top = 40.dp)
                )
                Spacer(modifier = Modifier.height(40.dp))
                Text(
                    text = "Name",
                    color = Color.White,
                    fontSize = 30.sp,
                    modifier = Modifier.padding(top = 40.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Club name",
                    color = Color.White,
                    fontSize = 30.sp,
                    modifier = Modifier.padding(top = 40.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Coach/Player",
                    color = Color.White,
                    fontSize = 30.sp,
                    modifier = Modifier.padding(top = 40.dp)
                )
                Spacer(modifier = Modifier.height(30.dp))
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .width(125.dp)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Text(
                        text = "Logout",
                        color = Color.Black,
                        fontSize = 20.sp
                    )
                }
            }
            FooterUser()
        }
    }
}

@Composable
fun FooterHome(modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(0.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        listOf(
            Icons.Filled.Home,
            Icons.Filled.Notifications,
            Icons.Filled.CalendarToday,
            Icons.Filled.Groups,
            Icons.Filled.Settings
        ).forEachIndexed { index, icon ->
            val isSelected = index == 0
            IconButton(onClick = { /* TODO */ }) {
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
fun FooterEvent(modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(0.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        listOf(
            Icons.Filled.Home,
            Icons.Filled.Notifications,
            Icons.Filled.CalendarToday,
            Icons.Filled.Groups,
            Icons.Filled.Settings
        ).forEachIndexed { index, icon ->
            val isSelected = index == 1
            IconButton(onClick = { /* TODO */ }) {
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
fun FooterCalendar(modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(0.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        listOf(
            Icons.Filled.Home,
            Icons.Filled.Notifications,
            Icons.Filled.CalendarToday,
            Icons.Filled.Groups,
            Icons.Filled.Settings
        ).forEachIndexed { index, icon ->
            val isSelected = index == 2
            IconButton(onClick = { /* TODO */ }) {
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
fun FooterPlayers(modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(0.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        listOf(
            Icons.Filled.Home,
            Icons.Filled.Notifications,
            Icons.Filled.CalendarToday,
            Icons.Filled.Groups,
            Icons.Filled.Settings
        ).forEachIndexed { index, icon ->
            val isSelected = index == 3
            IconButton(onClick = { /* TODO */ }) {
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
fun FooterUser(modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(0.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        listOf(
            Icons.Filled.Home,
            Icons.Filled.Notifications,
            Icons.Filled.CalendarToday,
            Icons.Filled.Groups,
            Icons.Filled.Settings
        ).forEachIndexed { index, icon ->
            val isSelected = index == 4
            IconButton(onClick = { /* TODO */ }) {
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




