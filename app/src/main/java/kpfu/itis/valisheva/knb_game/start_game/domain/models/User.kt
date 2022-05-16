package kpfu.itis.valisheva.knb_game.start_game.domain.models

data class User(
    val name: String,
    val email: String,
    val moneyCnt: Int,
    val number: Int?,
    val creditSum: Int?,
    val cntScissors: Int?,
    val cntStone: Int?,
    val cntPaper: Int?,
    val status: Boolean,
)
