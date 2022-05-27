package kpfu.itis.valisheva.knb_game.di

import android.content.Context
import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Component
import kpfu.itis.valisheva.knb_game.App
import kpfu.itis.valisheva.knb_game.basic_game.presentation.fragments.BasicGameFragment
import kpfu.itis.valisheva.knb_game.basic_game.presentation.fragments.PlayerChallengeFragment
import kpfu.itis.valisheva.knb_game.di.modules.AppModule
import kpfu.itis.valisheva.knb_game.di.modules.FireBaseModule
import kpfu.itis.valisheva.knb_game.di.modules.RepositoryModule
import kpfu.itis.valisheva.knb_game.di.modules.ViewModelModule
import kpfu.itis.valisheva.knb_game.localGame.presentation.fragments.CardGameFragmentFirst
import kpfu.itis.valisheva.knb_game.localGame.presentation.fragments.CardGameSecondFragment
import kpfu.itis.valisheva.knb_game.login.presentation.fragments.RegisterFragment
import kpfu.itis.valisheva.knb_game.login.presentation.fragments.SignInFragment
import kpfu.itis.valisheva.knb_game.start_game.presentation.fragments.CreditFragment
import kpfu.itis.valisheva.knb_game.start_game.presentation.fragments.MainStoryFragment
import kpfu.itis.valisheva.knb_game.start_game.presentation.fragments.ProfileFragment

@Component(
    modules = [
        AppModule::class,
        FireBaseModule::class,
        RepositoryModule::class,
        ViewModelModule::class,
    ]
)
interface AppComponent {

    fun provideApp(): Context

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: App): Builder

        fun build(): AppComponent
    }

    fun inject(signInFragment: SignInFragment)
    fun inject(registerFragment: RegisterFragment)
    fun inject(profileFragment: ProfileFragment)
    fun inject(mainStoryFragment: MainStoryFragment)
    fun inject(creditFragment: CreditFragment)
    fun inject(basicGameFragment: BasicGameFragment)
    fun inject(playerChallengeFragment: PlayerChallengeFragment)
    fun inject(cardGameFragmentFirst: CardGameFragmentFirst)
    fun inject(cardGameFragmentSecond: CardGameSecondFragment)
}
