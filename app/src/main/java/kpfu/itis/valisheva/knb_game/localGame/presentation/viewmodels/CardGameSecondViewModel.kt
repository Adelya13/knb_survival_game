package kpfu.itis.valisheva.knb_game.localGame.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kpfu.itis.valisheva.knb_game.localGame.domain.usecases.*
import javax.inject.Inject

class CardGameSecondViewModel @Inject constructor(
    private val waitOpponentCardChoose: WaitOpponentCardChoose,
    private val setLocalGameResultsUseCase: SetLocalGameResultsUseCase
) : ViewModel() {

    private var _opponentCard: MutableLiveData<Result<String>> = MutableLiveData()
    val opponentCard: LiveData<Result<String>> = _opponentCard

    fun waitOpponentCard(opponentUid: String){
        viewModelScope.launch {
            try {
                val opponentCard = waitOpponentCardChoose(opponentUid)
                _opponentCard.value = Result.success(opponentCard)

            }catch (ex: Exception){
                _opponentCard.value = Result.failure(ex)
            }
        }
    }

    fun setResult(gameStatus: Boolean?, opponentUid: String){
        viewModelScope.launch {
            try {
                setLocalGameResultsUseCase(gameStatus, opponentUid)
            }catch (ex: Exception){

            }
        }
    }

}
