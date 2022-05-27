package kpfu.itis.valisheva.knb_game.basic_game.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kpfu.itis.valisheva.knb_game.basic_game.domain.models.Player
import kpfu.itis.valisheva.knb_game.basic_game.domain.usecases.*
import kpfu.itis.valisheva.knb_game.login.utils.exceptions.NotFoundUserExceptions
import javax.inject.Inject

class BasicGameViewModel@Inject constructor(
    private val findPlayerStarsCntUseCase: FindPlayerStarsCntUseCase,
    private val searchPlayersUseCase: SearchPlayersUseCase,
    private val startGameUseCase: StartGameUseCase,
    private val requestLocalChallengeUseCase: RequestLocalChallengeUseCase,
    private val updatePlayersInLocalGame: UpdatePlayersInLocalGame,
    private val searchYourselfInLocalGameUseCase: SearchYourselfInLocalGameUseCase
) : ViewModel() {

    private var _player: MutableLiveData<Result<Player>> = MutableLiveData()
    val player: LiveData<Result<Player>> = _player

    private var _starCnt: MutableLiveData<Result<Int>> = MutableLiveData()
    val starCnt: LiveData<Result<Int>> = _starCnt

    private var _opponentUid: MutableLiveData<Result<String>> = MutableLiveData()
    val opponentUid: LiveData<Result<String>> = _opponentUid

    private var _playerList: MutableLiveData<Result<ArrayList<Player>>> = MutableLiveData()
    val playerList: LiveData<Result<ArrayList<Player>>> = _playerList

    private var _playersInGameList: MutableLiveData<Result<ArrayList<Player>>> = MutableLiveData()
    val playersInGameList: LiveData<Result<ArrayList<Player>>> = _playersInGameList



    fun searchYourselfInLocalGame(){
        viewModelScope.launch {
            try {
                val opponent = searchYourselfInLocalGameUseCase()
                _opponentUid.value = Result.success(opponent)

            }catch (ex: Exception){
                _opponentUid.value = Result.failure(ex)
            }
        }
    }

    fun searchPlayers(){
        viewModelScope.launch {
            try{
                val playerList = searchPlayersUseCase()
                val newPlayerList = updatePlayersInLocalGame()
                playerList.removeAll(newPlayerList)
                _playerList.value = Result.success(playerList)

            }catch (ex1: NotFoundUserExceptions){
                val playerList = searchPlayersUseCase()
                _playerList.value = Result.success(playerList)
            }catch (ex: Exception){
                _playerList.value = Result.failure(ex)
            }
        }
    }

    fun requestLocalChallenge(uid: String){
        viewModelScope.launch {
            try{
                val newPlayerList = requestLocalChallengeUseCase(uid)
                _playersInGameList.value = Result.success(newPlayerList)
                _playerList.value?.onSuccess {
                    it.removeAll(newPlayerList)
                    _playerList.value = Result.success(it)
                }

            }catch (ex: Exception){
                _playersInGameList.value = Result.failure(ex)
            }
        }
    }

    fun findStarsCnt(){
        viewModelScope.launch {
            try{
                val starsCnt = findPlayerStarsCntUseCase()
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
