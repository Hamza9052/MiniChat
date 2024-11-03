package com.example.loginscreen.ViewModel

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loginscreen.Constants
import com.example.loginscreen.Event.UserEvent
import com.example.loginscreen.Event.user
import com.example.loginscreen.UiHome.Screen
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
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

    private val _used = MutableLiveData("")
    val usd: LiveData<String> get() = _used


    private var auth = FirebaseAuth.getInstance()
    private val user = Firebase.auth.currentUser
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    var name :String = ""
    val userIds = FirebaseFirestore.getInstance()
        .collection("users").document(userId!!).get()
        .addOnSuccessListener { document->
            name = document.getString("first_name").toString()


    }

    private val password = mutableMapOf<String,String>()
    private val logId = mutableMapOf<String,String>()

    private val firstNames = MutableStateFlow(mutableListOf<String>())
    val userlist:StateFlow<List<String>> = firstNames.asStateFlow()


    init {
    GetMessage()
    }
    init {
        users(user())
    }
    init {
        if (userId != null) {
            fetchUserFirstName(userId)
        }
    }
init {
    userIds
}




    /**
    *this function for Create Account
    * */
    private fun CreateAccount(User: user, state: (state: Boolean) -> Unit, context: Context) {

        auth.createUserWithEmailAndPassword(User.emial,User.password)
            .addOnCompleteListener {task->
                if (task.isSuccessful) {


                    logId["first_name"] = User.loginId
                       password["password"] = User.password

                    FirebaseFirestore.getInstance().collection("users").document(userId!!)
                        .set(User.loginId)
                    FirebaseFirestore.getInstance().collection("password").document(userId!!)
                        .set(User.password)


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


    /**
    *this function for login
    * */
    private fun Logins(User: user, state: (state: Boolean) -> Unit, context: Context) {
        val user = Firebase.auth.currentUser
        viewModelScope.launch {


            if (user != null ){

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
                            "Password or Email incorrect.",
                            Toast.LENGTH_SHORT,
                        ).show()


                    }
                }
            }else{
                Toast.makeText(
                    context,
                    "The Account doesn't excited ",
                    Toast.LENGTH_SHORT,
                ).show()
            }

        }

    }


    /**
   * this function for show all user on your main Screen
   * */
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

                             val firs_name = document.getString("first_name")
                             if (firs_name != null){
                                 firstNames.value.add(firs_name)

                             }


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


        FirebaseFirestore.getInstance().collection("users").whereEqualTo("first_name",userId).get()
                .addOnSuccessListener { querySnapshot  ->

                        if (!querySnapshot.isEmpty ) {
                            val document = querySnapshot.documents.first()
                            _used.value = document.getString("first_name") ?: ""
                        }else{
                            _used.value = ""
                        }


                }
                .addOnFailureListener { exception ->
                    Log.w("name","error")
                }

    }

    /**
    * this message for add new message
    * */

    fun NewMessage(){
        val message:String = _message.value ?: throw IllegalArgumentException("Message empty")


        if (message.isNotEmpty()){

                    Firebase.firestore.collection(Constants.MESSAGES).document().set(
                hashMapOf(
                    Constants.MESSAGE to message,
                    Constants.SENT_BY to Firebase.auth.currentUser?.uid,
                    Constants.SENT_ON to System.currentTimeMillis()
                )
            ).addOnSuccessListener {
                _message.value = ""
            }
        }
    }


    /**
   * this message for get message
   * */

    fun GetMessage(){
        Firebase.firestore.collection(Constants.MESSAGES)
            .orderBy(Constants.SENT_ON)
            .addSnapshotListener{value , e ->
                if (e != null){
                    Log.w("mess","ListFailed",e)
                    return@addSnapshotListener
                }

                val list = emptyList<Map<String,Any>>().toMutableList()

                if (value != null){
                    for(doc in value){
                        val data = doc.data
                        data[Constants.IS_CURRENT_USER] =
                            Firebase.auth.currentUser?.uid.toString() == data[Constants.SENT_BY].toString()
                        list.add(data)
                    }
                }
                updateMessages(list)
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