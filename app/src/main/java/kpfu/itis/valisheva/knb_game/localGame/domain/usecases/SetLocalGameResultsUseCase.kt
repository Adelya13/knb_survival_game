package kpfu.itis.valisheva.knb_game.localGame.domain.usecases

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kpfu.itis.valisheva.knb_game.localGame.domain.repository.LocalGamePlayersRepository
import javax.inject.Inject

class SetLocalGameResultsUseCase @Inject constructor(
    private val localGamePlayersRepository: LocalGamePlayersRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend operator fun invoke(gameStatus: Boolean?, opponentUid: String){
        return withContext(dispatcher){
            localGamePlayersRepository.setLocalGameResults(gameStatus, opponentUid)
        }
    }
}
