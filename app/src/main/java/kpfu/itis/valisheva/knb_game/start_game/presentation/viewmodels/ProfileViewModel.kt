package kpfu.itis.valisheva.knb_game.start_game.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kpfu.itis.valisheva.knb_game.start_game.domain.models.User
import kpfu.itis.valisheva.knb_game.start_game.domain.usecases.FindUserUseCase
import kpfu.itis.valisheva.knb_game.start_game.domain.usecases.SignOutUseCase
import javax.inject.Inject

class ProfileViewModel  @Inject constructor(
    private val findUserUseCase: FindUserUseCase,
    private val signOutUseCase: SignOutUseCase,
) : ViewModel() {

    private var _user: MutableLiveData<Result<User>> = MutableLiveData()
    val user: LiveData<Result<User>> = _user


    fun findUser(){
        viewModelScope.launch {
            try{
                val user = findUserUseCase()
                _user.value = Result.success(user)

            }catch (ex: Exception){
                _user.value = Result.failure(ex)
            }
        }
    }

    fun signOut(){
        viewModelScope.launch {
            try{
                signOutUseCase()
            }catch (ex: Exception){

            }
        }
    }
}
