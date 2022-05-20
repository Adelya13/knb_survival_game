package kpfu.itis.valisheva.knb_game.basic_game.domain.repositories

import kpfu.itis.valisheva.knb_game.basic_game.domain.models.Player

interface PlayerRepository {

    suspend fun startGame() : Player
    suspend fun searchPlayers() : ArrayList<Player>
    suspend fun findPlayerStarsCnt() : Int
    suspend fun requestLocalChallenge(email: String) : ArrayList<Player>

}
