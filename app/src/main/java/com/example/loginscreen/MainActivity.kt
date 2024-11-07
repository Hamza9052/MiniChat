package com.hamza.test

import android.os.Bundle
import android.util.Log
import android.view.View
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hamza.test.UiHome.Create_Account
import com.hamza.test.UiHome.Login
import com.hamza.test.UiHome.MainScreen
import com.hamza.test.UiHome.MessageScreen
import com.hamza.test.UiHome.Screen
import com.hamza.test.ViewModel.UserViewModel
import com.hamza.test.ui.theme.LoginScreenTheme
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val ViewModel:UserViewModel by viewModels<UserViewModel> ()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoginScreenTheme {

                val navController = rememberNavController()


                NavHost(navController = navController, startDestination =   Screen.Login.route){

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



