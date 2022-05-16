package kpfu.itis.valisheva.knb_game.basic_game.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kpfu.itis.valisheva.knb_game.R
import kpfu.itis.valisheva.knb_game.databinding.FragmentBasicGameBinding
import kpfu.itis.valisheva.knb_game.decorators.SpaceItemDecorator

class BasicGameFragment: Fragment(R.layout.fragment_basic_game) {

    private lateinit var binding: FragmentBasicGameBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBasicGameBinding.bind(view)
        initRV()

    }
    private fun initRV(){
        //val decorator = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
        val spacing = SpaceItemDecorator(requireContext())
        binding.rvPlayers.run{
            //addItemDecoration(decorator)
            addItemDecoration(spacing)
        }
    }
}
