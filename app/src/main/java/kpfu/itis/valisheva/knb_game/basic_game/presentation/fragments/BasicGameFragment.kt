package kpfu.itis.valisheva.knb_game.basic_game.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBasicGameBinding.bind(view)
        initRV()
        initObservers()
        if(binding.rvPlayers.adapter == null) {
            waitPlayers()
        }
    }

    private fun initObservers(){
        basicGameViewModel.playerList.observe(viewLifecycleOwner){ it ->
            it?.fold(onSuccess = { playerList ->
                playerList.forEach { player ->
                    println(player.toString())
                }
                basicGameViewModel.findStarsCnt()
                initAdapter(playerList)
                hideLoading()
            },onFailure = { exception ->
                showMessage("Server Problem")
                Log.e("PLAYER_FAILURE", "searchPlayers:failed", exception)
            })
        }
        basicGameViewModel.starCnt.observe(viewLifecycleOwner){ it ->
            it?.fold(onSuccess = { cnt ->
                visibleStars(cnt)
            },onFailure = { exception ->
                showMessage("Server Problem")
                Log.e("PLAYER_STARS_FAILURE", "findPlayerStars:failed", exception)
            })
        }

    }

    private fun waitPlayers(){
        showLoading()
        basicGameViewModel.searchPlayers()
    }

    private fun initAdapter(players: ArrayList<Player>){
        playerAdapter = PlayerAdapter(players){
            requestLocalChallenge(it)
        }
        binding.rvPlayers.apply{
            adapter = playerAdapter
        }
    }

    private fun requestLocalChallenge(email: String){
        basicGameViewModel.requestLocalChallenge(email)
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
            rvPlayers.visibility = View.INVISIBLE
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
            rvPlayers.visibility = View.VISIBLE
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
                    ivThirdStar.visibility = View.INVISIBLE
                }
                1 -> {
                    ivFirstStar.visibility = View.VISIBLE
                    ivSecondStar.visibility = View.INVISIBLE
                    ivThirdStar.visibility = View.INVISIBLE
                }
                else -> {
                    ivFirstStar.visibility = View.INVISIBLE
                    ivSecondStar.visibility = View.INVISIBLE
                    ivThirdStar.visibility = View.INVISIBLE
                }
            }
        }
    }
}
