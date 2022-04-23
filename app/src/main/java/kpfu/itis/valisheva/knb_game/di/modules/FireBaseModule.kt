package kpfu.itis.valisheva.knb_game.di.modules

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides



private const val DB_URL = "https://gameofrockpaperscissors-default-rtdb.europe-west1.firebasedatabase.app/"

@Module
class FireBaseModule {
    @Provides
    fun provideAuth(): FirebaseAuth = Firebase.auth

    @Provides
    fun provideFirestore(): FirebaseFirestore= Firebase.firestore

    @Provides
    fun provideRealtimeDB(): FirebaseDatabase = Firebase.database(DB_URL)

    @Provides
    fun provideReference(db: FirebaseDatabase): DatabaseReference = db.reference
}
