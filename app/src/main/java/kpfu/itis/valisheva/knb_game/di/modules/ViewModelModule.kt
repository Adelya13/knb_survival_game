package kpfu.itis.valisheva.knb_game.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kpfu.itis.valisheva.knb_game.app_utils.AppViewModelFactory
import kpfu.itis.valisheva.knb_game.di.ViewModelKey
import kpfu.itis.valisheva.knb_game.login.presentation.viewmodels.RegisterViewModel
import kpfu.itis.valisheva.knb_game.login.presentation.viewmodels.SignInViewModel
import kpfu.itis.valisheva.knb_game.start_game.presentation.viewmodels.CreditViewModel
import kpfu.itis.valisheva.knb_game.start_game.presentation.viewmodels.MainStoryViewModel
import kpfu.itis.valisheva.knb_game.start_game.presentation.viewmodels.ProfileViewModel

@Module
interface ViewModelModule {

    @Binds
    fun bindViewModelFactory(
        factory: AppViewModelFactory
    ): ViewModelProvider.Factory


    @Binds
    @IntoMap
    @ViewModelKey(SignInViewModel::class)
    fun bindSignInViewModel(
        viewModel: SignInViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    fun bindRegisterViewModel(
        viewModel: RegisterViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    fun bindProfileViewModel(
        viewModel: ProfileViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainStoryViewModel::class)
    fun bindMainStoryViewModel(
        viewModel: MainStoryViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreditViewModel::class)
    fun bindCreditViewModel(
        viewModel: CreditViewModel
    ): ViewModel


}
