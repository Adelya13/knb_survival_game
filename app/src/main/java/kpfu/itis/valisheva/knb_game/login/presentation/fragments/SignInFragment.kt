package kpfu.itis.valisheva.knb_game.login.presentation.fragments

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kpfu.itis.valisheva.knb_game.App

import kpfu.itis.valisheva.knb_game.R
import kpfu.itis.valisheva.knb_game.app_utils.AppViewModelFactory
import kpfu.itis.valisheva.knb_game.databinding.FragmentSigninBinding
import kpfu.itis.valisheva.knb_game.login.presentation.LoginValidator
import kpfu.itis.valisheva.knb_game.login.presentation.viewmodels.SignInViewModel
import javax.inject.Inject

class SignInFragment: Fragment(R.layout.fragment_signin) {

    private lateinit var binding: FragmentSigninBinding
    private lateinit var validator: LoginValidator


    @Inject
    lateinit var factory: AppViewModelFactory

    private val signInViewModel: SignInViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (activity?.application as App).appComponent.inject(this)
        super.onCreate(savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSigninBinding.bind(view)
        initObservers()
        validator = LoginValidator()

        with (binding) {
            btnContinue.setOnClickListener {
                val email = binding.inputEmail.text.toString()
                val password = binding.inputPassword.text.toString()
                if(
                    validator.checkEmail(inputEmail,etEmail) &&
                    validator.checkPassword(inputPassword,etPassword)
                ){
                    signInViewModel.signIn(email, password)
                }

            }
            btnRegister.setOnClickListener {
                findNavController().navigate(
                    R.id.action_signInFragment_to_registerFragment
                )
            }

        }
    }

    private fun initObservers(){
        signInViewModel.user.observe(viewLifecycleOwner){
            it?.fold(onSuccess = { userFireBase ->
                println(userFireBase.toString())
                findNavController().navigate(
                    R.id.action_signInFragment_to_profileFragment
                )
            },onFailure = { exception ->
                exception.message?.let { message ->
                    showMessage(message)
                }
                Log.e(TAG, "signInWithEmail:failure", exception)
            })
        }
    }

    private fun showMessage(message: String) {
        Snackbar.make(
            requireActivity().findViewById(R.id.container),
            message,
            Snackbar.LENGTH_LONG
        ).show()
    }

    companion object {
        private const val TAG = "EmailPassword"
    }
    override fun onDestroyView() {
        super.onDestroyView()
    }
}
