package hr.ferit.josipnovak.projectrma.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import hr.ferit.josipnovak.projectrma.R
import hr.ferit.josipnovak.projectrma.ui.theme.DarkBlue

@Composable
fun StartScreenView(modifier: Modifier = Modifier, navController: NavController) {
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
                onClick = { navController.navigate("login") },
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
                onClick = { navController.navigate("register_coach") },
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