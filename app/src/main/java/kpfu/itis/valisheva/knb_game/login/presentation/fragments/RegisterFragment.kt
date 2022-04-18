package kpfu.itis.valisheva.knb_game.login.presentation.fragments

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kpfu.itis.valisheva.knb_game.App
import kpfu.itis.valisheva.knb_game.R
import kpfu.itis.valisheva.knb_game.app_utils.AppViewModelFactory
import kpfu.itis.valisheva.knb_game.databinding.FragmentRegisterBinding
import kpfu.itis.valisheva.knb_game.login.presentation.LoginValidator
import kpfu.itis.valisheva.knb_game.login.presentation.viewmodels.RegisterViewModel
import kpfu.itis.valisheva.knb_game.login.presentation.viewmodels.SignInViewModel
import javax.inject.Inject

class RegisterFragment: Fragment(R.layout.fragment_register) {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var validator: LoginValidator

    @Inject
    lateinit var factory: AppViewModelFactory

    private val registerViewModel: RegisterViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (activity?.application as App).appComponent.inject(this)
        super.onCreate(savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterBinding.bind(view)
        initObservers()
        validator = LoginValidator()


        with(binding){
            btnContinue.setOnClickListener {
                val email = binding.inputEmail.text.toString()
                val password = binding.inputPassword.text.toString()
                val name = binding.inputName.text.toString()
                if (validator.validateRegister(binding)) {
                    registerViewModel.register(email, password, name)
                }

            }
            btnSignin.setOnClickListener(){
                findNavController().navigate(
                    R.id.action_registerFragment_to_signInFragment
                )
            }
        }

    }

    private fun initObservers(){
        registerViewModel.user.observe(viewLifecycleOwner){
            it?.fold(onSuccess = { userFireBase ->
                println(userFireBase.toString())
                showMessage("createUserWithEmail:success")
                findNavController().navigate(
                    R.id.action_registerFragment_to_profileFragment
                )
            },onFailure = { exception ->
                showMessage("Registration failed")
                Log.e(TAG, "createUserWithEmail:failed", exception)
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
}
