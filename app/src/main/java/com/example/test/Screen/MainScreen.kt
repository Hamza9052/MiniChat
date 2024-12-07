package com.example.test.UiHome

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.DrawerDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material.TextButton
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.test.Event.UserEvent
import com.example.test.Event.user
import com.example.test.R
import com.example.test.ViewModel.UserViewModel
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.asFlow
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.test.AccountManager.accountManager
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.text.ifEmpty

@SuppressLint("StateFlowValueCalledInComposition", "SuspiciousIndentation",
    "CoroutineCreationDuringComposition"
)
@Composable
fun MainScreen(
    viewModel: UserViewModel,
    navController: NavController
) {
    val active by remember { mutableStateOf(false) }
    val showDialog = remember { mutableStateOf(false) }
    val drawerState = rememberDrawerState (initialValue = DrawerValue.Closed )
    val scope = rememberCoroutineScope()
    val isRefreshing = remember { mutableStateOf(false) }
    val name by viewModel._name.observeAsState()
    val profile = rememberAsyncImagePainter(
        model = ImageRequest.Builder(navController.context)
            .data(viewModel.generateSignedUrl(name.toString()))
            .crossfade(true)
            .error(R.drawable.profil)
            .build()
    )

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    drawerContainerColor = Color.DarkGray,
                    drawerContentColor = contentColorFor(colorResource(R.color.White)),
                    modifier =     if(navController.context.resources.configuration.smallestScreenWidthDp >= 400){
                        Modifier
                            .fillMaxWidth(0.3f)
                    }else{
                        Modifier
                            .fillMaxWidth(0.7f)
                    },
                    content = {

                        VerticalDivider(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardColors(
                                    contentColor = Color.DarkGray,
                                    containerColor = Color.DarkGray,
                                    disabledContentColor = Color.Transparent,
                                    disabledContainerColor = Color.Transparent
                                ),
                                onClick = {

                                }
                            ) {

                                Row {
                                    Image(
                                        painter = profile,
                                        contentDescription = "profile",
                                        modifier = Modifier
                                            .size(60.dp)
                                            .clip(RoundedCornerShape(50.dp)),
                                    )
                                    Column {
                                        Text(
                                            name.toString(),
                                            color = colorResource(R.color.BurlyWood),
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            modifier = Modifier.padding(start = 5.dp, top = 5.dp)
                                        )
                                    }

                                }


                            }
                        }

                        VerticalDivider(modifier = Modifier.height(10.dp))

                        NavigationDrawerItem(
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.AccountBox,
                                    contentDescription = "Profile Icon",
                                    tint = colorResource(R.color.BurlyWood)
                                )
                            },
                            label = {
                                Text(
                                    text = "Profile",
                                    color = colorResource(R.color.White)
                                )
                            },
                            selected = false,
                            onClick = {
                                navController.navigate(Screen.Profile.route)
                            },
                            colors = NavigationDrawerItemDefaults.colors(colorResource(R.color.DimGray))
                        )

                        NavigationDrawerItem(
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Logout,
                                    contentDescription = "Logout",
                                    tint = colorResource(R.color.BurlyWood)
                                )
                            },
                            label = {
                                Text(
                                    text = "Logout",
                                    color = colorResource(R.color.White)
                                )
                            },
                            selected = false,
                            onClick = {
                                showDialog.value = true
                            },
                            colors = NavigationDrawerItemDefaults.colors(colorResource(R.color.DimGray))
                        )
                    }
                )
            },
            content =  {

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .background(Color.DarkGray)
                ) {

                    Searchbar(active, viewModel, navController, showDialog,{
                        scope.launch {
                            drawerState.open()
                        }
                    })

                    if (showDialog.value) {
                        Log.e("show", "work")

                        AlertDialogSingOut(
                            onDismissRequest = { showDialog.value = false },
                            onConfirmation = {
                                viewModel.action(
                                    UserEvent.signOut() { state ->
                                        if (state) {
                                            showDialog.value = false
                                            navController.navigate(Screen.Login.route)
                                        }
                                    },
                                    context = navController.context
                                )
                            }
                        )

                    }
                    SwipeRefresh(
                        state = rememberSwipeRefreshState(isRefreshing.value),
                        onRefresh = {
                            isRefreshing.value = true
                            scope.launch{
                                delay(2000)
                                isRefreshing.value = false
                            }

                        }
                    ) {
                    LazyColumn(
                        Modifier.fillMaxSize()
                    ) {

                        items(viewModel.userlist.value.size) { item ->
                            var user = viewModel.userlist.value[item]
                            var image = viewModel.ImageUri.value[item]
                            var painterUri = rememberAsyncImagePainter(
                                model = ImageRequest.Builder(navController.context)
                                    .data(image)
                                    .crossfade(true)
                                    .error(R.drawable.profil)
                                    .build()
                            )
                            LaunchedEffect(user) {
                                viewModel.getlastmessage(user)
                            }


                            var lastMessage = viewModel.last.value?.get(user)
                            Log.e("test id for saga ", "${name.toString() + user + lastMessage}")
                            if (lastMessage == "" || name.toString() == user || lastMessage == null) {
                                Log.e("message is Empty", lastMessage.toString())
                                Log.e("test id for saga ", user)
                            } else {
                                Spacer(modifier = Modifier.height(15.dp))
                                listItem(user, navController, lastMessage.toString(),painterUri)
                            }


                        }
                    }

                    }


                }
            })



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
    click:()->Unit
) {
    val user = user()
    var actives by remember { mutableStateOf(active) }
    var search by remember { mutableStateOf(user.search) }

    // Animated padding and visibility
    val animatedPaddingH by animateDpAsState(
        targetValue = 0.dp,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing) // Smooth easing
    )
    val animatedPaddingV by animateDpAsState(
        targetValue = 0.dp,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing) // Smooth easing
    )


    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = animatedPaddingH,
                vertical = animatedPaddingV
            )
            .background(color = colorResource(R.color.DimGray))
    ) {
        // Animated Logout Button

        SearchBar(
            query = search,
            modifier = Modifier
                .weight(1f)
                .padding(end = if (actives) 0.dp else 8.dp),
            colors = SearchBarDefaults.colors(containerColor = colorResource(R.color.DimGray)),
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
                if (actives) {
                    AnimatedVisibility(
                        visible = actives,
                        exit = fadeOut(animationSpec = tween(50)) + slideOutHorizontally(
                            targetOffsetX = { it },
                            animationSpec = tween(50, easing = FastOutSlowInEasing)
                        ),
                        enter = fadeIn(animationSpec = tween(50)) + slideInHorizontally(
                            initialOffsetX = {it},
                            animationSpec = tween(50, easing = FastOutSlowInEasing)
                        )
                    ) {
                    IconButton(
                        onClick = { actives = false }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close",
                            modifier = Modifier.size(20.dp),
                            tint = colorResource(R.color.BurlyWood)
                        )
                    }
                    }
                }else{
                    AnimatedVisibility(
                        visible = !actives,
                        exit = fadeOut(animationSpec = tween(50)) + slideOutHorizontally(
                            targetOffsetX = { it },
                            animationSpec = tween(50, easing = FastOutSlowInEasing)
                        ),
                        enter = fadeIn(animationSpec = tween(50)) + slideInHorizontally(
                            initialOffsetX = {it},
                            animationSpec = tween(50, easing = FastOutSlowInEasing)
                        )
                    ) {

                        Icon(
                            modifier = Modifier
                                .size(40.dp)
                                .padding(end = 2.dp, top = 3.dp)
                                .clickable {
                                    click()
                                },
                            tint = colorResource(R.color.BurlyWood),
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu Icon"
                        )

                    }
                    search = ""
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
            val name by viewModel._name.observeAsState()
            for (i in 0 until size) {
                val users = viewModel.userlist.value[i]
                if (users.contains(search) && search.isNotEmpty() && name.toString() != users) {
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
    message: String,
    painterUri:AsyncImagePainter
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
                        painter = painterUri,
                        contentDescription = "profile",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(50.dp)),

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
                        message,
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
    onConfirmation: () -> Unit,
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
        text = {
            Text(
                "Logout",
                fontSize = 25.sp,
                color = colorResource(R.color.BurlyWood)
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirmation) {
                Text(
                    text = "Logout",
                    color = colorResource(R.color.BurlyWood),
                    fontSize = 18.sp
                )
            }
        },
        dismissButton = {
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


//@Composable
//@Preview
//fun test (
//){
//    MainScreen(
//        navController = NavController(context = LocalContext.current),
//        viewModel = UserViewModel()
//    )
//}