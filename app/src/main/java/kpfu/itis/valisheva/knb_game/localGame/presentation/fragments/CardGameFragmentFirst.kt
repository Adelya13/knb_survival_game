package kpfu.itis.valisheva.knb_game.localGame.presentation.fragments

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
import kpfu.itis.valisheva.knb_game.basic_game.domain.models.Player
import kpfu.itis.valisheva.knb_game.basic_game.presentation.viewmodels.BasicGameViewModel
import kpfu.itis.valisheva.knb_game.databinding.FragmentBasicGameBinding
import kpfu.itis.valisheva.knb_game.databinding.FragmentCardGameFirstBinding
import kpfu.itis.valisheva.knb_game.localGame.presentation.services.CardService
import kpfu.itis.valisheva.knb_game.localGame.presentation.viewmodels.CardGameFirstViewModel
import javax.inject.Inject

private const val REQUEST_LOCAL_GAME_KEY = "Request"
private const val REQUEST_OPPONENT_UID_KEY = "REQUEST OPPONENT"
private const val YOUR_CARD_KEY = "CARD"
class CardGameFragmentFirst: Fragment(R.layout.fragment_card_game_first) {

    private lateinit var binding: FragmentCardGameFirstBinding
    private lateinit var cardService : CardService
    private var chosenCard : String = ""

    @Inject
    lateinit var factory: AppViewModelFactory

    private val cardGameFirstViewModel: CardGameFirstViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (activity?.application as App).appComponent.inject(this)
        activity?.onBackPressed()
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCardGameFirstBinding.bind(view)
        cardService = CardService()
        initObservers()
        if(arguments != null){
            if(arguments?.get(REQUEST_LOCAL_GAME_KEY) == true){
               showLoading()
               cardGameFirstViewModel.waitOpponent()

                with(binding){
                    btnStone.setOnClickListener{
                        chooseCardAndWaitOpponent("cntStone")
                    }
                    btnScissors.setOnClickListener{
                        chooseCardAndWaitOpponent("cntScissors")
                    }
                    btnPaper.setOnClickListener{
                        chooseCardAndWaitOpponent("cntPaper")
                    }
                }
            }
        }
    }

    private fun chooseCardAndWaitOpponent(cardName: String){
        chosenCard = cardName
        cardGameFirstViewModel.chooseCard(cardName)
        cardGameFirstViewModel.waitOpponent()
        navigateToSecondGameFragment(cardName)
    }

    private fun navigateToSecondGameFragment(card : String){
        val opponentUid =  arguments?.get(REQUEST_OPPONENT_UID_KEY).toString()
        var bundle: Bundle? = null

        bundle = Bundle().apply {
            putString(REQUEST_OPPONENT_UID_KEY,opponentUid)
            putString(YOUR_CARD_KEY, card)
        }

        val options = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .build()

        findNavController().navigate(
            R.id.action_cardGameFragmentFirst_to_cardGameSecondFragment,
            bundle,
            options
        )
    }



    private fun showLoading() {
        with(binding){
            progressBar.visibility = View.VISIBLE
            tvChoose.text = "Ожидание соперника"
            btnPaper.visibility = View.GONE
            btnScissors.visibility = View.GONE
            btnStone.visibility = View.GONE
            tvPaperCnt.visibility = View.GONE
            tvScissorsCnt.visibility = View.GONE
            tvStoneCnt.visibility = View.GONE

        }

    }

    private fun hideLoading() {
        with(binding){
            progressBar.visibility = View.GONE
            tvChoose.text = "Выбирите карту"
        }
    }

    private fun initObservers(){
        cardGameFirstViewModel.opponentStatus.observe(viewLifecycleOwner){
            it?.fold(onSuccess = {status->
                if(status){
                   cardGameFirstViewModel.showOwnInfo()
                }
                if(!status){
                    showMessage("Соперник отказался с вами играть")
                    findNavController().navigate(
                        R.id.action_cardGameFragmentFirst_to_basicGameFragment
                    )
                }

            }, onFailure = {
                Log.e("OPPONENT_NOT_FOUND", "responseToOpponent:failed", it)
            })
        }
        cardGameFirstViewModel.player.observe(viewLifecycleOwner){
            it?.fold(onSuccess = {player->
                hideLoading()
                showCardCnt(player)

            }, onFailure = {
                Log.e("AUTH_NOT_FOUND", "auth: failed", it)
            })
        }
    }

    private fun showCardCnt(player: Player){
        with(binding) {
            tvPaperCnt.text = player.cntPaper.toString()
            tvScissorsCnt.text = player.cntScissors.toString()
            tvStoneCnt.text = player.cntStone.toString()

            if(player.cntPaper != 0){
                btnPaper.visibility = View.VISIBLE
                tvPaperCnt.visibility = View.VISIBLE
            }
            if(player.cntScissors != 0 ){
                btnScissors.visibility = View.VISIBLE
                tvScissorsCnt.visibility = View.VISIBLE
            }
            if( player.cntStone != 0){
                btnStone.visibility = View.VISIBLE
                tvStoneCnt.visibility = View.VISIBLE
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
