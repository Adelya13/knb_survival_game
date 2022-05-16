package kpfu.itis.valisheva.knb_game.login.domain.usecases

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kpfu.itis.valisheva.knb_game.login.domain.models.UserInfo
import kpfu.itis.valisheva.knb_game.login.domain.repositories.UserInfoRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val userRepository: UserInfoRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend operator fun invoke(email: String, password: String): UserInfo {
        return withContext(dispatcher){
            userRepository.signIn(email, password)
        }
    }
}
