package kpfu.itis.valisheva.knb_game.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import kpfu.itis.valisheva.knb_game.R
import kpfu.itis.valisheva.knb_game.databinding.ActivityMainBinding
import android.content.Intent
import androidx.navigation.findNavController
import kpfu.itis.valisheva.knb_game.start_game.presentation.fragments.ProfileFragment


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var controller: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also{
            setContentView(it.root)
        }
        controller = findController(R.id.container)
    }

    override fun onBackPressed() {

    }
}
