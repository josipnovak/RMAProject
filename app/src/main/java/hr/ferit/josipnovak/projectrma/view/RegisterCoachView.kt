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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import hr.ferit.josipnovak.projectrma.FirebaseAuth
import hr.ferit.josipnovak.projectrma.R
import hr.ferit.josipnovak.projectrma.ui.theme.DarkBlue

@Composable
fun RegisterCoachView(modifier: Modifier = Modifier, navController: NavController, fbAuth: FirebaseAuth) {
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
                    onClick = { /*Do nothing*/ },
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
                    onClick = { navController.navigate("register_player")  },
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
                onClick = {
                    Log.d("RegisterCoach", "Registering user with email: $email and password: $password")
                    fbAuth.registerUser(email, password) { success, error ->
                        if (success) {
                            navController.navigate("login")
                        } else {
                            Log.e("RegisterError", error ?: "Unknown error")
                        }
                    }
                },
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