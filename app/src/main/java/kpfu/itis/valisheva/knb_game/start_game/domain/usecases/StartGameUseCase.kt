package kpfu.itis.valisheva.knb_game.start_game.domain.usecases

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kpfu.itis.valisheva.knb_game.start_game.domain.repositories.UserRepository
import javax.inject.Inject

class StartGameUseCase@Inject constructor(
    private val userRepository: UserRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend operator fun invoke() {
        return withContext(dispatcher){
            userRepository.start()
        }
    }
}
