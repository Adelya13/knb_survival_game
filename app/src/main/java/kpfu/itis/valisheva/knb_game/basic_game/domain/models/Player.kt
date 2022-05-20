package kpfu.itis.valisheva.knb_game.basic_game.domain.models

data class Player(
    val uid: String,
    val name: String,
    val email: String,
    val moneyCnt: Int,
    val creditSum: Int,
    val cntScissors: Int,
    val cntStone: Int,
    val cntPaper: Int,
    val cntStar: Int,
    val status: Boolean,
)
