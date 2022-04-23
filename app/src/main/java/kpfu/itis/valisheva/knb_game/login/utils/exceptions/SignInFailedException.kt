package kpfu.itis.valisheva.knb_game.login.utils.exceptions

class SignInFailedException(message: String?) : RuntimeException(message) {
    override val message: String?
        get() = super.message
}
