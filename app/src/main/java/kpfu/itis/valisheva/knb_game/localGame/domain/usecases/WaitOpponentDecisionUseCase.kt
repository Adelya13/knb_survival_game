package kpfu.itis.valisheva.knb_game.localGame.domain.usecases

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kpfu.itis.valisheva.knb_game.basic_game.domain.models.Player
import kpfu.itis.valisheva.knb_game.basic_game.domain.repositories.PlayerRepository
import kpfu.itis.valisheva.knb_game.localGame.domain.repository.LocalGamePlayersRepository
import javax.inject.Inject

class WaitOpponentDecisionUseCase  @Inject constructor(
    private val localGamePlayersRepository: LocalGamePlayersRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend operator fun invoke() : Boolean {
        return withContext(dispatcher){
            localGamePlayersRepository.waitOpponentDecision()
        }
    }
}
