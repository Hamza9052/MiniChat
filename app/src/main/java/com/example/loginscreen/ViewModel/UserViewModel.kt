package com.hamza.test.ViewModel

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamza.test.Constants
import com.hamza.test.Event.UserEvent
import com.hamza.test.Event.user
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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



    private val firstNames = MutableStateFlow(mutableListOf<String>())
    val userlist:StateFlow<List<String>> = firstNames.asStateFlow()

    val ids = Firebase.auth.currentUser
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


    fun isUserLoggedIn(context: Context): Boolean {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("is_logged_in", false)
    }



    /**
     *this function for save Login
     * */

   @SuppressLint("CommitPrefEdits")
   private fun saveLoginState(
        context: Context,
        email: String,
        password:String,
        uid: String,

    ) {

            // Save email and UID in SharedPreferences
            val sharedPreferences: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            sharedPreferences.edit()
                .putString("user_email", email)
                .putString("user_id", uid)
                .putString("password",password)
                .apply()
    }


    /**
    *this function for Create Account
    * */
    private fun CreateAccount(User: user, state: (state: Boolean) -> Unit, context: Context) {

           Firebase.auth.createUserWithEmailAndPassword(User.emial, User.password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {




                        FirebaseFirestore.getInstance().collection("users").document(task.result.user?.uid!!)
                            .set(
                                hashMapOf(
                                 "password" to User.password,
                                    "first_name" to User.loginId
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

        Firebase.auth.signInWithEmailAndPassword(User.emial,User.password).addOnCompleteListener {
                task->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "Login:success")

                val user = FirebaseAuth.getInstance().currentUser
                saveLoginState(context,user?.email!!,task.result.user?.uid!!,User.password)

                state(true)

                id = task.result.user?.uid!!
                        FirebaseFirestore.getInstance()
                            .collection("users").document(task.result.user?.uid!!).get()
                            .addOnSuccessListener { document ->


                                    name = document.getString("first_name").toString()

                            }




            } else {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "Login:failure", task.exception)
                Toast.makeText(
                    context,
                    "Password or Email incorrect.",
                    Toast.LENGTH_SHORT,
                ).show()


            }
        }



    }

    fun retrieveUserInfoFromPreferences(context: Context): Pair<String?, String?> {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("user_email", null)
        val password = sharedPreferences.getString("password", null)
        return Pair(email, password)
    }

     fun getlastUser(context: Context){
        val (email, password) = retrieveUserInfoFromPreferences(context)

        if (email != null && password != null) {
            Firebase.auth.signInWithEmailAndPassword(email,password)
            Log.d("LastUserInfo", "No previous user logged in")
        } else {
            Log.d("LastUserInfo", "No previous user logged in")
        }
    }
    /**
     *this function  for signOut
     * */

    private fun signout(User:user,state: (state: Boolean) -> Unit, context: Context) {

            Log.e("logout", "im here")
            Firebase.auth.signOut()
            state(true)


            // Clear user data from SharedPreferences
            val sharedPreferences: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            sharedPreferences.edit()
                .remove("user_email")
                .remove("user_id")
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
                            Constants.SENT_ON to System.currentTimeMillis()
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

                            value?.let {snapshot ->
                                for(doc in snapshot.documents){

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
                                    else{
                                        Log.e("data", doc.id)
                                    }



                                }
                                updateMessages(list)
                            }

                        }


            } else{


                    Firebase.firestore.collection(Constants.MESSAGES)
                        .document("allmessage").collection(name+username)
                        .orderBy(Constants.SENT_ON)
                        .addSnapshotListener{value , e ->
                            if (e != null){
                                Log.w("mess","ListFailed",e)
                                return@addSnapshotListener
                            }

                            val list = emptyList<Map<String,Any>>().toMutableList()



                            value?.let {snapshot ->
                                for(doc in snapshot.documents){

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

                                    }else{
                                        Log.e("data", doc.id)
                                    }



                                }
                                updateMessages(list)
                            }

                        }

                }


            }

    }
    }


    /**
     * this for search
     * */










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