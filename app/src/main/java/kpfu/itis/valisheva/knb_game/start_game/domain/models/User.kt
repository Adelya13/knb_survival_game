package kpfu.itis.valisheva.knb_game.start_game.domain.models

data class User(
    val name: String,
    val email: String,
    val moneyCnt: Int,
    val creditSum: Int?,
    var cntScissors: Int?,
    var cntStone: Int?,
    var cntPaper: Int?,
    var cntStar: Int?,
    var status: Boolean,
)
