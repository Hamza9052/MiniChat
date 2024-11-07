package com.hamza.test.ViewModel

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamza.test.Constants
import com.hamza.test.Event.UserEvent
import com.hamza.test.Event.user
import com.hamza.test.UiHome.Screen
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class UserViewModel() : ViewModel() {




    fun action(event: UserEvent, context: Context){
        when(event){
            is UserEvent.Login -> Logins(event.user,event.state,context)
            is UserEvent.CreateAccount -> CreateAccount(event.user,event.state,context)

        }

    }

    private val _used = MutableLiveData("")
    val usd: LiveData<String> get() = _used


    private val password = mutableMapOf<String,String>()
    private val logId = mutableMapOf<String,String>()


    private val user = Firebase.auth.currentUser



    private val firstNames = MutableStateFlow(mutableListOf<String>())
    val userlist:StateFlow<List<String>> = firstNames.asStateFlow()


    var name= ""




    init {
        users()

    }
    init {
            if (Firebase.auth.currentUser?.uid != null) {
                fetchUserFirstName(Firebase.auth.currentUser?.uid!!)

            }

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
                        state(false)
                    }
                }

    }


    /**
    *this function for login
    * */
    private fun Logins(User: user, state: (state: Boolean) -> Unit, context: Context) {



            if (user != null ){

                FirebaseAuth.getInstance().signInWithEmailAndPassword(User.emial,User.password).addOnCompleteListener {
                        task->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "Login:success")
                        state(true)





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
            }else{
                Toast.makeText(
                    context,
                    "The Account doesn't excited ",
                    Toast.LENGTH_SHORT,
                ).show()
            }



    }


    /**
   * this function for show all user on your main Screen
   * */
     fun users() {



            if (user == null) {
                callbackFlow<Screen.Login> {
                    Log.e("tag", "nothing")
                }
            } else {

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

        viewModelScope.launch {
            val collection1 = Firebase.firestore.collection(Constants.MESSAGES).document("allmessage")

            if (message.isNotEmpty()) {

                collection1.collection(username+name).document().set(

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


    /**
   * this message for get message
   * */

    fun GetMessage(us:String){
        viewModelScope.launch {
        Firebase.firestore
            .collection(Constants.MESSAGES)
            .document("allmessage")
            .collection(name+us )
            .get().addOnSuccessListener{sanphot->



                if (sanphot.isEmpty){
                    Firebase.firestore.collection(Constants.MESSAGES)
                        .document("allmessage").collection(us+name)
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
                                    if (nams != null && nams == name || nams == us){
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


            }


                else{
                    Firebase.firestore.collection(Constants.MESSAGES)
                        .document("allmessage").collection(name+us)
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
                                    if (nams != null && nams == name || nams == us){
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