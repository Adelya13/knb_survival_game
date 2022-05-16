package kpfu.itis.valisheva.knb_game.start_game.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kpfu.itis.valisheva.knb_game.start_game.domain.models.User
import kpfu.itis.valisheva.knb_game.start_game.domain.usecases.FindUserUseCase
import kpfu.itis.valisheva.knb_game.start_game.domain.usecases.GetCreditUseCase
import kpfu.itis.valisheva.knb_game.start_game.domain.usecases.StartGameUseCase
import javax.inject.Inject

class CreditViewModel @Inject constructor(
    private val getCreditUseCase: GetCreditUseCase,
    private val startGameUseCase: StartGameUseCase,
): ViewModel() {
    private var _credit: MutableLiveData<Result<Int>> = MutableLiveData()
    val credit: LiveData<Result<Int>> = _credit

    fun getCreditAndStartGame(credit: Int){
        viewModelScope.launch {
            viewModelScope.launch {
                try{
                    getCreditUseCase(credit)
                    startGameUseCase()
                }catch (ex: Exception){
                    _credit.value = Result.failure(ex)
                }
            }
        }
    }

    fun getCredit(credit: Int){
        viewModelScope.launch {
            viewModelScope.launch {
                try{
                    getCreditUseCase(credit)
                }catch (ex: Exception){
                    _credit.value = Result.failure(ex)
                }
            }
        }
    }


}
