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
import javax.inject.Inject
import kotlin.math.asin

class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
): UserRepository {


    override fun signIn(email: String, password: String): Task<AuthResult> {
        return auth.signInWithEmailAndPassword(email, password)
    }

    override fun register(email: String, password: String, name: String): Task<AuthResult> {
        return auth.createUserWithEmailAndPassword(email, password)
    }


//    override fun signIn(email: String, password: String): FirebaseUser? {
//        var user: FirebaseUser? = null
//            auth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        user = auth.currentUser
//                        println("UUUUSSSEEERRR $user")
//                    }else{
//                        Log.w("SIGN_IN_EXCEPTION", "signInUserWithEmail:failure", task.exception)
//                    }
//                }
//            println("UUUUSSSEEERRR $user")
//            return user
//
//    }

//    override fun register(email: String, password: String, name: String):  FirebaseUser? {
//        var user: FirebaseUser? = null
//
//        auth.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    user = auth.currentUser
//
//                }else{
//                    Log.w("REGISTER_EXCEPTION", "createUserWithEmail:failure", task.exception)
//                }
//            }
//
//        return user
//    }
}

