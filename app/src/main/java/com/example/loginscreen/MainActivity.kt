package com.example.loginscreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.loginscreen.UiHome.Create_Account
import com.example.loginscreen.UiHome.Login
import com.example.loginscreen.UiHome.MainScreen
import com.example.loginscreen.UiHome.MessageScreen
import com.example.loginscreen.UiHome.Screen
import com.example.loginscreen.ViewModel.UserViewModel
import com.example.loginscreen.ui.theme.LoginScreenTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val ViewModel:UserViewModel by viewModels<UserViewModel> ()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoginScreenTheme {

                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = Screen.Login.route){
                        composable(
                            Screen.Login.route,
                            exitTransition = {
                                slideOutHorizontally(
                                    targetOffsetX = { -1000 },
                                    animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
                                )
                            },
                            popEnterTransition = {
                                slideInHorizontally(
                                    initialOffsetX = { -1000 },
                                    animationSpec = tween(durationMillis = 400, easing = LinearOutSlowInEasing)
                                )
                            }
                        ){
                            Login(  navController = navController,ViewModel,navController.context)
                        }
                        composable(
                            Screen.register.route,
                            enterTransition = {
                                slideInHorizontally(
                                    initialOffsetX = { 1000 },
                                    animationSpec = tween(durationMillis = 400, easing = LinearOutSlowInEasing)
                                )
                            },
                            exitTransition = {
                                slideOutHorizontally(
                                    targetOffsetX = { 1000 },
                                    animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
                                )
                            }
                        ){
                            Create_Account( navController = navController,ViewModel,navController.context)
                        }
                        composable(
                            Screen.Main_Screen.route,
                            enterTransition = {
                                fadeIn(animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing))
                            },
                            exitTransition = {
                                slideOutHorizontally(
                                    targetOffsetX = { -1000 },
                                    animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
                                )
                            }
                        ){
                            MainScreen(viewModel = ViewModel,navController)
                        }
                    composable(
                        "message/{first_name}",
                        enterTransition = {
                            slideInHorizontally(
                                initialOffsetX = { 1000 },
                                animationSpec = tween(durationMillis = 400, easing = LinearOutSlowInEasing)
                            )
                        },
                        exitTransition = {
                            slideOutHorizontally(
                                targetOffsetX = { 1000 },
                                animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
                            )
                        }
                    ) { backStackEntry ->
                        val userId = backStackEntry.arguments?.getString("first_name") ?: ""
                        MessageScreen(navController, ViewModel, userId) // Pass userId to MessageScreen
                    }
                        }
                }

                }
            }
        }



