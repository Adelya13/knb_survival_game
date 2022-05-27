package kpfu.itis.valisheva.knb_game.basic_game.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kpfu.itis.valisheva.knb_game.App
import kpfu.itis.valisheva.knb_game.R
import kpfu.itis.valisheva.knb_game.app_utils.AppViewModelFactory
import kpfu.itis.valisheva.knb_game.basic_game.presentation.viewmodels.BasicGameViewModel
import kpfu.itis.valisheva.knb_game.basic_game.presentation.viewmodels.PlayerChallengeFragmentViewModel
import kpfu.itis.valisheva.knb_game.databinding.FragmentBasicGameBinding
import kpfu.itis.valisheva.knb_game.databinding.FragmentPlayerChallengeBinding
import javax.inject.Inject

private const val RESPONSE_OPPONENT_UID_KEY = "RESPONSE OPPONENT"
private const val RESPONSE_LOCAL_GAME_KEY = "Response"

class PlayerChallengeFragment: Fragment(R.layout.fragment_player_challenge) {

    private lateinit var binding: FragmentPlayerChallengeBinding

    @Inject
    lateinit var factory: AppViewModelFactory

    private val playerChallengeFragmentViewModel: PlayerChallengeFragmentViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (activity?.application as App).appComponent.inject(this)
        activity?.onBackPressed()
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPlayerChallengeBinding.bind(view)
        initObservers()
        with(binding){
            if(arguments != null){
                if(arguments?.get(RESPONSE_LOCAL_GAME_KEY) == true){
                    val opponentUid =  arguments?.get(RESPONSE_OPPONENT_UID_KEY).toString()
                    showLoading()
                    playerChallengeFragmentViewModel
                        .findPlayerByUid(
                           opponentUid
                        )


                    btnAccept.setOnClickListener {
                        playerChallengeFragmentViewModel.setStatusTrue(opponentUid)

                        findNavController().navigate(
                            R.id.action_playerChallengeFragment_to_cardGameFragmentFirst
                        )
                    }
                    btnRefuse.setOnClickListener {
                        playerChallengeFragmentViewModel.setStatusFalse(opponentUid)

                        findNavController().navigate(
                            R.id.action_playerChallengeFragment_to_basicGameFragment
                        )
                    }
                }
            }
        }


    }


    private fun initObservers(){
        playerChallengeFragmentViewModel.player.observe(viewLifecycleOwner){
            it?.fold(onSuccess = {
                val message: String = it.name + " вызывает вас на игру"
                binding.tvMessage.text = message
                visibleStars(it.cntStar)
                hideLoading()
            }, onFailure = {
                Log.e("OPPONENT_NOT_FOUND", "responseToOpponent:failed", it)
            })
        }
    }

    private fun showLoading() {
        with(binding){
            progressBar.visibility = View.VISIBLE
            tvMessage.visibility = View.GONE
            btnRefuse.visibility = View.GONE
            btnAccept.visibility = View.GONE
            ivFirstStar.visibility = View.GONE
            ivSecondStar.visibility = View.GONE
            ivThirdStar.visibility = View.GONE

        }

    }

    private fun hideLoading() {
        with(binding){
            progressBar.visibility = View.GONE
            tvMessage.visibility = View.VISIBLE
            btnRefuse.visibility = View.VISIBLE
            btnAccept.visibility = View.VISIBLE
            ivFirstStar.visibility = View.VISIBLE
            ivSecondStar.visibility = View.VISIBLE
            ivThirdStar.visibility = View.VISIBLE
        }
    }

    private fun showMessage(message: String) {
        Snackbar.make(
            requireActivity().findViewById(R.id.container),
            message,
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun visibleStars(cntStar: Int){
        with(binding) {
            when (cntStar) {
                3 -> {
                    ivFirstStar.visibility = View.VISIBLE
                    ivSecondStar.visibility = View.VISIBLE
                    ivThirdStar.visibility = View.VISIBLE
                }
                2 -> {
                    ivFirstStar.visibility = View.VISIBLE
                    ivSecondStar.visibility = View.VISIBLE
                    ivThirdStar.visibility = View.GONE
                }
                1 -> {
                    ivFirstStar.visibility = View.VISIBLE
                    ivSecondStar.visibility = View.GONE
                    ivThirdStar.visibility = View.GONE
                }
                else -> {
                    ivFirstStar.visibility = View.GONE
                    ivSecondStar.visibility = View.GONE
                    ivThirdStar.visibility = View.GONE
                }
            }
        }
    }
}
