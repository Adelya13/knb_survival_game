package kpfu.itis.valisheva.knb_game.localGame.domain.repository

import kpfu.itis.valisheva.knb_game.basic_game.domain.models.Player

interface LocalGamePlayersRepository {
    suspend fun waitOpponentDecision() : Boolean
    suspend fun showPlayerInfo() : Player
    suspend fun setCard(card: String)
    suspend fun waitOpponentChoose(opponentUid: String) : String
    suspend fun setLocalGameResults(gameStatus: Boolean?, opponentUid: String)
}
