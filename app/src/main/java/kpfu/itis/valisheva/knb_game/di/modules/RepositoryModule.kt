package kpfu.itis.valisheva.knb_game.di.modules

import dagger.Binds
import dagger.Module
import kpfu.itis.valisheva.knb_game.login.data.UserRepositoryImpl
import kpfu.itis.valisheva.knb_game.login.domain.repositories.UserRepository

@Module
interface RepositoryModule {

    @Binds
    fun userRepository(
        impl: UserRepositoryImpl
    ): UserRepository
}
