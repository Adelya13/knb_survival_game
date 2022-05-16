package kpfu.itis.valisheva.knb_game.start_game.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kpfu.itis.valisheva.knb_game.App
import kpfu.itis.valisheva.knb_game.R
import kpfu.itis.valisheva.knb_game.app_utils.AppViewModelFactory
import kpfu.itis.valisheva.knb_game.databinding.FragmentProfileBinding
import kpfu.itis.valisheva.knb_game.login.presentation.viewmodels.SignInViewModel
import kpfu.itis.valisheva.knb_game.start_game.domain.models.User
import kpfu.itis.valisheva.knb_game.start_game.presentation.viewmodels.ProfileViewModel
import javax.inject.Inject


class ProfileFragment: Fragment(R.layout.fragment_profile) {
    private lateinit var user: User
    private lateinit var binding: FragmentProfileBinding

    @Inject
    lateinit var factory: AppViewModelFactory

    private val profileViewModel: ProfileViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (activity?.application as App).appComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)
        initObservers()
        profileViewModel.findUser()

        with(binding){
            btnExit.setOnClickListener{
                exit()
            }

            btnContinue.setOnClickListener {
                findNavController().navigate(
                    R.id.action_profileFragment_to_mainStoryFragment
                )
            }
        }

    }

    private fun initObservers(){
        profileViewModel.user.observe(viewLifecycleOwner){
            it?.fold(onSuccess = { user ->
                println(user.toString())
                if(user.cntPaper == null){
                    displayShortInformation(user)
                }
            },onFailure = { exception ->
                findNavController().navigate(
                    R.id.action_profileFragment_to_signInFragment
                )
                Log.e("PROFILE_FAILURE", "createUserWithEmail:failed", exception)
            })
        }
    }


    private fun exit(){
        profileViewModel.signOut()
        findNavController().navigate(
            R.id.action_profileFragment_to_signInFragment
        )
    }

    private fun displayShortInformation(user: User){
        with(binding){
            doInvisibleAdditionalInfo()
            tvNamePut.text = user.name
            tvMoneyCntPut.text = user.moneyCnt.toString()
        }
    }

    private fun doInvisibleAdditionalInfo(){
        with(binding){

            ivFirstStar.visibility = View.INVISIBLE
            ivSecondStar.visibility = View.INVISIBLE
            ivThirdStar.visibility = View.INVISIBLE
            tvChoose.visibility = View.INVISIBLE
            tvCreditPut.visibility = View.INVISIBLE
            tvCards.visibility = View.INVISIBLE
            tvPercent.visibility = View.INVISIBLE
            tvMoneyPercentPut.visibility = View.INVISIBLE
            tvScissors.visibility = View.INVISIBLE
            tvScissorsPut.visibility = View.INVISIBLE
            tvPaper.visibility = View.INVISIBLE
            tvMoneyPaperPut.visibility = View.INVISIBLE
            tvStone.visibility = View.INVISIBLE
            tvStonePut.visibility = View.INVISIBLE
        }
    }


    private fun showMessage(message: String) {
        Snackbar.make(
            requireActivity().findViewById(R.id.container),
            message,
            Snackbar.LENGTH_LONG
        ).show()
    }

}
