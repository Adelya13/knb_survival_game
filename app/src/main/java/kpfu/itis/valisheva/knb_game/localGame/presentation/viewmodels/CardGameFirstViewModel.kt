package kpfu.itis.valisheva.knb_game.localGame.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kpfu.itis.valisheva.knb_game.basic_game.domain.models.Player
import kpfu.itis.valisheva.knb_game.localGame.domain.usecases.SetCardUseCase
import kpfu.itis.valisheva.knb_game.localGame.domain.usecases.ShowOwnInformation
import kpfu.itis.valisheva.knb_game.localGame.domain.usecases.WaitOpponentCardChoose
import kpfu.itis.valisheva.knb_game.localGame.domain.usecases.WaitOpponentDecisionUseCase
import javax.inject.Inject

class CardGameFirstViewModel  @Inject constructor(
    private val waitOpponentDecisionUseCase: WaitOpponentDecisionUseCase,
    private val showOwnInformation: ShowOwnInformation,
    private val setCardUseCase: SetCardUseCase,
) : ViewModel() {

    private var _opponentStatus: MutableLiveData<Result<Boolean>> = MutableLiveData()
    val opponentStatus: LiveData<Result<Boolean>> = _opponentStatus

//    private var _opponentCard: MutableLiveData<Result<String>> = MutableLiveData()
//    val opponentCard: LiveData<Result<String>> = _opponentCard


    private var _player: MutableLiveData<Result<Player>> = MutableLiveData()
    val player: LiveData<Result<Player>> = _player



    fun chooseCard(cardName: String){
        viewModelScope.launch {
            try {
                setCardUseCase(cardName)

            }catch (ex: Exception){

            }
        }
    }

    fun showOwnInfo(){
        viewModelScope.launch {
            try {
                val player = showOwnInformation()
                _player.value = Result.success(player)

            }catch (ex: Exception){
                _player.value = Result.failure(ex)
            }
        }
    }

    fun waitOpponent(){
        viewModelScope.launch {
            try {
                val status = waitOpponentDecisionUseCase()
                _opponentStatus.value = Result.success(status)

            }catch (ex: Exception){
                _opponentStatus.value = Result.failure(ex)
            }
        }
    }
}
