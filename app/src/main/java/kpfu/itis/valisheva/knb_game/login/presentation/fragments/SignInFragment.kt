package kpfu.itis.valisheva.knb_game.login.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kpfu.itis.valisheva.knb_game.R
import kpfu.itis.valisheva.knb_game.databinding.FragmentRegisterBinding
import kpfu.itis.valisheva.knb_game.databinding.FragmentSigninBinding

class SignInFragment: Fragment(R.layout.fragment_signin) {
    private lateinit var binding: FragmentSigninBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSigninBinding.bind(view)
        binding.btnRegister.setOnClickListener(){
            findNavController().navigate(
                R.id.action_signInFragment_to_registerFragment
            )
        }
        binding.btnContinue.setOnClickListener(){
            findNavController().navigate(
                R.id.action_signInFragment_to_mainStoryFragment
            )
        }
    }
}
