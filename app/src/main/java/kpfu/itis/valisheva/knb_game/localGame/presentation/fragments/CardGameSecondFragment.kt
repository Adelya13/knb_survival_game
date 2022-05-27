package kpfu.itis.valisheva.knb_game.localGame.presentation.fragments

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
import kpfu.itis.valisheva.knb_game.databinding.FragmentCardGameFirstBinding
import kpfu.itis.valisheva.knb_game.databinding.FragmentCardGameSecondBinding
import kpfu.itis.valisheva.knb_game.localGame.presentation.services.CardService
import kpfu.itis.valisheva.knb_game.localGame.presentation.viewmodels.CardGameFirstViewModel
import kpfu.itis.valisheva.knb_game.localGame.presentation.viewmodels.CardGameSecondViewModel
import javax.inject.Inject

private const val REQUEST_OPPONENT_UID_KEY = "REQUEST OPPONENT"
private const val YOUR_CARD_KEY = "CARD"

class CardGameSecondFragment: Fragment(R.layout.fragment_card_game_second) {

    private lateinit var binding: FragmentCardGameSecondBinding
    private lateinit var cardService : CardService
    private var chosenCard : String = ""
    private lateinit var opponentUid : String

    @Inject
    lateinit var factory: AppViewModelFactory

    private val cardGameSecondViewModel: CardGameSecondViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (activity?.application as App).appComponent.inject(this)
        activity?.onBackPressed()
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCardGameSecondBinding.bind(view)
        cardService = CardService()
        binding.btnContinue.visibility = View.GONE
        initObservers()
        setYourCardImg()
        waitOpponent()
    }

    private fun setYourCardImg(){
        chosenCard =  arguments?.get(YOUR_CARD_KEY).toString()
        binding.ivYourCard.setImageResource(cardService.getCardImage(chosenCard))
    }


    private fun waitOpponent(){
        opponentUid =  arguments?.get(REQUEST_OPPONENT_UID_KEY).toString()
        cardGameSecondViewModel.waitOpponentCard(opponentUid)
    }


    private fun initObservers(){

        cardGameSecondViewModel.opponentCard.observe(viewLifecycleOwner){
            it?.fold(onSuccess = {cardName->
                with(binding){
                    ivRivalCard.setImageResource(cardService.getCardImage(cardName))
                    val gameStatus = cardService.isYourCardWin(cardName, chosenCard)
                    cardGameSecondViewModel.setResult(gameStatus,opponentUid)
                    showResults(gameStatus)
                    btnContinue.visibility = View.VISIBLE
                    btnContinue.setOnClickListener {
                        findNavController().navigate(
                            R.id.action_cardGameSecondFragment_to_basicGameFragment
                        )
                    }
                }

            }, onFailure = {
                Log.e("OPPONENT_CARD_NOT_FOUND", "choose_card: failed", it)
            })
        }
    }
    private fun showResults(status: Boolean?){
        with(binding){
            if(status == true){
                tvMessage.text = "Вы выиграли!"
            } else if( status == false){
                tvMessage.text = "Вы проиграли!"
            } else{
                tvMessage.text = "Ничья"
            }
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
