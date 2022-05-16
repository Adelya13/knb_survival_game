package kpfu.itis.valisheva.knb_game.start_game.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kpfu.itis.valisheva.knb_game.start_game.domain.usecases.GetCreditUseCase
import kpfu.itis.valisheva.knb_game.start_game.domain.usecases.StartGameUseCase
import javax.inject.Inject

class MainStoryViewModel@Inject constructor(
    private val startGameUseCase: StartGameUseCase,
): ViewModel() {


    fun start(){
        viewModelScope.launch {
            viewModelScope.launch {
                try{
                    startGameUseCase()
                }catch (ex: Exception){

                }
            }
        }
    }

}
