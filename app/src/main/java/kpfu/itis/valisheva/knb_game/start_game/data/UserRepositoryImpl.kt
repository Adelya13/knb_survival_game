package kpfu.itis.valisheva.knb_game.start_game.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kpfu.itis.valisheva.knb_game.login.utils.exceptions.NotFoundUserExceptions
import kpfu.itis.valisheva.knb_game.start_game.domain.models.User
import kpfu.itis.valisheva.knb_game.start_game.domain.repositories.UserRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


private const val START_CNT_OF_CARDS = 3
private const val START_CNT_OF_STARS = 3


class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val dbReference: DatabaseReference,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): UserRepository {

    override suspend fun findUser(): User = suspendCoroutine{
        val user = auth.currentUser
        if(user !=null){
            dbReference.child("users")
                .child(user.uid)
                .addListenerForSingleValueEvent(object: ValueEventListener{
                    override fun onDataChange(dbUser: DataSnapshot) {
                            val user : User = User(
                                email = dbUser.child("email").value.toString(),
                                name = dbUser.child("name").value.toString(),
                                moneyCnt = dbUser.child("moneyCnt").value.toString().toInt(),
                                creditSum = dbUser.child("creditSum").value.toString().toIntOrNull(),
                                cntPaper = dbUser.child("cntPaper").value.toString().toIntOrNull(),
                                cntScissors = dbUser.child("cntScissors").value.toString().toIntOrNull(),
                                cntStone = dbUser.child("cntStone").value.toString().toIntOrNull(),
                                cntStar = dbUser.child("cntStar").value.toString().toIntOrNull(),
                                status = dbUser.child("status").value.toString().toBoolean()
                            )
                            it.resume(user)
                    }
                    override fun onCancelled(error: DatabaseError) {
                        it.resumeWithException(NotFoundUserExceptions(error.message))
                    }
                })

        } else{
            it.resumeWithException(NotFoundUserExceptions("User not found"))
        }

    }


    override suspend fun signOut() {
        auth.signOut()
    }

    override suspend fun getCredit(credit: Int) : Unit = suspendCoroutine{
        val user = auth.currentUser
        if(user !=null){
            dbReference.child("users")
                .child(user.uid)
                .child("creditSum")
                .setValue(credit)
        } else{
            it.resumeWithException(NotFoundUserExceptions("User not found"))
        }
    }

    override suspend fun start() {
    }
}
