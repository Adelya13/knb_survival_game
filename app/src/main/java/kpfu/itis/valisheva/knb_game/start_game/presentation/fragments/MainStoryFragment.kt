package kpfu.itis.valisheva.knb_game.start_game.presentation.fragments

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import kpfu.itis.valisheva.knb_game.App
import kpfu.itis.valisheva.knb_game.R
import kpfu.itis.valisheva.knb_game.app_utils.AppViewModelFactory
import kpfu.itis.valisheva.knb_game.databinding.FragmentMainStoryBinding
import kpfu.itis.valisheva.knb_game.start_game.domain.StoryMessages
import kpfu.itis.valisheva.knb_game.start_game.presentation.viewmodels.CreditViewModel
import kpfu.itis.valisheva.knb_game.start_game.presentation.viewmodels.MainStoryViewModel
import javax.inject.Inject


class MainStoryFragment: Fragment(R.layout.fragment_main_story) {

    private lateinit var binding: FragmentMainStoryBinding

    @Inject
    lateinit var factory: AppViewModelFactory

    private val mainStoryViewModel: MainStoryViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (activity?.application as App).appComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainStoryBinding.bind(view)
        with(binding){
            if(tvStory.text != null){
                btnContinue.setOnClickListener {
                    tvStory.text = StoryMessages.MAIN_STORY_PART_2
                    btnContinue.setOnClickListener {
                        tvStory.text = null
                        findNavController().navigate(
                            R.id.action_mainStoryFragment_to_creditFragment
                        )
                    }
                }
            }else{
                tvStory.text = StoryMessages.MAIN_STORY_INSTRUCTIONS_PART_1
                btnContinue.setOnClickListener {
                    tvStory.text = StoryMessages.MAIN_STORY_INSTRUCTION_PART_2
                    startGame()
                    findNavController().navigate(
                        R.id.action_mainStoryFragment_to_basicGameFragment
                    )
                }
            }
        }
    }
    private fun startGame(){
        mainStoryViewModel.start()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        item.onNavDestinationSelected(findNavController())
        return super.onOptionsItemSelected(item)
    }
}
