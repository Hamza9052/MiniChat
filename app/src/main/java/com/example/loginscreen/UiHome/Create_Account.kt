package com.hamza.test.UiHome

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.hamza.test.Event.UserEvent
import com.hamza.test.Event.user
import com.hamza.test.R
import com.hamza.test.ViewModel.UserViewModel

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
            .fillMaxSize()
            .background(colorResource(R.color.BurlyWood)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.9f))
        Box (contentAlignment = Alignment.Center){
            val composition by rememberLottieComposition(
                LottieCompositionSpec.RawRes(
                    R.raw.create
                )
            )
            val preloaderProgress by animateLottieCompositionAsState(
                composition,
                iterations = LottieConstants.IterateForever,
                isPlaying = true
            )
            LottieAnimation(
                composition = composition,
                progress = preloaderProgress,
                modifier = Modifier.size(200.dp).align(Alignment.Center)
            )

        }

        Spacer(modifier = Modifier.weight(0.3f))

        OutlinedTextField(
            value = user.loginId ,
            onValueChange ={
                user = user.copy(
                    loginId = it
                )
            },label = {
                Text(
                    text = "UserName",
                    color = colorResource(R.color.DarkSlateBlue),
                    fontWeight = FontWeight.Light
                )
            },
            modifier = Modifier.width(330.dp),
            singleLine = true,
            shape = RoundedCornerShape(15.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorResource(R.color.DarkSlateBlue)
            ),
            textStyle = TextStyle(
                color = colorResource(R.color.DarkSlateBlue),
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.weight(0.1f))

        OutlinedTextField(
            value = user.emial ,
            onValueChange ={
                user = user.copy(
                    emial = it
                )
            },label = {
                Text(
                    text = "Email",
                    color = colorResource(R.color.DarkSlateBlue),
                    fontWeight = FontWeight.Light
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorResource(R.color.DarkSlateBlue)
            ),
            modifier = Modifier.width(330.dp),
            singleLine = true,
            shape = RoundedCornerShape(15.dp),
            textStyle = TextStyle(
                color = colorResource(R.color.Black),
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.weight(0.1f))

        OutlinedTextField(
            value = user.password ,
            onValueChange ={
                user = user.copy(
                    password = it
                )

            },label = {
                Text(
                    text = "Password",
                    color = colorResource(R.color.DarkSlateBlue),
                    fontWeight = FontWeight.Light
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorResource(R.color.DarkSlateBlue)
            ),
            modifier = Modifier.width(330.dp),
            singleLine = true,
            shape = RoundedCornerShape(15.dp),
            textStyle = TextStyle(
                color = colorResource(R.color.Black),
                fontWeight = FontWeight.Bold
            ),
            visualTransformation = if (showPassword){
                VisualTransformation.None
            }else{
                PasswordVisualTransformation()
            },keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                if (showPassword) {
                    IconButton(onClick = { showPassword = false }) {
                        Icon(
                            imageVector = Icons.Filled.VisibilityOff,
                            contentDescription = "hide_password",
                            tint = colorResource(R.color.HotPink)
                        )
                    }
                } else {
                    IconButton(
                        onClick = { showPassword = true }) {
                        Icon(
                            imageVector = Icons.Filled.Visibility,
                            contentDescription = "hide_password",
                            tint = colorResource(R.color.DarkSlateBlue)
                        )
                    }
                }
            }
        )


        Spacer(modifier = Modifier.weight(0.1f))

        Button(
            onClick = {
                if (
                    user.password.isEmpty() ||
                    user.emial.isEmpty() ||
                    user.password.isEmpty() && user.emial.isEmpty() ){
                    Toast.makeText(
                        navController.context,
                        "password or email is empty!",
                        Toast.LENGTH_SHORT).show().toString()

                }else {

                    ViewModel.action(
                        event = UserEvent.CreateAccount(user = user) { state ->
                            if (state) {
                                navController.navigate(Screen.Login.route)
                            }

                        }, context
                    )
                }
            },
            modifier = Modifier
                .width(330.dp)
                .height(40.dp),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors( colorResource(R.color.DarkSlateBlue))

        ) {
            Text(
                text = "Create Account",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = colorResource(R.color.BurlyWood)
            )

        }



        Spacer(modifier = Modifier.weight(0.3f))




        Button(
            onClick = {
                navController.navigate(Screen.Login.route)
            },
            modifier = Modifier
                .width(330.dp)
                .height(40.dp),
            shape = RoundedCornerShape(30.dp),
            contentPadding = ButtonDefaults.ContentPadding,
            colors = ButtonDefaults.buttonColors( colorResource(R.color.DarkSlateBlue))

        ) {
            Text(
                text = "Log In",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = colorResource(R.color.BurlyWood)
            )

        }

        Spacer(modifier = Modifier.weight(0.7f))

    }
}