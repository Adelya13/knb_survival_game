package kpfu.itis.valisheva.knb_game.basic_game.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kpfu.itis.valisheva.knb_game.basic_game.domain.models.Player
import kpfu.itis.valisheva.knb_game.basic_game.domain.repositories.PlayerRepository
import kpfu.itis.valisheva.knb_game.basic_game.utils.exceptions.ExcessNumberOfPlayersException
import kpfu.itis.valisheva.knb_game.login.domain.models.UserInfo
import kpfu.itis.valisheva.knb_game.login.utils.exceptions.NotFoundUserExceptions
import kpfu.itis.valisheva.knb_game.login.utils.exceptions.UserAlreadyRegisteredException
import kpfu.itis.valisheva.knb_game.start_game.domain.models.User
import javax.inject.Inject
import kotlin.coroutines.coroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

private const val CHILD_EL_TAG = "Child Event"

private const val PLAYERS_CNT = 2

class PlayerRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val dbReference: DatabaseReference
) : PlayerRepository {


    override suspend fun startGame() : Player = suspendCoroutine {
        val currUser = auth.currentUser
        if(currUser!=null) {
            var cnt = 0
            dbReference.child("players").get().addOnSuccessListener { data ->
                cnt = data.childrenCount.toInt()
            }
            //пока тестирую я не удаляю пользователей при выходе из игры, поэтому вычитаю 1
            if(cnt == PLAYERS_CNT-1){
                it.resumeWithException(ExcessNumberOfPlayersException("Game in progress. Please wait!"))
                //тут по хорошему создавать на сервере новую комнату, если первая заполнится,
                //однако возможность  firebase cloud function, с помощью которых можно было прописать
                //действия на сервере и отправлять fcm не доступна
            }else {
                dbReference.child("users").child(currUser.uid).get()
                    .addOnSuccessListener { dbUser ->
                        try {
                            val player = Player(
                                email = dbUser.child("email").value.toString(),
                                name = dbUser.child("name").value.toString(),
                                moneyCnt = dbUser.child("moneyCnt").value.toString().toInt(),
                                creditSum = dbUser.child("creditSum").value.toString().toInt(),
                                cntPaper = 3,
                                cntScissors = 3,
                                cntStone = 3,
                                cntStar = 3,
                                status = true
                            )
                            dbReference.child("players").child(currUser.uid).setValue(player)
                            it.resume(player)

                        } catch (e: Exception) {
                            it.resumeWithException(e)
                            println("Exception")
                        }
                    }.addOnFailureListener { ex ->
                    it.resumeWithException(NotFoundUserExceptions("Нет сети"))
                }
            }

        }else{
            it.resumeWithException(NotFoundUserExceptions("Пользователь не найден"))
        }
    }

    override suspend fun searchPlayers(): ArrayList<Player> = suspendCoroutine { coroutine ->
        val players : ArrayList<Player> = arrayListOf()
        dbReference.child("players").addChildEventListener(object: ChildEventListener{

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if(!auth.currentUser?.email.equals(snapshot.child("email").value.toString())){
                    val player = Player(
                        email = snapshot.child("email").value.toString(),
                        name = snapshot.child("name").value.toString(),
                        moneyCnt = snapshot.child("moneyCnt").value.toString().toInt(),
                        creditSum = snapshot.child("creditSum").value.toString().toInt(),
                        cntPaper = snapshot.child("cntPaper").value.toString().toInt(),
                        cntScissors = snapshot.child("cntScissors").value.toString().toInt(),
                        cntStone = snapshot.child("cntStone").value.toString().toInt(),
                        cntStar = snapshot.child("cntStar").value.toString().toInt(),
                        status = snapshot.child("status").value.toString().toBoolean()
                    )

                    players.add(player)
                    Log.d(CHILD_EL_TAG, "onChildAdded:" + snapshot.value.toString())
                }
                if( players.size + 1 == PLAYERS_CNT){
                    coroutine.resume(players)
                }
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
//                coroutine.resume()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    override suspend fun findPlayerStarsCnt(): Int = suspendCoroutine{
        val user = auth.currentUser
        if(user !=null){
            dbReference.child("players")
                .child(user.uid)
                .get()
                .addOnSuccessListener { dbPlayers ->
                    it.resume(
                        dbPlayers.child("cntStar")
                        .value
                        .toString()
                        .toInt()
                    )
                }
                .addOnFailureListener{ ex ->
                    it.resumeWithException(ex)
                }

        } else{
            it.resumeWithException(NotFoundUserExceptions("Player not found"))
        }
    }
}


