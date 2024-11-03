package com.example.loginscreen.UiHome

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.loginscreen.R
import com.example.loginscreen.ViewModel.UserViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: UserViewModel,
    navController: NavController
){
    var searchText by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(
            colors = TopAppBarColors(
                containerColor = Color.Transparent,
                scrolledContainerColor = Color.Transparent,
             navigationIconContentColor = Color.Transparent,
            titleContentColor = Color.Transparent,
             actionIconContentColor = Color.Transparent
            ),
            title = { "" },
            actions = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()

                ) {
                    OutlinedTextField(
                        value = searchText,
                        onValueChange ={
                            searchText = it
                        },
                        placeholder = {
                            Text(
                                "Search",
                                textAlign = TextAlign.Center,
                                color = Color.Black
                            ) },
                        modifier = Modifier
                            .fillMaxSize(),
                        singleLine = true,
                        shape = RoundedCornerShape(30.dp),
                        textStyle = TextStyle(color = Color.Black),

                        trailingIcon = {
                            IconButton(onClick = {  }) {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = "Search",
                                    modifier = Modifier.size(20.dp),
                                    tint = Color.Black

                                )
                            }
                        }
                    )
                }
            }) }
    ) {padding->

        Spacer(modifier = Modifier.height(70.dp))
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(Color.DarkGray)
                .padding(padding)
        ) {

            Spacer(modifier = Modifier.height(15.dp))
            LazyColumn(Modifier.fillMaxSize()) {
                items(viewModel.userlist.value.size) {item->

                    val user = viewModel.userlist.value.get(item)
                    if (viewModel.name == user ){

                    }else{
                        listItem(user, navController)
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }
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


@Composable
fun listItem(
    name: String,
    navController: NavController
){

        Row(modifier = Modifier
            .fillMaxSize()
        ) {

            Card(
                modifier = Modifier
                .fillMaxSize(),
                shape = RoundedCornerShape(30.dp),
                colors = CardColors(
                    contentColor = colorResource(R.color.DarkSlateGray),
                    containerColor = colorResource(R.color.DarkSlateGray),
                    disabledContentColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                ),
                onClick = {
                    navController.navigate("message/$name")
                },
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