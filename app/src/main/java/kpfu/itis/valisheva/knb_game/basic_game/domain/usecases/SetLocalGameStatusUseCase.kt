package kpfu.itis.valisheva.knb_game.basic_game.domain.usecases

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kpfu.itis.valisheva.knb_game.basic_game.domain.models.Player
import kpfu.itis.valisheva.knb_game.basic_game.domain.repositories.PlayerRepository
import javax.inject.Inject

class SetLocalGameStatusUseCase @Inject constructor(
    private val playerRepository: PlayerRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend operator fun invoke(status: Boolean, uid: String) {
        return withContext(dispatcher) {
            playerRepository.setStatus(status, uid)
        }
    }
}
