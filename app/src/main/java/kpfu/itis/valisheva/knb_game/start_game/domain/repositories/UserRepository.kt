package kpfu.itis.valisheva.knb_game.start_game.domain.repositories

import kpfu.itis.valisheva.knb_game.start_game.domain.models.User


interface UserRepository{
    suspend fun findUser() : User
    suspend fun signOut()
    suspend fun getCredit(credit: Int)
    suspend fun start()
}
