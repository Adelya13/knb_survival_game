package kpfu.itis.valisheva.knb_game.login.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kpfu.itis.valisheva.knb_game.login.domain.usecases.SignInUseCase
import okhttp3.internal.wait
import javax.inject.Inject

class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase
): ViewModel() {

    private var _user: MutableLiveData<Result<FirebaseUser>> = MutableLiveData()
    val user: LiveData<Result<FirebaseUser>> = _user


    fun signIn(email: String, password : String){
        viewModelScope.launch {
            try{
               val user = signInUseCase(email, password)
                _user.value = Result.success(user)

            }catch (ex: Exception){
                _user.value = Result.failure(ex)
            }
        }
    }
}
