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
                addListenerToUseCase(email, password, name)
            }catch (ex: Exception){
                _user.value = Result.failure(ex)
            }
        }
    }
    private fun addListenerToUseCase(email: String, password: String, name: String){
        registerUseCase(email, password, name)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = task.result.user
                    if(user !=null){
                        _user.value = Result.success(user)
                    }else{
                        _user.value = Result.failure(Exception("registerWithEmail:failure"))
                    }
                }else{
                    _user.value = Result.failure(Exception("registerWithEmail:failure"))
                    Log.w("SIGN_IN_EXCEPTION", "registerUserWithEmail:failure", task.exception)
                }
            }
    }
}
