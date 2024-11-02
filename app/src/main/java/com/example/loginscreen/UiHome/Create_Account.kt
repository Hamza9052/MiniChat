package com.example.loginscreen.UiHome

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.loginscreen.Event.UserEvent
import com.example.loginscreen.Event.user
import com.example.loginscreen.ViewModel.UserViewModel

@Composable
fun Create_Account(

    navController: NavController,
    ViewModel: UserViewModel,
    context: Context
){
    var showPassword by remember { mutableStateOf(value = false) }
    var user by remember {
        mutableStateOf(user())
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1.2f))
        Image(
            imageVector = Icons.Filled.AccountCircle,
            contentDescription ="Logo",

            alignment = Alignment.Center,
            modifier = Modifier
                .width(100.dp)
                .height(100.dp)
        )

        Spacer(modifier = Modifier.weight(0.3f))

        OutlinedTextField(
            value = user?.loginId?:"",
            onValueChange ={
                user = user.copy(
                    loginId = it
                )
            },label = {
                Text(text = "UserName")
            },
            modifier = Modifier.width(330.dp),
            singleLine = true,
            shape = RoundedCornerShape(15.dp),
            textStyle = TextStyle(color = Color.Black)
        )

        Spacer(modifier = Modifier.weight(0.3f))

        OutlinedTextField(
            value = user?.emial?:"",
            onValueChange ={
                user = user.copy(
                    emial = it
                )
            },label = {
                Text(text = "Email")
            },
            modifier = Modifier.width(330.dp),
            singleLine = true,
            shape = RoundedCornerShape(15.dp),
            textStyle = TextStyle(color = Color.Black)
        )

        Spacer(modifier = Modifier.weight(0.1f))

        OutlinedTextField(
            value = user?.password?: "",
            onValueChange ={
                user = user.copy(
                    password = it
                )

            },label = {
                Text(text = "Password")
            },
            modifier = Modifier.width(330.dp),
            singleLine = true,
            shape = RoundedCornerShape(15.dp),
            textStyle = TextStyle(color = Color.Black),
            visualTransformation = if (showPassword){
                VisualTransformation.None
            }else{
                PasswordVisualTransformation()
            },keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                if (showPassword) {
                    IconButton(onClick = { showPassword = false }) {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = "hide_password"
                        )
                    }
                } else {
                    IconButton(
                        onClick = { showPassword = true }) {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = "hide_password"
                        )
                    }
                }
            }
        )


        Spacer(modifier = Modifier.weight(0.1f))

        Button(
            onClick = {

                ViewModel.action(
                    event = UserEvent.CreateAccount(user = user){state ->
                        if (state){
                            navController.navigate(Screen.Login.route)
                        }

                    },context
                )
            },
            modifier = Modifier
                .width(330.dp)
                .height(40.dp),
            shape = RoundedCornerShape(30.dp)

        ) {
            Text(
                text = "Create Account",
                fontWeight = FontWeight.Bold
            )

        }



        Spacer(modifier = Modifier.weight(0.1f))




        Button(
            onClick = {
                navController.navigate(Screen.Login.route)
            },
            modifier = Modifier
                .width(330.dp)
                .height(40.dp),
            shape = RoundedCornerShape(30.dp),
            contentPadding = ButtonDefaults.ContentPadding

        ) {
            Text(
                text = "Log In",
                fontWeight = FontWeight.Bold
            )

        }

        Spacer(modifier = Modifier.weight(0.2f))

    }
}