package com.example.test

import android.os.Build
import android.os.Bundle
import android.Manifest
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.test.UiHome.Create_Account
import com.example.test.UiHome.Login
import com.example.test.UiHome.MainScreen
import com.example.test.UiHome.MessageScreen
import com.example.test.UiHome.Screen
import com.example.test.ViewModel.UserViewModel
import com.example.test.ui.theme.LoginScreenTheme
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
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
                LaunchedEffect(key1 = "") {
                    ViewModel.check(navController.context,navController)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                        Dexter.withContext(navController.context)
                            .withPermission(Manifest.permission.POST_NOTIFICATIONS)
                            .withListener(object : PermissionListener{
                                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {}

                                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {}

                                override fun onPermissionRationaleShouldBeShown(
                                    p0: PermissionRequest?,
                                    p1: PermissionToken?,
                                ) {
                                    p1?.continuePermissionRequest()
                                }

                            }).check()
                    }
                }


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



