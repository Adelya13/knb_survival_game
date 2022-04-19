package kpfu.itis.valisheva.knb_game.login.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import kpfu.itis.valisheva.knb_game.login.domain.usecases.RegisterUseCase
import javax.inject.Inject

class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
): ViewModel() {

    private var _user: MutableLiveData<Result<FirebaseUser>> = MutableLiveData()
    val user: LiveData<Result<FirebaseUser>> = _user


    fun register(email: String, password: String, name: String) {
        viewModelScope.launch {
            try{
                val user = registerUseCase(email, password,name)
                _user.value = Result.success(user)

            }catch (ex: Exception){
                _user.value = Result.failure(ex)
            }
        }
    }

}
