package kpfu.itis.valisheva.knb_game.di.modules

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kpfu.itis.valisheva.knb_game.App
import kpfu.itis.valisheva.knb_game.login.presentation.fragments.SignInFragment

@Module
class AppModule {

    @Provides
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    fun bindContext(application: App): Context = application.applicationContext

}
