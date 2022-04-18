package kpfu.itis.valisheva.knb_game

import android.app.Application
import kpfu.itis.valisheva.knb_game.di.AppComponent
import kpfu.itis.valisheva.knb_game.di.DaggerAppComponent

class App: Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
            .builder()
            .application(this)
            .build()
    }
}
