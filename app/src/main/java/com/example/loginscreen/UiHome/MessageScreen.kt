package com.example.loginscreen.UiHome

import android.graphics.drawable.shapes.Shape
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.captionBar
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.waterfall
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat.MessagingStyle.Message
import androidx.navigation.NavController
import com.example.loginscreen.R
import kotlinx.coroutines.flow.callbackFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageScreen(
//    navController: NavController
){
    var SendMessage by remember { mutableStateOf("") }
    Scaffold(


        topBar = {
            CenterAlignedTopAppBar(
                windowInsets = WindowInsets.captionBar,
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colorResource(R.color.DarkSlateGray),
                    titleContentColor = colorResource(R.color.White),
                ),
                title = {
                    Text(
                        "test",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = colorResource(R.color.BurlyWood),
                        fontWeight = FontWeight.ExtraBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description",
                            tint = colorResource(R.color.BurlyWood),
                            modifier = Modifier.size(30.dp)
                        )
                    }
                },
                actions = {

                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            tint = colorResource(R.color.BurlyWood),
                            contentDescription = "Localized description",
                            modifier = Modifier
                                .size(50.dp)
                                .width(20.dp)
                        )

                }

            )
        }, bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(topStart = 33.dp, topEnd = 33.dp)),
                containerColor =  MaterialTheme.colorScheme.primaryContainer,
                actions = {
                    OutlinedTextField(
                        value = SendMessage,
                        onValueChange ={
                            SendMessage = it
                        },
                        placeholder = {
                            Text(
                                "Message",
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
                                    imageVector = Icons.Filled.Send,
                                    contentDescription = "Search",
                                    modifier = Modifier.size(20.dp),
                                    tint = Color.Black

                                )
                            }
                        }
                    )
                }
            )
        }

        ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = AbsoluteAlignment.Right
        ) {
            LazyColumn {
                // Add a single item
                item {
                    messageUser1((false))
                    Spacer(modifier = Modifier.height(5.dp))
                }

                item {
                    messageUser1(true)
                }
            }
        }
    }


}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun test(

){
    MessageScreen()
}

@Composable
fun messageUser1(isCurrentUser:Boolean){

    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardColors(
             contentColor =   if(!isCurrentUser)
                 colorResource(R.color.DarkSlateGray)
             else
                 colorResource(R.color.BurlyWood),
            containerColor =if(!isCurrentUser)
                colorResource(R.color.DarkSlateGray)
            else
                colorResource(R.color.BurlyWood),
            disabledContainerColor = if(!isCurrentUser)
                colorResource(R.color.DarkSlateGray)
            else
                colorResource(R.color.BurlyWood),
            disabledContentColor = if(!isCurrentUser)
                colorResource(R.color.DarkSlateGray)
            else
                colorResource(R.color.BurlyWood)

        )
    ) {
        Text(
            text = "Message",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign =
            if (isCurrentUser)
                TextAlign.End
            else
                TextAlign.Start,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            color =
            if (!isCurrentUser)
                colorResource(R.color.BurlyWood)
            else
                colorResource(R.color.DarkSlateGray)


        )

    }

}