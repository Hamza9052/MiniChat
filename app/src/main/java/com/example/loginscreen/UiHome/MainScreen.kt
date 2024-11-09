package com.hamza.test.UiHome

import android.annotation.SuppressLint
import android.util.Log

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material.TextButton
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hamza.test.Event.UserEvent
import com.hamza.test.Event.user
import com.hamza.test.R
import com.hamza.test.ViewModel.UserViewModel



@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun MainScreen(
    viewModel: UserViewModel,
    navController: NavController,
) {
    val active by remember { mutableStateOf(false) }
    val onDismissRequest = remember { mutableStateOf(true)}
    val showDialog = remember { mutableStateOf(false) }

    Spacer(modifier = Modifier.height(70.dp))
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(Color.DarkGray)

    ) {





        Searchbar(active, viewModel, navController,showDialog)

            if (showDialog.value){
                Log.e("show","work")

                       AlertDialogSingOut(
                           onDismissRequest = { showDialog.value = false },
                           onConfirmation = {viewModel.action(
                               UserEvent.signOut() {state->
                                   if (state){
                                       showDialog.value = false
                                       navController.navigate(Screen.Login.route)
                                   }
                               },
                               context = navController.context)}
                       )

            }





            Spacer(modifier = Modifier.height(15.dp))
            LazyColumn(Modifier.fillMaxSize()) {
                items(viewModel.userlist.value.size) { item ->

                    val user = viewModel.userlist.value[item]
                    if (viewModel.name == user) {
                        Log.e("test id for saga ", user)
                    } else {
                        listItem(user, navController)
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }


        }
    }


//@Composable
//@Preview(showSystemUi = true)
//fun test(){
//    MainScreen()
//
//}

    @SuppressLint("StateFlowValueCalledInComposition")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Searchbar(
        active: Boolean,
        viewModel: UserViewModel,
        navController: NavController,
        showDialog: MutableState<Boolean>,

        ) {

        val user = user()
        var actives by remember { mutableStateOf(active) }
        var search by remember { mutableStateOf(user.search) }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = if (actives) 0.dp else 16.dp)
        ) {

            SearchBar(
                query = search,
                modifier = Modifier
                    .weight(1f) // Occupy available width but leave space for IconButton
                    .padding(end = if (actives) 0.dp else 8.dp),
                colors = SearchBarDefaults.colors(colorResource(R.color.Gray)),
                onQueryChange = { search = it },
                onSearch = {},
                active = actives,
                onActiveChange = { actives = it },
                placeholder = {
                    Text(
                        text = "Search",
                        color = colorResource(R.color.BurlyWood),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                },
                leadingIcon = {
                    (if (actives) Icons.Filled.Close else null)?.let {
                        IconButton(onClick = { navController.navigate(Screen.Main_Screen.route) }) {
                            Icon(
                                imageVector = it,
                                contentDescription = "Search",
                                modifier = Modifier.size(20.dp),
                                tint = colorResource(R.color.BurlyWood)

                            )
                        }

                    }
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search",
                        modifier = Modifier.size(20.dp),
                        tint = colorResource(R.color.BurlyWood)

                    )
                }
            ) {

                val size = viewModel.userlist.value.size

                for (i in 0 until size) {

                    val users = viewModel.userlist.value.get(i)
                    for (i in 0 until users.length) {
                        if (
                            users.contains(search) &&
                            search.isNotEmpty() &&
                            !viewModel.name.equals(users)
                        ) {
                            LazyColumn {
                                item {
                                    searchName(navController, users)
                                }
                            }
                            break
                        }
                    }
                }

            }
            if (actives){

            }else{
                Card(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(top = 33.dp)
                        .size(45.dp)
                        .clickable { showDialog.value = true },
                    shape = RoundedCornerShape(50.dp),
                    colors = CardDefaults.cardColors(colorResource(R.color.BurlyWood))
                ) {
                    IconButton(
                        onClick = { showDialog.value = true},
                        modifier = Modifier
                            .size(45.dp)

                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Search",
                            modifier = Modifier.size(30.dp) ,
                            tint = colorResource(R.color.DarkSlateBlue)

                        )

                    }
                }

        }
    }











}




@Composable
fun searchName(
    navController: NavController,
    name: String,
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        colors = CardColors(
            contentColor = colorResource(R.color.DarkSlateBlue),
            containerColor = colorResource(R.color.DarkSlateBlue),
            disabledContentColor = Color.Transparent,
            disabledContainerColor = Color.Transparent
        ), onClick = {
            navController.navigate("message/$name")
        }
    ) {
        Text(
            text = name,
            fontSize = 20.sp,
            color = colorResource(R.color.BurlyWood),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
    }
}


@Composable
fun listItem(
    name: String,
    navController: NavController,

) {

    Row(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Card(
            modifier = Modifier
                .fillMaxSize(),
            shape = RoundedCornerShape(30.dp),
            colors = CardColors(
                contentColor = colorResource(R.color.DarkSlateBlue),
                containerColor = colorResource(R.color.DarkSlateBlue),
                disabledContentColor = Color.Transparent,
                disabledContainerColor = Color.Transparent
            ),
            onClick = {
                navController.navigate("message/$name")

            }
        ) {

            Row {
                Image(
                    Icons.Filled.AccountCircle,
                    contentDescription = "profile",
                    modifier = Modifier.size(60.dp),
                    colorFilter = ColorFilter.tint(colorResource(R.color.BurlyWood))
                )
                Spacer(modifier = Modifier.weight(0.1f))
                Column {
                    Text(
                        name,
                        color = colorResource(R.color.BurlyWood),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,

                        )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Message...",
                        color = colorResource(R.color.BurlyWood),
                        fontSize = 15.sp
                    )

                }
                Spacer(modifier = Modifier.weight(3f))

            }


        }
    }
}


/**
 * this function for AlerDialog
 */



@Composable
fun AlertDialogSingOut(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {

  AlertDialog(
      onDismissRequest = onDismissRequest,
      icon = {
          Icon(
              Icons.AutoMirrored.Filled.Logout,
              contentDescription = "Logout",
              tint = colorResource(R.color.BurlyWood)
          )
             },
      text = { Text(
          "Logout",
          fontSize = 25.sp,
          color = colorResource(R.color.BurlyWood)
      ) },
      confirmButton ={
          TextButton(onClick =  onConfirmation) {
              Text(
                  text = "Logout",
                  color = colorResource(R.color.BurlyWood),
                  fontSize = 18.sp
              )
          }
      },
      dismissButton ={
          TextButton(onClick = onDismissRequest) {
              Text(
                  text = "Cancel",
                  color = colorResource(R.color.BurlyWood),
                  fontSize = 18.sp
              )
          }
      },
      containerColor = colorResource(R.color.DarkSlateBlue),
      shape = RoundedCornerShape(17.dp)
  )

}