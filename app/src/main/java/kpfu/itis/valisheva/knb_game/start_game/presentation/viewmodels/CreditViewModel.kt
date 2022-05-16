package kpfu.itis.valisheva.knb_game.start_game.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kpfu.itis.valisheva.knb_game.start_game.domain.models.User
import kpfu.itis.valisheva.knb_game.start_game.domain.usecases.FindUserUseCase
import kpfu.itis.valisheva.knb_game.start_game.domain.usecases.GetCreditUseCase
import javax.inject.Inject

class CreditViewModel @Inject constructor(
    private val getCreditUseCase: GetCreditUseCase,
): ViewModel() {
    private var _credit: MutableLiveData<Result<Int>> = MutableLiveData()
    val credit: LiveData<Result<Int>> = _credit

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
