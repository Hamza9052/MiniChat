package com.example.test.ViewModel

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.annotation.RestrictTo
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cloudinary.Cloudinary
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.test.API.NotificationApi
import com.example.test.AccountManager.LoginAction
import com.example.test.AccountManager.Result
import com.example.test.Notification.NotificationData
import com.example.test.Notification.NotificationIn
import com.example.test.Constants
import com.example.test.Event.Loginstate
import com.example.test.Event.UserEvent
import com.example.test.Event.user
import com.example.test.UiHome.Screen
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.messaging.FirebaseMessaging
import com.example.test.token.AccessToken
import com.google.firebase.auth.FirebaseAuth
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.colorResource
import androidx.lifecycle.asFlow
import androidx.lifecycle.map
import androidx.navigation.NavController
import com.example.test.AccountManager.ResultIn
import com.example.test.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.tasks.await

class UserViewModel() : ViewModel() {

    var states by mutableStateOf(Loginstate())
        private set


    fun logAction(loginAction: LoginAction){
        when(loginAction){
            is LoginAction.Login -> {
                when(loginAction.result){
                    Result.Cancelled -> {
                        states = states.copy(
                            errorMessage = "Login was Cancelled"
                        )
                    }
                    Result.Failure -> {
                        states = states.copy(
                            errorMessage = "Login was Failed"
                        )
                    }
                    is Result.Success -> {
                        states = states.copy(
                            loggedInUser = loginAction.result.name
                        )
                    }
                }
            }
            is LoginAction.OnEmailChange -> {
                states = states.copy(
                    email = loginAction.email
                )
            }
            is LoginAction.OnPasswordChange -> {
                states = states.copy(
                    password = loginAction.password
                )
            }

            is LoginAction.Logging ->{
                when(loginAction.result){
                    ResultIn.Cancelled -> {
                        states = states.copy(
                            errorMessage = "Login was Cancelled"
                        )
                    }
                    ResultIn.Failure -> {
                        states = states.copy(
                            errorMessage = "Login was Failed"
                        )
                    }
                    is ResultIn.Success -> {
                        states = states.copy(
                            loggedInUser = loginAction.result.name
                        )
                    }

                    ResultIn.NoCredentials ->{
                        states = states.copy(
                            errorMessage = "No Credentials were set up."
                        )
                    }
                }
            }
        }
    }



    fun action(event: UserEvent, context: Context){
        when(event){
            is UserEvent.Login -> Logins(event.email,event.password,event.state,context)
            is UserEvent.CreateAccount -> CreateAccount(event.user,event.state,context)
            is UserEvent.signOut -> signout(event.state,context)
            is UserEvent.Upload_Image -> upload(event.image,context)

        }

    }





    private val _used = MutableLiveData("")
    val usd: LiveData<String> get() = _used


    private val _listTime = MutableLiveData(emptyList<String>().toMutableList())
    val listTime :LiveData<MutableList<String>> = _listTime
    private val firstNames = MutableLiveData<MutableList<String>>(mutableListOf())
    val userlist: LiveData<List<String>> = firstNames.map { it.toList() }

    private val _ImageUri = MutableLiveData<MutableList<String>>(mutableListOf())
    val ImageUri:LiveData<List<String>> = _ImageUri.map { it.toList() }
    private val _isLoggedIn = MutableStateFlow(false) // Default to logged out
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val name = MutableLiveData("")
    val _name: LiveData<String> get() = name

    private val id = MutableLiveData("")





    init {
            if (Firebase.auth.currentUser?.uid != null) {
                fetchUserFirstName(Firebase.auth.currentUser?.uid!!)
            }

    }




    /**
     *this function for save Login
     * */

    @SuppressLint("SuspiciousIndentation")
    fun check(context: Context):String {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val check = sharedPreferences.getString("login","")
        val email = sharedPreferences.getString("email","")
        val password = sharedPreferences.getString("password","")
            if (check.equals("true")){
                Logins(emial = email.toString(),password = password.toString(), context = context, state = {state->
                    if (!state) {
                        // If login fails, reset the login state in SharedPreferences
                        sharedPreferences.edit()
                            .putString("login", "false")
                            .apply()
                    } else {
                        _isLoggedIn.value = true
                    }

                })

                sharedPreferences.getString("uid","")
                sharedPreferences.getString("name","")
                sharedPreferences.getString("email","")
            }else{
                _isLoggedIn.value = true
            }

        return check!!
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
    private fun Logins(emial:String,password:String, state: (state: Boolean) -> Unit, context: Context) {


           FirebaseMessaging.getInstance().token.addOnCompleteListener{task->
               if (task.isSuccessful){
                   token.update { task.result.toString() }
               }
           }
           Firebase.auth.signInWithEmailAndPassword(emial,password).addOnCompleteListener {
                   task->
               if (task.isSuccessful) {
                   // Sign in success, update UI with the signed-in user's information

                   Log.d(TAG, "Login:success")
                   id.value = task.result.user?.uid!!
                   state(true)
                   FirebaseFirestore.getInstance()
                       .collection("users").document(id.value.toString())
                       .update("FcmToken",token.value).addOnCompleteListener{task->
                           if (task.isSuccessful){
                               Log.d("token Success", "Logins: isSuccessful", )
                           }else {
                               Log.e("token Failed", "Logins: ${task.exception}", )
                           }
                       }
                   FirebaseFirestore.getInstance()
                       .collection("users")
                       .document(id.value.toString()).get()
                       .addOnSuccessListener { document ->
                           name.value = document.getString("first_name").toString()
                           _isLoggedIn.value = true
                           val sharedPreferences: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                           val editor = sharedPreferences.edit()
                                 .putString("uid",id.value)
                                 .putString("login", _isLoggedIn.value.toString())
                                 .putString("name",name.value)
                                 .putString("password",password)
                                 .putString("email",emial)
                           editor.apply()
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

    /**
     *this function  for signOut
     * */

    private fun signout(state: (state: Boolean) -> Unit, context: Context) {

            Log.e("logout", "im here")
            Firebase.auth.signOut()
            state(true)
             FirebaseFirestore.getInstance()
            .collection("users").document(id.value.toString())
            .update("FcmToken","").addOnCompleteListener{task->
                if (task.isSuccessful){
                    Log.d("token Success", "Logins: isSuccessful", )
                }else {
                    Log.e("token Failed", "Logins: ${task.exception}", )
                }
            }
        _isLoggedIn.value = false
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit()
            .clear()
            .apply()


    }



    /**
   * this function for show all user on your main Screen
   * */
    private val _DataLoadign = MutableLiveData<Boolean>()
    val DataLoadign: LiveData<Boolean> get() = _DataLoadign
     fun users() {
         _DataLoadign.value = true
                FirebaseFirestore.getInstance().collection("users")
                    .get()
                    .addOnSuccessListener { result ->
                        var tes = ""
                        val use = firstNames.value ?:mutableListOf()
                        val ima = _ImageUri.value ?:mutableListOf()
                        for (document in result) {

                            val firs_name = document.getString("first_name")
                           for (l in 0 until userlist.value!!.size ){
                               if (firs_name == userlist.value!!.get(l)){
                                   tes = firs_name

                               }
                           }
                            if (firs_name != null && firs_name != tes) {
                                use.add(firs_name.toString())
                                val name = generateSignedUrl(firs_name.toString())
                                ima.add(name!!)
                            }
                        }

                        _ImageUri.value = ima
                        firstNames.value = use
                        _DataLoadign.value = false
                    }.addOnFailureListener{
                        _DataLoadign.value = false
                    }
    }

    /**
     * this function for get notification of message
     * */

    private var tok = MutableStateFlow("")

    fun sendMessage(names:String,messag:String,context: Context){
        viewModelScope.launch{
            try {
                var toks:String = ""
            FirebaseFirestore.getInstance().collection("users")
                .whereEqualTo("first_name",names)
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
                                         "title" to name.value!!,
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
        val collection1 = username + name.value
        val collection2 = name.value + username
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
                            Constants.SENT_BY to name.value,
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
            .collection(name.value+username)
            .get().addOnSuccessListener{sanphot->



                if (sanphot.isEmpty){
                    Firebase.firestore.collection(Constants.MESSAGES)
                        .document("allmessage").collection(username+name.value)
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
                                    if (nams != null && nams == name.value || nams == username){
                                        val data = doc.data
                                        data?.set(Constants.IS_CURRENT_USER,
                                            name.value == data[Constants.SENT_BY].toString()
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
                        .collection(name.value+username)
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
                                    if (nams != null && nams == name.value || nams == username){
                                        val data = doc.data
                                        data?.set(Constants.IS_CURRENT_USER, name.value == data[Constants.SENT_BY].toString())

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

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    fun fetchLastMessagesForAllUsers(users: List<String>, currentUser: String) {
        _isLoading.value = true

        val fetchJobs = users.map { user ->
            if (user != currentUser) {
                getlastmessage(user, currentUser)
            } else {
                null
            }
        }.filterNotNull()

        // Wait for all fetching jobs to complete
        viewModelScope.launch {
            fetchJobs.joinAll()
            _isLoading.value = false
        }
    }

//    private val lastMessage = MutableStateFlow(mutableMapOf<String,String>())
//    private val lastMessage = MutableLiveData<MutableMap<String, String>>(mutableMapOf())
//    val last:StateFlow<Map<String,String>> = lastMessage.asStateFlow()
//    val last: LiveData<MutableMap<String, String>> = lastMessage
private val _lastMessage = MutableStateFlow<Map<String, String>>(emptyMap())
    val lastMessage: StateFlow<Map<String, String>> = _lastMessage.asStateFlow()
    private fun getlastmessage(username: String, name: String):Job {
        return viewModelScope.launch {
            val collectionPath1 = "$username$name"
            val collectionPath2 = "$name$username"

            val result = try {
                val snapshot1 = Firebase.firestore
                    .collection(Constants.MESSAGES)
                    .document("allmessage")
                    .collection(collectionPath1)
                    .orderBy(Constants.SENT_ON)
                    .limitToLast(1)
                    .get()
                    .await()

                snapshot1.documents.lastOrNull()?.getString("message")?.orEmpty()
                    ?: fetchFromSecondPath(collectionPath2)
            } catch (e: Exception) {
                Log.e("getLastMessage", "Error fetching messages for $username", e)
                ""
            }

            updateLastMessage(username, result)
        }
    }

    private suspend fun fetchFromSecondPath(collectionPath: String): String {
        return try {
            val snapshot = Firebase.firestore
                .collection(Constants.MESSAGES)
                .document("allmessage")
                .collection(collectionPath)
                .orderBy(Constants.SENT_ON)
                .limitToLast(1)
                .get()
                .await()

            snapshot.documents.lastOrNull()?.getString("message").orEmpty()
        } catch (e: Exception) {
            Log.e("getLastMessage", "Error fetching from second path: $collectionPath", e)
            ""
        }
    }

    private fun updateLastMessage(username: String, message: String) {
        _lastMessage.value = _lastMessage.value.toMutableMap().apply {
            put(username, message)
        }
    }
    /**
     * Update the list after getting the details from firestore
     */

    private fun updateMessages(list: MutableList<Map<String, Any>>) {
        _messages.value = list.asReversed()
    }


    private var config: HashMap<String, String> = HashMap()

    private fun upload (filepath: String, context: Context) {


        MediaManager.init(context,config)
            MediaManager.get().upload(filepath)
                .option("public_id",name.value)
                .callback(object : UploadCallback {
                override fun onSuccess(requestId: String?, resultData: MutableMap<Any?, Any?>?) {
                    Toast.makeText(context, "Task successful", Toast.LENGTH_SHORT).show()

                }

                override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {


                }

                override fun onReschedule(requestId: String?, error: ErrorInfo?) {

                }

                override fun onError(requestId: String?, error: ErrorInfo?) {

                    Toast.makeText(context, "Task Not successful"+ error, Toast.LENGTH_SHORT).show()

                }

                override fun onStart(requestId: String?) {

                    Toast.makeText(context, "Start", Toast.LENGTH_SHORT).show()
                }
            }).dispatch()


    }


     @SuppressLint("SuspiciousIndentation")
     fun generateSignedUrl(publicId: String): String? {
        val config = mutableMapOf<String, Any>()


        val cloudinary = Cloudinary(config)
        val signedUrl = cloudinary.url()
            .secure(true)  // Ensure the URL is HTTPS
            .signed(true)  // Private image
            .generate(publicId)


    // Return the signed URL for the image
             return signedUrl
    }






    override fun onCleared() {
        super.onCleared()
    }

}


















