package kpfu.itis.valisheva.knb_game.localGame.domain.models

data class Room (
    val ownerId: String,
    val opponentId: String,
    val status: Boolean,
)
