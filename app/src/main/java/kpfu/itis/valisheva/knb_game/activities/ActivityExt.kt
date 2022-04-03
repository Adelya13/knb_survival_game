package kpfu.itis.valisheva.knb_game.activities

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment

fun AppCompatActivity.findController(
    id : Int
) = (supportFragmentManager.findFragmentById(id) as NavHostFragment)
    .navController
