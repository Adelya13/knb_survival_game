package kpfu.itis.valisheva.knb_game.basic_game.utils.exceptions

class NotFoundStarsException (
    message: String?
) : RuntimeException(message) {

    override val message: String?
        get() = super.message
}
