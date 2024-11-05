package com.example.loginscreen.UiHome


import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row


import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.loginscreen.R
import com.example.loginscreen.ViewModel.UserViewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import com.example.loginscreen.Constants

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageScreen(
    navController: NavController,
    viewModel: UserViewModel = viewModel(),
    userId: String,

    ){
     viewModel.GetMessage()

    val Message:String by viewModel.message.observeAsState(initial = "")
    val Messages:List<Map<String,Any>> by viewModel.messages.observeAsState(
        initial = emptyList<Map<String,Any>>().toMutableList()
    )
    LaunchedEffect(userId) {
        viewModel.fetchUserFirstName(userId) // Ensure this fetches the correct name
    }
    val firstName by viewModel.usd.observeAsState("")

    Scaffold(


        topBar = {
            CenterAlignedTopAppBar(

                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colorResource(R.color.DarkSlateGray),
                    titleContentColor = colorResource(R.color.White),
                ),
                title = {
                    Text(
                        firstName,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = colorResource(R.color.BurlyWood),
                        fontWeight = FontWeight.ExtraBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {navController.navigate(Screen.Main_Screen.route) }) {
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
                                .size(40.dp)
                                .width(40.dp)
                        )

                }

            )
        })
    { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color.DarkGray),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = AbsoluteAlignment.Right
        ) {
            LazyColumn(
                reverseLayout = true
            ) {
                items(Messages.size) {index->
                    val message = Messages[index]
                    val isCurrentUser = message[Constants.IS_CURRENT_USER] as Boolean
                    messageUser1(
                        message =  message[Constants.MESSAGE].toString(),
                        isCurrentUser = isCurrentUser
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                }


            }
            Row (
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.height(85.dp)
            ) {
                OutlinedTextField(
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                    value = Message,
                    onValueChange ={
                        viewModel.updateMessage(it)
                    },
                    placeholder = {
                        Text(
                            "Message",
                            textAlign = TextAlign.Center,
                            color = Color.Black
                        ) },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 15.dp,
                            bottom = 15.dp),
                    singleLine = true,
                    shape = RoundedCornerShape(30.dp),
                    textStyle = TextStyle(color = Color.Black),

                    trailingIcon = {
                        IconButton(onClick = {
                            viewModel.NewMessage()
                        }) {
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
        }

    }


}



@Composable
fun messageUser1(
    message: String,
    isCurrentUser:Boolean
){

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
            text = message,
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