package kpfu.itis.valisheva.knb_game.login.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.*
import kpfu.itis.valisheva.knb_game.login.domain.models.UserInfo
import kpfu.itis.valisheva.knb_game.login.domain.repositories.UserRepository
import kpfu.itis.valisheva.knb_game.login.utils.exceptions.NotFoundUserExceptions
import kpfu.itis.valisheva.knb_game.login.utils.exceptions.SignInFailedException
import kpfu.itis.valisheva.knb_game.login.utils.exceptions.UserAlreadyRegisteredException
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val dbReference: DatabaseReference
): UserRepository {

    override suspend fun signIn(email: String, password: String): UserInfo = suspendCoroutine{

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val currUser = auth.currentUser
                    if(currUser!=null) {
                        dbReference.child("users").child(currUser.uid).get().addOnSuccessListener {info->
                            try{
                                val user = UserInfo(
                                    info.child("name").value.toString(),
                                    info.child("email").value.toString(),
                                    info.child("moneyCnt").value.toString().toInt()
                                )
                                it.resume(user)

                            }catch (e: Exception){
                                it.resumeWithException(e)
                                println("Exception")
                            }
                        }.addOnFailureListener{ex->
                            it.resumeWithException(NotFoundUserExceptions("Пользователь не найден"))
                        }
                    }else{
                        it.resumeWithException(NotFoundUserExceptions("Проблемы со входом, попробуйте позже"))
                    }
                }else{
                    it.resumeWithException(SignInFailedException("Пользователь не найден"))
                    Log.w("SIGN_IN_EXCEPTION", "signInUserWithEmail:failure", task.exception)
                }
            }
    }

    override suspend fun register(email: String, password: String, name: String):  UserInfo = suspendCoroutine{
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val currUser = auth.currentUser
                    if(currUser!=null) {
                        val user = UserInfo(name,email,0)
                        dbReference.child("users").child(currUser.uid).setValue(user)
                        it.resume(user)

                    }else{
                        it.resumeWithException(UserAlreadyRegisteredException("Регистрация провалена"))
                    }

                }else{
                    it.resumeWithException(UserAlreadyRegisteredException("Такой пользователь уже существует"))
                    Log.w("REGISTER_EXCEPTION", "createUserWithEmail:failure", task.exception)
                }
            }
    }
}

