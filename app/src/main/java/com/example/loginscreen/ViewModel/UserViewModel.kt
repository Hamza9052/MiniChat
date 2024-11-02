package com.example.loginscreen.ViewModel

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loginscreen.Event.UserEvent
import com.example.loginscreen.Event.user
import com.example.loginscreen.UiHome.Screen
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class UserViewModel() : ViewModel() {




    fun action(event: UserEvent,context: Context){
        when(event){
            is UserEvent.Login -> Logins(event.user,event.state,context)
            is UserEvent.CreateAccount -> CreateAccount(event.user,event.state,context)

        }

    }

    private var auth = FirebaseAuth.getInstance()
    private val user = Firebase.auth.currentUser
    val luser= mutableMapOf<String,String>()
init {
    users(user())
}



    private val firstNames = MutableStateFlow(mutableListOf<String>())
    val userlist:StateFlow<List<String>> = firstNames.asStateFlow()

    private fun CreateAccount(User: user, state: (state: Boolean) -> Unit, context: Context) {

        auth.createUserWithEmailAndPassword(User.emial,User.password)
            .addOnCompleteListener {task->
                if (task.isSuccessful) {
                    val userId= FirebaseAuth.getInstance().currentUser?.uid
                    luser["first_name"]=User.loginId
                    FirebaseFirestore.getInstance().collection("users").document(userId!!)
                        .set(luser)

                    state(true)


                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        context,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    state(false)
                }
            }


    }

    private fun Logins(User: user, state: (state: Boolean) -> Unit, context: Context) {
        val user = Firebase.auth.currentUser
        viewModelScope.launch {


            if (user != null){

                auth.signInWithEmailAndPassword(User.emial,User.password).addOnCompleteListener {
                        task->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "Login:success")
                        state(true)

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "Login:failure", task.exception)
                        Toast.makeText(
                            context,
                            "Login failed.",
                            Toast.LENGTH_SHORT,
                        ).show()


                    }
                }
            }else{
                state(false)
            }

        }

    }


     fun users(User: user) {



             if (user == null){
                 callbackFlow<Screen.Login> {
                     Log.e("tag","nothing")
                 }
             }else{

                 FirebaseFirestore.getInstance().collection("users")
                     .get()
                     .addOnSuccessListener { result ->

                         for (document in result){

                             val firsname = document.getString("first_name")
                             if (firsname != null){
                                 firstNames.value.add(firsname)
                             }

                         }



                     }
             }


    }

    override fun onCleared() {
        super.onCleared()

    }
}