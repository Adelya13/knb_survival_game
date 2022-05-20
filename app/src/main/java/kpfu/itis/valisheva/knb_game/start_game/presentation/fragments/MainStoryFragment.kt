package kpfu.itis.valisheva.knb_game.start_game.presentation.fragments

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
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

private const val NAVIGATE_KEY = "Navigate"
private const val START_GAME_KEY = "Start"

class MainStoryFragment: Fragment(R.layout.fragment_main_story) {

    private lateinit var binding: FragmentMainStoryBinding

    @Inject
    lateinit var factory: AppViewModelFactory

    private val mainStoryViewModel: MainStoryViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (activity?.application as App).appComponent.inject(this)
        activity?.onBackPressed()
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainStoryBinding.bind(view)


        with(binding){
            if(arguments == null){
                btnContinue.setOnClickListener {
                    scrollText.scrollTo(0,0)
                    tvStory.text = StoryMessages.MAIN_STORY_PART_2
                    btnContinue.setOnClickListener {
                        findNavController().navigate(
                            R.id.action_mainStoryFragment_to_creditFragment
                        )
                    }
                }
            }else if(arguments?.get(NAVIGATE_KEY)==true){
                tvStory.text = StoryMessages.MAIN_STORY_INSTRUCTIONS_PART_1
                btnContinue.setOnClickListener {
                    scrollText.scrollTo(0,0)
                    tvStory.text = StoryMessages.MAIN_STORY_INSTRUCTION_PART_2
                    btnContinue.setOnClickListener {
                       navigateToBasicGame()
                    }
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        item.onNavDestinationSelected(findNavController())
        return super.onOptionsItemSelected(item)
    }
}
