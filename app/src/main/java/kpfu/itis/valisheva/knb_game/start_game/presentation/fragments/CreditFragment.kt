package kpfu.itis.valisheva.knb_game.start_game.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kpfu.itis.valisheva.knb_game.App
import kpfu.itis.valisheva.knb_game.R
import kpfu.itis.valisheva.knb_game.app_utils.AppViewModelFactory
import kpfu.itis.valisheva.knb_game.databinding.FragmentCreditBinding
import kpfu.itis.valisheva.knb_game.start_game.presentation.CreditValidator
import kpfu.itis.valisheva.knb_game.start_game.presentation.viewmodels.CreditViewModel
import kpfu.itis.valisheva.knb_game.start_game.presentation.viewmodels.ProfileViewModel
import javax.inject.Inject

private const val NAVIGATE_KEY = "Navigate"
private const val START_GAME_KEY = "Start"

class CreditFragment: Fragment(R.layout.fragment_credit) {

    private lateinit var binding: FragmentCreditBinding
    private lateinit var validator: CreditValidator


    @Inject
    lateinit var factory: AppViewModelFactory

    private val creditViewModel: CreditViewModel by viewModels {
        factory
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        (activity?.application as App).appComponent.inject(this)
        activity?.onBackPressed()
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCreditBinding.bind(view)
        validator = CreditValidator()
        initObservers()

        with(binding){
            btnRules.setOnClickListener {
                if(validator.checkCredit(inputCreditSum,etCreditSum)){
                    addCreditMoney()
                    navigateToMainStory()
                }

            }

            btnContinue.setOnClickListener {
                if(validator.checkCredit(inputCreditSum,etCreditSum)){
                    addCreditMoney()
                    navigateToBasicGame()
                }
            }
        }
    }

    private fun navigateToBasicGame(){
        var bundle: Bundle? = null

        bundle = Bundle().apply {
            putBoolean(START_GAME_KEY, true)
        }

        val options = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .build()

        findNavController().navigate(
            R.id.action_creditFragment_to_basicGameFragment,
            bundle,
            options
        )
    }

    private fun navigateToMainStory(){
        var bundle: Bundle? = null

        bundle = Bundle().apply {
            putBoolean(NAVIGATE_KEY, true)
        }

        val options = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .build()

        findNavController().navigate(
            R.id.action_creditFragment_to_mainStoryFragment,
            bundle,
            options
        )
    }

    private fun initObservers(){
        creditViewModel.credit.observe(viewLifecycleOwner){
            it?.fold(onSuccess = { credit ->
               Log.i("CREDIT_SUN","creditSum: $credit")
            },onFailure = { exception ->
                showMessage("Credit technical problems")
                Log.e("PROFILE_FAILURE", "createUserWithEmail:failed", exception)
            })
        }
    }


    private fun addCreditMoney(){
        creditViewModel.getCredit(binding.inputCreditSum.text.toString().toInt())
    }

    private fun showMessage(message: String) {
        Snackbar.make(
            requireActivity().findViewById(R.id.container),
            message,
            Snackbar.LENGTH_LONG
        ).show()
    }

}
