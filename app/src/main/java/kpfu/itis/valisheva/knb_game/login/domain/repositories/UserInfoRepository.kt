package kpfu.itis.valisheva.knb_game.login.domain.repositories

import com.google.firebase.auth.FirebaseUser
import kpfu.itis.valisheva.knb_game.login.domain.models.UserInfo

interface UserInfoRepository {

    suspend fun signIn(email: String, password: String): UserInfo
    suspend fun register(email: String, password: String, name: String): UserInfo

}
