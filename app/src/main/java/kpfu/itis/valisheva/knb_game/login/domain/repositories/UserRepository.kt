package kpfu.itis.valisheva.knb_game.login.domain.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kpfu.itis.valisheva.knb_game.login.domain.models.UserModel

interface UserRepository {

    suspend fun signIn(email: String, password: String): FirebaseUser
    suspend fun register(email: String, password: String, name: String):  FirebaseUser

}
