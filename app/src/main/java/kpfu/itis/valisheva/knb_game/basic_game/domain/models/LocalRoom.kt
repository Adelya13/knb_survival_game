package kpfu.itis.valisheva.knb_game.basic_game.domain.models

data class LocalRoom (
    val ownerId: String,
    val opponentId: String,
    val status: Boolean,
)
