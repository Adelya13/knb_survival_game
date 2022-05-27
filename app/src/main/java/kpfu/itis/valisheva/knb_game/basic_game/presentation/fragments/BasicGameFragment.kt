package kpfu.itis.valisheva.knb_game.basic_game.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kpfu.itis.valisheva.knb_game.App
import kpfu.itis.valisheva.knb_game.R
import kpfu.itis.valisheva.knb_game.app_utils.AppViewModelFactory
import kpfu.itis.valisheva.knb_game.basic_game.domain.models.Player
import kpfu.itis.valisheva.knb_game.basic_game.presentation.rv.PlayerAdapter
import kpfu.itis.valisheva.knb_game.basic_game.presentation.viewmodels.BasicGameViewModel
import kpfu.itis.valisheva.knb_game.databinding.FragmentBasicGameBinding
import kpfu.itis.valisheva.knb_game.decorators.SpaceItemDecorator
import javax.inject.Inject

private const val START_GAME_KEY = "Start"
private const val REQUEST_LOCAL_GAME_KEY = "Request"
private const val REQUEST_OPPONENT_UID_KEY = "REQUEST OPPONENT"
private const val RESPONSE_OPPONENT_UID_KEY = "RESPONSE OPPONENT"
private const val RESPONSE_LOCAL_GAME_KEY = "Response"



class BasicGameFragment: Fragment(R.layout.fragment_basic_game) {

    private lateinit var binding: FragmentBasicGameBinding
    private lateinit var playerAdapter: PlayerAdapter

    @Inject
    lateinit var factory: AppViewModelFactory

    private val basicGameViewModel: BasicGameViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (activity?.application as App).appComponent.inject(this)
        activity?.onBackPressed()
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBasicGameBinding.bind(view)
        initRV()
        initObservers()
        basicGameViewModel.findStarsCnt()
        if(arguments != null){
            if(arguments?.get(START_GAME_KEY) == true){
                basicGameViewModel.startGame()
            }
        }
        if(binding.rvPlayers.adapter == null) {
            waitPlayers()
        }
    }

    private fun initObservers(){
        basicGameViewModel.playerList.observe(viewLifecycleOwner){ it ->
            it?.fold(onSuccess = { playerList ->
                initAdapter(playerList)
                if(binding.progressBar.isVisible){
                    hideLoading()
                }

            },onFailure = { exception ->
                showMessage("Server Problem")
                Log.e("PLAYER_FAILURE", "searchPlayers:failed", exception)
            })
        }
        basicGameViewModel.starCnt.observe(viewLifecycleOwner){ it ->
            it?.fold(onSuccess = { cnt ->
                visibleStars(cnt)
                basicGameViewModel.searchYourselfInLocalGame()
            },onFailure = { exception ->
                showMessage("Server Problem")
                Log.e("PLAYER_STARS_FAILURE", "findPlayerStars:failed", exception)
            })
        }
        basicGameViewModel.player.observe(viewLifecycleOwner){
            it?.fold(onSuccess = { player ->
                basicGameViewModel.searchYourselfInLocalGame()
            },onFailure = { exception ->
                showMessage("Player not inter to game")
                Log.e("PLAYER_START_FAILURE", "startGame:failed", exception)
            })
        }
        basicGameViewModel.playersInGameList.observe(viewLifecycleOwner){
            it?.fold(onSuccess = { players ->
                basicGameViewModel.searchYourselfInLocalGame()
            },onFailure = { exception ->
                showMessage("Player not inter to game")
                Log.e("PLAYER_START_FAILURE", "startGame:failed", exception)
            })
        }
        basicGameViewModel.opponentUid.observe(viewLifecycleOwner){
            it?.fold(onSuccess = {
                responseNavigation(it)
            }, onFailure = {
                Log.e("OPPONENT_NOT_FOUND", "responseToOpponent:failed", it)
            })
        }

    }

    private fun waitPlayers(){
        showLoading()
        basicGameViewModel.searchPlayers()
        basicGameViewModel.searchYourselfInLocalGame()
    }

    private fun initAdapter(players: ArrayList<Player>){

        playerAdapter = PlayerAdapter(players){
            requestLocalChallenge(it)
        }
        binding.rvPlayers.apply {
            adapter = playerAdapter
        }
    }

    private fun responseNavigation(uid: String){
        var bundle: Bundle? = null

        bundle = Bundle().apply {
            putBoolean(RESPONSE_LOCAL_GAME_KEY, true)
                putString(RESPONSE_OPPONENT_UID_KEY,uid)
        }

        val options = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .build()

        findNavController().navigate(
            R.id.action_basicGameFragment_to_playerChallengeFragment,
            bundle,
            options
        )
    }



    private fun requestLocalChallenge(uid: String){
        basicGameViewModel.requestLocalChallenge(uid)
        requestNavigation(uid)
    }
    private fun requestNavigation(uid: String){
        var bundle: Bundle? = null

        bundle = Bundle().apply {
            putBoolean(REQUEST_LOCAL_GAME_KEY, true)
            putString(REQUEST_OPPONENT_UID_KEY,uid)
        }

        val options = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .build()

        findNavController().navigate(
            R.id.action_basicGameFragment_to_cardGameFragmentFirst,
            bundle,
            options
        )
    }

    private fun initRV(){
        val spacing = SpaceItemDecorator(requireContext())
        binding.rvPlayers.run{
            addItemDecoration(spacing)
        }
    }

    private fun showLoading() {
        with(binding){
            progressBar.visibility = View.VISIBLE
            ivFirstStar.visibility = View.INVISIBLE
            ivSecondStar.visibility = View.INVISIBLE
            ivThirdStar.visibility = View.INVISIBLE
            tvTitle.visibility = View.INVISIBLE
            tvChooseTitle.visibility = View.INVISIBLE
        }

    }

    private fun hideLoading() {
        with(binding){
            progressBar.visibility = View.GONE
            tvTitle.visibility = View.VISIBLE
            tvChooseTitle.visibility = View.VISIBLE
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
