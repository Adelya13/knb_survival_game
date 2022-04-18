package kpfu.itis.valisheva.knb_game.login.domain.usecases

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kpfu.itis.valisheva.knb_game.login.domain.models.UserModel
import kpfu.itis.valisheva.knb_game.login.domain.repositories.UserRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(email: String, password: String): Task<AuthResult> {
        return userRepository.signIn(email, password)
    }
}
