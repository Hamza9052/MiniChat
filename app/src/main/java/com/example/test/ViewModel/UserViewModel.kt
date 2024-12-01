package com.example.test.ViewModel

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.test.API.NotificationApi
import com.example.test.Notification.NotificationData
import com.example.test.Notification.NotificationIn
import com.example.test.Constants
import com.example.test.Event.UserEvent
import com.example.test.Event.user
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.messaging.FirebaseMessaging
import com.example.test.UiHome.Screen
import com.example.test.token.AccessToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime

class UserViewModel() : ViewModel() {




    fun action(event: UserEvent, context: Context){
        when(event){
            is UserEvent.Login -> Logins(event.user,event.state,context)
            is UserEvent.CreateAccount -> CreateAccount(event.user,event.state,context)
            is UserEvent.signOut -> signout(event.user,event.state,context)

        }

    }

    private val _used = MutableLiveData("")
    val usd: LiveData<String> get() = _used


    private val _listTime = MutableLiveData(emptyList<String>().toMutableList())
    val listTime :LiveData<MutableList<String>> = _listTime
    private val firstNames = MutableStateFlow(mutableListOf<String>())
    val userlist:StateFlow<List<String>> = firstNames.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false) // Default to logged out
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    var name= ""
   var id =""


    init {
        users()

    }


    init {
            if (Firebase.auth.currentUser?.uid != null) {
                fetchUserFirstName(Firebase.auth.currentUser?.uid!!)

            }

    }





    /**
     *this function for save Login
     * */


    @SuppressLint("SuspiciousIndentation")
    fun check(context: Context, navController: NavController):String {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val check = sharedPreferences.getString("login","")
        val email=sharedPreferences.getString("email","")
        val password = sharedPreferences.getString("password","")
            if (check.equals("true")){
                Logins(user(password = password!!, emial = email!!), context = context, state = {check.toBoolean()})

            }

        return check.toString()
    }
    private var token = MutableStateFlow("")


    /**
    *this function for Create Account
    * */
    private fun CreateAccount(User: user, state: (state: Boolean) -> Unit, context: Context) {

        FirebaseMessaging.getInstance().token.addOnCompleteListener{task->
            if (task.isSuccessful){
                token.update {task.result.toString()}
                Log.d("token success", "CreateAccount: $token")
            }
        }

           Firebase.auth.createUserWithEmailAndPassword(User.emial, User.password)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {

                        FirebaseFirestore.getInstance().collection("users").document(task.result.user?.uid!!)
                            .set(
                                hashMapOf(
                                    "password" to User.password,
                                    "first_name" to User.loginId,
                                    "FcmToken" to token.value
                                )

                            )

                        state(true)


                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            context,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()

                    }
                }

    }



    /**
    *this function for login
    * */
    @SuppressLint("SuspiciousIndentation")
    private fun Logins(User: user, state: (state: Boolean) -> Unit, context: Context) {

       viewModelScope.launch{
           FirebaseMessaging.getInstance().token.addOnCompleteListener{task->
               if (task.isSuccessful){
                   token.update { task.result.toString() }
               }
           }
           Firebase.auth.signInWithEmailAndPassword(User.emial,User.password).addOnCompleteListener {
                   task->
               if (task.isSuccessful) {
                   // Sign in success, update UI with the signed-in user's information

                   Log.d(TAG, "Login:success")
                   id = task.result.user?.uid!!
                   state(true)
                   FirebaseFirestore.getInstance()
                       .collection("users").document(id)
                       .update("FcmToken",token.value).addOnCompleteListener{task->
                           if (task.isSuccessful){
                               Log.d("token Success", "Logins: isSuccessful", )
                           }else {
                               Log.e("token Failed", "Logins: ${task.exception}", )
                           }
                       }
                   _isLoggedIn.value = true

                   FirebaseFirestore.getInstance()
                       .collection("users")
                       .document(id).get()
                       .addOnSuccessListener { document ->
                           name = document.getString("first_name").toString()
                           val sharedPreferences: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                           sharedPreferences.edit()
                               .putString("login", _isLoggedIn.value.toString())
                               .putString("email",User.emial)
                               .putString("uid",id)
                               .putString("password",User.password)
                               .putString("name",name)
                               .apply()
                       }



               } else {
                   // If sign in fails, display a message to the user.
                   Log.e(TAG, "Login:failure", task.exception)
                   Toast.makeText(
                       context,
                       "Password or Email incorrect.",
                       Toast.LENGTH_SHORT,
                   ).show()


               }
           }

       }

    }

    /**
     *this function  for signOut
     * */

    private fun signout(User:user,state: (state: Boolean) -> Unit, context: Context) {

            Log.e("logout", "im here")
            Firebase.auth.signOut()
            state(true)
             FirebaseFirestore.getInstance()
            .collection("users").document(id)
            .update("FcmToken","").addOnCompleteListener{task->
                if (task.isSuccessful){
                    Log.d("token Success", "Logins: isSuccessful", )
                }else {
                    Log.e("token Failed", "Logins: ${task.exception}", )
                }
            }
        _isLoggedIn.value = false
            // Clear user data from SharedPreferences
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit()
            .putString("login", _isLoggedIn.value.toString())
            .remove("")
            .remove("email")
            .remove("uid")
            .remove("password")
            .apply()

    }



    /**
   * this function for show all user on your main Screen
   * */
     fun users() {

                FirebaseFirestore.getInstance().collection("users")
                    .get()
                    .addOnSuccessListener { result ->

                        for (document in result) {

                            val firs_name = document.getString("first_name")
                            if (firs_name != null) {
                                firstNames.value.add(firs_name)
                            }
                        }
                    }
    }

    /**
     * this function for get notification of message
     * */

    private var tok = MutableStateFlow("")

    fun sendMessage(name:String,messag:String,context: Context){
        viewModelScope.launch{
            try {
                var toks:String = ""
            FirebaseFirestore.getInstance().collection("users")
                .whereEqualTo("first_name",name)
                .get()
                .addOnSuccessListener{documents->
                 viewModelScope.launch{
                     if (!documents.isEmpty){

                         for (doc in documents){

                             tok.update {
                                 doc.getString("FcmToken").toString()
                             }

                         }
                         if (tok.value.isNotEmpty()) {
                             val notification = NotificationIn(
                                 message = NotificationData(
                                     token = tok.value,
                                     hashMapOf(
                                         "title" to _used.value.toString(),
                                         "body" to messag
                                     )
                                 ),
                             )

                             sendPushNotification(notification)
                         } else {
                             Log.e("sendMessage", "No valid FCM token available to send.")
                         }
                     }else{
                         Log.e("get Token", "get Token is Failed !!:${toks}")
                     }
                 }
                }

                Log.e("get Token", "get Token is success:${toks}")







            } catch (e: Exception) {
            Log.e("sendMessage", "Error in sendMessage: ${e.message}", e)
            }
        }
    }


    private suspend fun sendPushNotification(notification: NotificationIn) {
        // Fetch Bearer token
        val accessToken = AccessToken.getToken()

        if (accessToken != null) {
            withContext(Dispatchers.IO){
                val notificationInterface = NotificationApi.create()
                val call = notificationInterface.sendNotification(notification, "Bearer $accessToken")

                call.enqueue(object : Callback<NotificationIn> {
                    override fun onResponse(call: Call<NotificationIn>, response: Response<NotificationIn>) {
                        if (response.isSuccessful) {
                            Log.d("Notification", "Notification sent successfully: ${response.body()}")
                        } else {
                            Log.e("Notification", "Failed to send notification${response.errorBody()?.string()}")

                        }
                    }

                    override fun onFailure(call: Call<NotificationIn>, t: Throwable) {
                        Log.e("Notification", "Failed to send notification")
                    }
                })
            }

        } else {
            Log.e("Notification", "Failed to get access token")

        }
    }

  /**
  * this function for show all user on your main Screen
  * */
    private val _message = MutableLiveData("")
    val message: MutableLiveData<String> = _message

    private val _messages = MutableLiveData(emptyList<Map<String,Any>>().toMutableList())
    val messages :LiveData<MutableList<Map<String,Any>>> = _messages



    /**
     * Update the message value as user types
     */
    fun updateMessage(message: String) {
        _message.value = message
    }

    /**
     * call name
     */
    fun fetchUserFirstName(userId: String) {

        FirebaseFirestore.getInstance()
            .collection("users")
            .whereEqualTo("first_name",userId)
            .get()
                .addOnSuccessListener { querySnapshot  ->

                        if (!querySnapshot.isEmpty ) {
                            val document = querySnapshot.documents.first()
                            _used.value = document.getString("first_name") ?: ""
                        }


                }
                .addOnFailureListener { exception ->
                    Log.w("name","error")
                }

    }

    /**
    * this message for add new message
    * */

    @SuppressLint("NewApi")
    fun NewMessage(username:String){
        val message:String = _message.value ?: throw IllegalArgumentException("Message empty")
        val collection1 = username + name
        val collection2 = name + username
        viewModelScope.launch {
            val collectiona = Firebase.firestore.collection(Constants.MESSAGES).document("allmessage").collection(collection2)

            if (message.isNotEmpty()) {
                collectiona.get().addOnSuccessListener {snap->
                    val finalcollection = if(!snap.isEmpty) collection2 else collection1

                    Firebase.firestore.collection(Constants.MESSAGES)
                        .document("allmessage").collection(finalcollection)
                        .document().set(

                        hashMapOf(
                            Constants.MESSAGE to message,
                            Constants.SENT_BY to name,
                            Constants.SENT_ON to System.currentTimeMillis(),
                            Constants.TIME to "${LocalDateTime.now().hour}:${LocalDateTime.now().minute}"
                        )

                    ).addOnSuccessListener {
                        _message.value = ""
                    }
                }
            }
        }
    }


    /**
   * this message for get message
   * */

    fun GetMessage(username:String){
        viewModelScope.launch {
        Firebase.firestore
            .collection(Constants.MESSAGES)
            .document("allmessage")
            .collection(name+username)
            .get().addOnSuccessListener{sanphot->



                if (sanphot.isEmpty){
                    Firebase.firestore.collection(Constants.MESSAGES)
                        .document("allmessage").collection(username+name)
                        .orderBy(Constants.SENT_ON)
                        .addSnapshotListener{value , e ->
                            if (e != null){
                                Log.w("mess","ListFailed",e)
                                return@addSnapshotListener
                            }

                            val list = emptyList<Map<String,Any>>().toMutableList()
                            val listTi = emptyList<String>().toMutableList()
                            value?.let {snapshot ->
                                for(doc in snapshot.documents){
                                    val time = doc.getString("time")
                                    val nams = doc.getString("sent_by")
                                    Log.e("data", "${nams}")
                                    if (nams != null && nams == name || nams == username){
                                        val data = doc.data
                                        data?.set(Constants.IS_CURRENT_USER,
                                            name == data[Constants.SENT_BY].toString()
                                        )
                                        if (data != null) {
                                            list.add(data)
                                        }
                                        Log.e("data", "${data}")

                                    }
                                    if (time!!.isNotEmpty()){
                                        listTi.add(time)

                                    }
                                    else{
                                        Log.e("data", doc.id)
                                    }
                                }

                                    _listTime.value = listTi.asReversed()


                                updateMessages(list)
                            }

                        }



            } else{
                    Firebase.firestore.collection(Constants.MESSAGES)
                        .document("allmessage")
                        .collection(name+username)
                        .orderBy(Constants.SENT_ON)
                        .addSnapshotListener{value , e ->
                            if (e != null){
                                Log.w("mess","ListFailed",e)
                                return@addSnapshotListener
                            }
                            val list = emptyList<Map<String,Any>>().toMutableList()
                            val listTi = emptyList<String>().toMutableList()
                            value?.let {snapshot ->
                                for(doc in snapshot.documents){
                                    val time = doc.getString("time")
                                    val nams = doc.getString("sent_by")
                                    Log.e("data", "${nams}")
                                    if (nams != null && nams == name || nams == username){
                                        val data = doc.data
                                        data?.set(Constants.IS_CURRENT_USER, name == data[Constants.SENT_BY].toString())

                                        if (data != null) {
                                            list.add(data)
                                        }
                                        if (time!!.isNotEmpty()){
                                            listTi.add(time)

                                        }
                                        Log.e("data", "${data}")

                                    }else{
                                        Log.e("data", doc.id)
                                    }


                                }
                                _listTime.value = listTi.asReversed()
                                updateMessages(list)
                            }

                        }

                }



            }

    } }



    private val lastmessage = MutableStateFlow(mutableMapOf<String,String>())
    val last:StateFlow<Map<String,String>> = lastmessage.asStateFlow()

    fun getlastmessage(username: String) {


        val collectionPath1 = "$username$name"
        val collectionPath2 = "$name$username"
        Firebase.firestore
            .collection(Constants.MESSAGES)
            .document("allmessage")
            .collection(collectionPath1)
            .orderBy(Constants.SENT_ON)
            .limitToLast(1)
            .get()
            .addOnSuccessListener { snapshot1 ->
                if (!snapshot1.isEmpty) {
                    // Process the result from collectionPath1
                    snapshot1?.documents?.lastOrNull()?.let { doc ->
                        val lastMsg = doc.getString("message").orEmpty()
                        val currentLast = lastmessage.value[username]

                        // Update only if the message changes
                        if (currentLast != lastMsg) {
                            lastmessage.update { it.toMutableMap().apply { this[username] = lastMsg } }
                        }
                    } ?: run {
                        // No messages found for the user
                        lastmessage.update { it.toMutableMap().apply { this[username] = "" } }
                    }
                } else {
                    // Check the second collection path
                    Firebase.firestore
                        .collection(Constants.MESSAGES)
                        .document("allmessage")
                        .collection(collectionPath2)
                        .orderBy(Constants.SENT_ON)
                        .limitToLast(1)
                        .get()
                        .addOnSuccessListener { snapshot2 ->
                            snapshot2?.documents?.lastOrNull()?.let { doc ->
                                val lastMsg = doc.getString("message").orEmpty()
                                val currentLast = lastmessage.value[username]

                                // Update only if the message changes
                                if (currentLast != lastMsg) {
                                    lastmessage.update { it.toMutableMap().apply { this[username] = lastMsg } }
                                }
                            } ?: run {
                                // No messages found for the user
                                lastmessage.update { it.toMutableMap().apply { this[username] = "" } }
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.e("getLastMessage", "Error querying collectionPath2", e)
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.e("getLastMessage", "Error querying collectionPath1", e)
            }
    }

    /**
     * Update the list after getting the details from firestore
     */

    private fun updateMessages(list: MutableList<Map<String, Any>>) {
        _messages.value = list.asReversed()
    }

    override fun onCleared() {
        super.onCleared()
    }

}