package com.example.loginscreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
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
                        composable(Screen.Login.route){
                            Login(  navController = navController,ViewModel,navController.context)
                        }
                        composable(Screen.register.route){
                            Create_Account( navController = navController,ViewModel,navController.context)
                        }
                        composable(Screen.Main_Screen.route){
                            MainScreen(viewModel = ViewModel,navController)
                        }
                    composable("message/{first_name}") { backStackEntry ->
                        val userId = backStackEntry.arguments?.getString("first_name") ?: ""
                        MessageScreen(navController, ViewModel, userId) // Pass userId to MessageScreen
                    }
                        }
                }

                }
            }
        }



