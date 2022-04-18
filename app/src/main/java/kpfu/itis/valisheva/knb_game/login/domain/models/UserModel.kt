package kpfu.itis.valisheva.knb_game.login.domain.models

import java.util.*

data class UserModel(
    val id: Int,
    val name: String,
    val email: String,
    val pass: UUID,
    val cntMoney: Int,

)
