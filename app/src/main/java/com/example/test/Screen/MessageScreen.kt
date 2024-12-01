package com.example.test.UiHome


import android.annotation.SuppressLint
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
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
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
import com.example.test.R
import com.example.test.ViewModel.UserViewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import com.example.test.Constants
import java.time.LocalDateTime
import java.util.Calendar

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageScreen(
    navController: NavController,
    viewModel: UserViewModel = viewModel(),
    userId: String,

    ){


    val Message:String by viewModel.message.observeAsState(initial = "")
    val Messages:List<Map<String,Any>> by viewModel.messages.observeAsState(
        initial = emptyList<Map<String,Any>>().toMutableList()
    )
    val times:List<String> by viewModel.listTime.observeAsState(
        initial = emptyList<String>().toMutableList()
    )

    LaunchedEffect(userId) {
        viewModel.fetchUserFirstName(userId)
        viewModel.GetMessage(userId)
    }

    val firstName by viewModel.usd.observeAsState(userId)


    Scaffold(


        topBar = {
            CenterAlignedTopAppBar(

                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colorResource(R.color.DarkSlateBlue),
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
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    reverseLayout = true,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    items(Messages.size) { index ->
                        val message = Messages[index]
                        val isCurrentUser = message[Constants.IS_CURRENT_USER] as Boolean
                        val time = times[index]
                        messageUser1(
                            message = message[Constants.MESSAGE].toString(),
                            isCurrentUser = isCurrentUser,
                            time = time
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                    }

                }

                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                        .padding(8.dp)
                ) {
                    OutlinedTextField(
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text
                        ),
                        colors = OutlinedTextFieldDefaults.colors(colorResource(R.color.DarkSlateBlue)),
                        value = Message,
                        onValueChange = {
                            viewModel.updateMessage(it)
                        },
                        placeholder = {
                            Text(
                                "Message",
                                textAlign = TextAlign.Center,
                                color = Color.White
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        singleLine = true,
                        shape = RoundedCornerShape(30.dp),
                        textStyle = TextStyle(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        ),

                        trailingIcon = {
                            IconButton(onClick = {
                                viewModel.NewMessage(firstName)
                                viewModel.sendMessage(firstName,Message,navController.context)
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Send,
                                    contentDescription = "Search",
                                    modifier = Modifier.size(20.dp),
                                    tint = colorResource(R.color.BurlyWood)

                                )
                            }
                        }
                    )

                }

            }

    }


}



@SuppressLint("NewApi")
@Composable
fun messageUser1(
    message: String,
    isCurrentUser:Boolean,
    time:String
){

    var calendar = LocalDateTime.now()
    calendar.hour
    calendar.minute
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardColors(
             contentColor =   if(!isCurrentUser)
                 colorResource(R.color.DarkSlateBlue)
             else
                 colorResource(R.color.BurlyWood),
            containerColor =if(!isCurrentUser)
                colorResource(R.color.DarkSlateBlue)
            else
                colorResource(R.color.BurlyWood),
            disabledContainerColor = if(!isCurrentUser)
                colorResource(R.color.DarkSlateBlue)
            else
                colorResource(R.color.BurlyWood),
            disabledContentColor = if(!isCurrentUser)
                colorResource(R.color.DarkSlateBlue)
            else
                colorResource(R.color.BurlyWood)

        )
    ) {
        Column {
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
                    .padding(top = 13.dp, start = 13.dp, end = 13.dp, bottom = 2.dp),
                color =
                if (!isCurrentUser)
                    colorResource(R.color.BurlyWood)
                else
                    colorResource(R.color.DarkSlateBlue)

            )
            Spacer(modifier = Modifier.height(1.dp))
            Text(
                text = time,
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 3.dp),
                color =Color.Black
            )
        }

    }

}
