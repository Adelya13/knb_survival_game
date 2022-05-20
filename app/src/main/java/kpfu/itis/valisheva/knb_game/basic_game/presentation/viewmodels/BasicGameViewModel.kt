package kpfu.itis.valisheva.knb_game.basic_game.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kpfu.itis.valisheva.knb_game.basic_game.domain.models.Player
import kpfu.itis.valisheva.knb_game.basic_game.domain.usecases.FindPlayerStarsCnt
import kpfu.itis.valisheva.knb_game.basic_game.domain.usecases.SearchPlayersUseCase
import kpfu.itis.valisheva.knb_game.basic_game.domain.usecases.StartGameUseCase
import javax.inject.Inject

class BasicGameViewModel@Inject constructor(
    private val findPlayerStarsCnt: FindPlayerStarsCnt,
    private val searchPlayersUseCase: SearchPlayersUseCase,
    private val startGameUseCase: StartGameUseCase,
) : ViewModel() {

    private var _player: MutableLiveData<Result<Player>> = MutableLiveData()
    val player: LiveData<Result<Player>> = _player

    private var _starCnt: MutableLiveData<Result<Int>> = MutableLiveData()
    val starCnt: LiveData<Result<Int>> = _starCnt

//    private var _opponent: MutableLiveData<Result<Player>> = MutableLiveData()
//    val opponent: LiveData<Result<Player>> = _opponent

    private var _playerList: MutableLiveData<Result<ArrayList<Player>>> = MutableLiveData()
    val playerList: LiveData<Result<ArrayList<Player>>> = _playerList

    fun searchPlayers(){
        viewModelScope.launch {
            try{
                val playerList = searchPlayersUseCase()
                _playerList.value = Result.success(playerList)

            }catch (ex: Exception){
                _playerList.value = Result.failure(ex)
            }
        }
    }

    fun requestLocalChallenge(email: String){

    }

    fun findStarsCnt(){
        viewModelScope.launch {
            try{
                val starsCnt = findPlayerStarsCnt()
                _starCnt.value = Result.success(starsCnt)

            }catch (ex: Exception){
                _starCnt.value = Result.failure(ex)
            }
        }
    }

    fun startGame(){
        viewModelScope.launch {
            try{
                val player = startGameUseCase()
                _player.value = Result.success(player)

            }catch (ex: Exception){
                _player.value = Result.failure(ex)
            }
        }
    }
}
