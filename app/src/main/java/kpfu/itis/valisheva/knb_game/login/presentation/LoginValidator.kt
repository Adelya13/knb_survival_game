package kpfu.itis.valisheva.knb_game.login.presentation

import android.text.TextUtils
import androidx.appcompat.widget.AppCompatEditText
import com.google.android.material.textfield.TextInputLayout
import kpfu.itis.valisheva.knb_game.databinding.FragmentRegisterBinding
import kpfu.itis.valisheva.knb_game.databinding.FragmentSigninBinding

class LoginValidator {

    fun checkPassword(inputPassword: AppCompatEditText, etPassword: TextInputLayout ): Boolean{
        var valid = true
        val password = etPassword.toString()
        val inputPass = inputPassword.text.toString()
        if (TextUtils.isEmpty(password) || inputPass.length < 6) {
            etPassword.error = "Пароль должен содержать не менее 6-и символов"
            valid = false
        } else {
            //!password.contains(Regex(""))
            etPassword.error = null
        }
        return valid
    }

    fun checkEmail(inputEmail: AppCompatEditText, etEmail: TextInputLayout ): Boolean {
        var valid = true
        val email = etEmail.toString()
        when {
            TextUtils.isEmpty(email) -> {
                etEmail.error = "заполните графу email"
                valid = false
            }
            !isEmailValid(inputEmail.text.toString()) -> {
                etEmail.error = "введён некорректный email"
                valid = false
            }
            else -> {
                etEmail.error = null
            }
        }
        return valid
    }
    fun checkName(inputName: AppCompatEditText, etName: TextInputLayout): Boolean {
        var valid = true
        val name = etName.toString()
        when {
            TextUtils.isEmpty(name) -> {
                etName.error = "заполните графу Имя"
                valid = false
            }
            else -> {
                etName.error = null
            }
        }
        return valid

    }

    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}

