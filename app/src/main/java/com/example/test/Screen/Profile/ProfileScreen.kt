package com.example.test.Screen.Profile

import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.annotation.ContentView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.ActivityNavigatorExtras
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import android.net.Uri
import androidx.annotation.Size
import androidx.compose.material3.CircularProgressIndicator

import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.test.Event.UserEvent
import com.example.test.R
import com.example.test.ViewModel.UserViewModel
import java.io.File
import kotlin.contracts.contract

@Composable
fun ProfileScreen(navController: NavController, ViewModel: UserViewModel) {
    val imageUri = rememberSaveable { mutableStateOf("") }

    var image = ViewModel.generateSignedUrl(ViewModel.name)
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(navController.context)
            .data(image)
            .crossfade(true)
            .build()
    )
    val PainterImage = rememberAsyncImagePainter(
        imageUri.value.ifEmpty {
            R.drawable.profil
        }
    )



    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.background(colorResource(R.color.DimGray))
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier
                .size(150.dp)
        ) {
            if (painter.state is coil.compose.AsyncImagePainter.State.Error){
                val pickMedia = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
                    // Callback is invoked after the user selects a media item or closes the
                    // photo picker.
                    if (uri != null) {
                        val filePath = resolveUriToFilePath(uri, navController.context)
                        if (filePath != null){
                            Log.d("PhotoPicker", "Selected URI: $filePath")
                            ViewModel.action(UserEvent.Upload_Image(filePath),navController.context)
                            imageUri.value = uri.toString()
                        }else{
                            Log.e("PhotoPicker", "Failed to resolve file path")
                        }
                    } else {
                        Log.d("PhotoPicker", "No media selected")
                    }
                }
                if (PainterImage.state is coil.compose.AsyncImagePainter.State.Loading){
                    CircularProgressIndicator(
                        color = colorResource(R.color.BurlyWood),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                Image(
                    painter = PainterImage,
                    contentDescription = "Image of Profile",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(RoundedCornerShape(75.dp))
                )
                IconButton(
                    onClick = {
                        pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = "Pick Picture",
                        tint = Color.White
                    )
                }
            }
            else{
                if (painter.state is coil.compose.AsyncImagePainter.State.Loading){
                    CircularProgressIndicator(
                        color = colorResource(R.color.BurlyWood),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                Image(
                    painter = painter,
                    contentDescription = "Image of Profile",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(RoundedCornerShape(75.dp))
                )
                IconButton(
                    onClick = {
                        "pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))"
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = "Pick Picture",
                        tint = Color.White
                    )
                }
            }


        }
        Spacer(modifier = Modifier.weight(0.1f))
        Text(
            ViewModel.name,
            color = colorResource(R.color.BurlyWood),
            fontSize = 25.sp,
            fontWeight = FontWeight.ExtraBold,
        )
        Spacer(modifier = Modifier.weight(0.8f))
        Text(
            "Bio",
            color = colorResource(R.color.BurlyWood),
            fontSize = 25.sp,
            fontWeight = FontWeight.ExtraBold,
        )
        Spacer(modifier = Modifier.weight(0.2f))
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Bio",
                color = colorResource(R.color.BurlyWood),
                fontSize = 25.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.weight(1f))
    }


}

private fun resolveUriToFilePath(uri: Uri, context: Context): String? {
    val contentResolver = context.contentResolver
    val fileName = "temp_image.png"
    val tempFile = File(context.cacheDir, fileName)

    try {
        contentResolver.openInputStream(uri)?.use { inputStream ->
            tempFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        return tempFile.absolutePath
    } catch (e: Exception) {
        Log.e("PhotoPicker", "Error resolving URI to file path: ${e.message}", e)
    }
    return null
}