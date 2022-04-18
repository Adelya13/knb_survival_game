package kpfu.itis.valisheva.knb_game

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kpfu.itis.valisheva.knb_game.R
import kpfu.itis.valisheva.knb_game.databinding.FragmentProfileBinding
import kpfu.itis.valisheva.knb_game.databinding.FragmentRegisterBinding

class ProfileFragment: Fragment(R.layout.fragment_profile) {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentProfileBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)
        auth = Firebase.auth
        val user = auth.currentUser
        checkUser(user)
        binding.btnContinue.setOnClickListener{
            auth.signOut()
            findNavController().navigate(
                R.id.action_profileFragment_to_signInFragment
            )
        }
    }
    private fun signOut() {
        auth.signOut()
    }
    override fun onStart() {
        super.onStart()
        val user = auth.currentUser
        checkUser(user)
    }

    private fun getUserName(user: FirebaseUser): String? {
        return user.displayName
    }
    private fun getEmail(user: FirebaseUser): String? {
        return user.email
    }

    private fun displayInformation(user: FirebaseUser){
        with(binding){
            tvNamePut.text = getUserName(user)
        }

    }

    private fun checkUser(user: FirebaseUser?){
        if (user == null) {
            // Not signed in, launch the Sign In
            findNavController().navigate(
                R.id.action_profileFragment_to_signInFragment
            )
        } else{
            displayInformation(user)
        }
    }

}
