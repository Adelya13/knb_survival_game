package kpfu.itis.valisheva.knb_game.basic_game.domain.repositories

import kpfu.itis.valisheva.knb_game.basic_game.domain.models.Player

interface PlayerRepository {

    suspend fun searchPlayers() : ArrayList<Player>
    suspend fun findPlayerStarsCnt() : Int

}
