package kpfu.itis.valisheva.knb_game.di.modules

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides

@Module
class FireBaseModule {
    @Provides
    fun provideAuth(): FirebaseAuth = Firebase.auth

    @Provides
    fun provideFirestore(): FirebaseFirestore= Firebase.firestore
}
