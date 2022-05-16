package kpfu.itis.valisheva.knb_game.di.modules

import dagger.Binds
import dagger.Module
import kpfu.itis.valisheva.knb_game.login.data.UserInfoRepositoryImpl
import kpfu.itis.valisheva.knb_game.login.domain.repositories.UserInfoRepository
import kpfu.itis.valisheva.knb_game.start_game.data.UserRepositoryImpl
import kpfu.itis.valisheva.knb_game.start_game.domain.repositories.UserRepository

@Module
interface RepositoryModule {

    @Binds
    fun userInfoRepository(
        impl: UserInfoRepositoryImpl
    ): UserInfoRepository

    @Binds
    fun userRepository(
        impl: UserRepositoryImpl
    ): UserRepository

}
