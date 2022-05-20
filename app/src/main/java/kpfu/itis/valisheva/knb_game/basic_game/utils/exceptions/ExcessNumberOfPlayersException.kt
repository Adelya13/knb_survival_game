package kpfu.itis.valisheva.knb_game.basic_game.utils.exceptions

class ExcessNumberOfPlayersException(
    message: String?
) : RuntimeException(message) {

    override val message: String?
        get() = super.message
}
