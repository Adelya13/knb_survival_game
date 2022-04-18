package kpfu.itis.valisheva.knb_game.login.presentation

import android.text.TextUtils
import kpfu.itis.valisheva.knb_game.databinding.FragmentRegisterBinding
import kpfu.itis.valisheva.knb_game.databinding.FragmentSigninBinding

class LoginValidator {
    fun validateSignIn(binding: FragmentSigninBinding): Boolean {
        var valid = true

        val email = binding.etEmail.toString()
        if (TextUtils.isEmpty(email)) {
            binding.etEmail.error = "Required."
            valid = false
        } else {
            binding.etEmail.error = null
        }

        val password = binding.etPassword.toString()
        if (TextUtils.isEmpty(password)) {
            binding.etPassword.error = "Required."
            valid = false
        } else {
            binding.etPassword.error = null
        }

        return valid

    }

    fun validateRegister(binding: FragmentRegisterBinding): Boolean {
        var valid = true

        val email = binding.etEmail.toString()
        if (TextUtils.isEmpty(email)) {
            binding.etEmail.error = "Required."
            valid = false
        } else {
            binding.etEmail.error = null
        }

        val password = binding.etPassword.toString()
        if (TextUtils.isEmpty(password)) {
            binding.etPassword.error = "Required."
            valid = false
        } else {
            binding.etPassword.error = null
        }

        return valid
    }
}
