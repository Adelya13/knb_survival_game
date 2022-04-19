package kpfu.itis.valisheva.knb_game.login.data

import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AdditionalUserInfo
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.awaitCancellation
import kpfu.itis.valisheva.knb_game.login.domain.models.UserModel
import kpfu.itis.valisheva.knb_game.login.domain.repositories.UserRepository
import okhttp3.internal.wait
import java.lang.Exception
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.math.asin

class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
): UserRepository {

    override suspend fun signIn(email: String, password: String): FirebaseUser = suspendCoroutine{
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if(user!=null) {
                        it.resume(user)
                    }else{
                        it.resumeWithException(Exception("SIGN_IN_EXCEPTION"))
                    }
                }else{
                    it.resumeWithException(Exception("SIGN_IN_EXCEPTION"))
                    Log.w("SIGN_IN_EXCEPTION", "signInUserWithEmail:failure", task.exception)
                }
            }
    }

    override suspend fun register(email: String, password: String, name: String):  FirebaseUser = suspendCoroutine{
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if(user!=null) {
                        it.resume(user)
                    }else{
                        it.resumeWithException(Exception("SIGN_IN_EXCEPTION"))
                    }

                }else{
                    it.resumeWithException(Exception("SIGN_IN_EXCEPTION"))
                    Log.w("REGISTER_EXCEPTION", "createUserWithEmail:failure", task.exception)
                }
            }
    }
}

