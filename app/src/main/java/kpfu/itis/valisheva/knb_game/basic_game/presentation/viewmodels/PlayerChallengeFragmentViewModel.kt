package kpfu.itis.valisheva.knb_game.basic_game.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kpfu.itis.valisheva.knb_game.basic_game.domain.models.Player
import kpfu.itis.valisheva.knb_game.basic_game.domain.usecases.FindPlayerByUidUseCase
import kpfu.itis.valisheva.knb_game.basic_game.domain.usecases.SetLocalGameStatusUseCase
import javax.inject.Inject

class PlayerChallengeFragmentViewModel @Inject constructor(
    private val findPlayerByUidUseCase: FindPlayerByUidUseCase,
    private val setLocalGameStatusUseCase: SetLocalGameStatusUseCase
) : ViewModel() {

    private var _player: MutableLiveData<Result<Player>> = MutableLiveData()
    val player: LiveData<Result<Player>> = _player

    fun sendRequest(uid: String){

    }

    fun setStatusTrue(uid: String){
        viewModelScope.launch {
            try {
                setLocalGameStatusUseCase(true, uid)

            }catch (ex: Exception){
                _player.value = Result.failure(ex)
            }
        }
    }
    fun setStatusFalse(uid: String){
        viewModelScope.launch {
            try {
                setLocalGameStatusUseCase(false, uid)

            }catch (ex: Exception){
                _player.value = Result.failure(ex)
            }
        }
    }

    fun findPlayerByUid(uid: String){
        viewModelScope.launch {
            try {
                val opponent = findPlayerByUidUseCase(uid)
                _player.value = Result.success(opponent)

            }catch (ex: Exception){
               _player.value = Result.failure(ex)
            }
        }
    }
}
