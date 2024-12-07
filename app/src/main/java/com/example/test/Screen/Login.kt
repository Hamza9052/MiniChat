@file:Suppress("PreviewAnnotationInFunctionWithParameters")

package com.example.test.UiHome

import android.content.Context
import android.widget.Toast
import androidx.activity.ComponentActivity
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
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.test.AccountManager.LoginAction
import com.example.test.AccountManager.Result
import com.example.test.AccountManager.ResultIn
import com.example.test.AccountManager.accountManager
import com.example.test.Event.Loginstate
import com.example.test.Event.UserEvent

import com.example.test.Event.user
import com.example.test.R
import com.example.test.ViewModel.UserViewModel
import kotlinx.coroutines.launch

@Preview
@Composable
fun Login(
    navController: NavController,
    ViewModel: UserViewModel,
    context: Context,
    state : Loginstate,
    onAction:(LoginAction) -> Unit
){
    val scope = rememberCoroutineScope()
    val accountManager = remember {
        accountManager(navController.context,ViewModel,state)
    }
    var showPassword by remember { mutableStateOf(value = false) }
    var user by remember {
        mutableStateOf(user())
    }

    LaunchedEffect(key1 = true) {
        val result = accountManager.Logging()
        if (result is ResultIn.Success) {
            onAction(LoginAction.Logging(result))
        }


    }
    LaunchedEffect(key1 = state.loggedInUser) {
        if (state.loggedInUser != null){

                ViewModel.action(UserEvent.Login(state.email,state.password) { state ->
                    navController.navigate(Screen.Main.route)

                }, context)

        }
    }



   Column(
       modifier = Modifier
           .fillMaxSize()
           .background(colorResource(R.color.BurlyWood)),
       verticalArrangement = Arrangement.Center,
       horizontalAlignment = Alignment.CenterHorizontally
   ) {
       Spacer(modifier = Modifier.weight(1.2f))
       Box (contentAlignment = Alignment.Center){
           val composition by rememberLottieComposition(
               LottieCompositionSpec.RawRes(
                   R.raw.logo
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
           value = state.email ,
               onValueChange ={
                   user = user.copy(
                       emial = it
                   )
                   state.email = it
                   onAction(LoginAction.OnEmailChange(state.email))
               },label = {
               Text(
                   text = "Email",
                   color = colorResource(R.color.DarkSlateBlue),
                   fontWeight = FontWeight.Light
               )
           },
           colors = OutlinedTextFieldDefaults.colors(focusedBorderColor =  colorResource(R.color.DarkSlateBlue)),
           modifier = Modifier.width(330.dp),
           singleLine = true,
           shape = RoundedCornerShape(15.dp),
           textStyle = TextStyle(
               color =colorResource(R.color.Black),
               fontWeight = FontWeight.Bold
                       )
       )

       Spacer(modifier = Modifier.weight(0.1f))

       OutlinedTextField(
           value = state.password ,
           onValueChange ={
               user = user.copy(
                   password = it
               )
               state.password = it
               onAction(LoginAction.OnPasswordChange(state.password))
           },label = {
               Text(
                   text = "Password",
                   color = colorResource(R.color.DarkSlateBlue),
                   fontWeight = FontWeight.Light
               )
           },
           modifier = Modifier.width(330.dp),
           singleLine = true,
           colors = OutlinedTextFieldDefaults.colors(
               focusedBorderColor = colorResource(R.color.DarkSlateBlue)
           ),
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
                           imageVector = Icons.Default.VisibilityOff,
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


       Spacer(modifier = Modifier.weight(0.2f))
       Button(
           onClick = {
               if (
                   state.password.isEmpty() ||
                   state.email.isEmpty() ||
                   state.email.isBlank() ||
                   state.password.isBlank() ||
                   state.password.isEmpty() && state.email.isEmpty() ){
                   Toast.makeText(
                       navController.context,
                       "password or email is empty!",
                       Toast.LENGTH_SHORT).show()
                   return@Button

               }
               scope.launch {

                   val rustl = accountManager.login(
                       state.email,
                       state.password
                   )
                   if (rustl is Result.Failure){
                       ViewModel.action(UserEvent.Login(state.email,state.password) { state ->
                           navController.navigate(Screen.Main.route)

                       }, context)
                   }

                   onAction(
                       LoginAction.Login(rustl)
                   )
               }





           },
           colors = ButtonDefaults.buttonColors(colorResource(R.color.DarkSlateBlue)),
           modifier = Modifier
               .width(330.dp)
               .height(40.dp),
           shape = RoundedCornerShape(30.dp)

       ) {
           Text(
               text = "Log In",
               fontWeight = FontWeight.Bold,
               fontSize = 18.sp,
               color = colorResource(R.color.BurlyWood)
           )

       }



//       Spacer(modifier = Modifier.weight(0.1f))

//       Text(
//           text ="Forget Password?",
//           fontSize = 18.sp,
//           color = colorResource(R.color.DarkSlateBlue),
//           fontWeight = FontWeight.ExtraBold
//           )

       Spacer(modifier = Modifier.weight(0.3f))

       Button(
           onClick = {
               navController.navigate(Screen.register.route)
           },
           modifier = Modifier
               .width(330.dp)
               .height(40.dp),
           shape = RoundedCornerShape(30.dp),
           contentPadding = ButtonDefaults.ContentPadding,
           colors = ButtonDefaults.buttonColors( colorResource(R.color.DarkSlateBlue))

       ) {
           Text(
               text = "Create new account",
               fontWeight = FontWeight.Bold,
               fontSize = 18.sp,
               color = colorResource(R.color.BurlyWood)
           )

       }

       Spacer(modifier = Modifier.weight(0.4f))

   }

}