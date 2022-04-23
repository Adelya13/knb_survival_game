package kpfu.itis.valisheva.knb_game.login.domain.usecases

import com.google.firebase.auth.FirebaseUser

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kpfu.itis.valisheva.knb_game.login.domain.models.UserInfo
import kpfu.itis.valisheva.knb_game.login.domain.repositories.UserRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend operator fun invoke(email: String, password: String, name: String): UserInfo {
        return withContext(dispatcher){
            userRepository.register(email, password, name)
        }

    }
}
