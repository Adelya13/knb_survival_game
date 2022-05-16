package kpfu.itis.valisheva.knb_game.basic_game.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import kpfu.itis.valisheva.knb_game.basic_game.domain.models.Player
import kpfu.itis.valisheva.knb_game.basic_game.domain.repositories.PlayerRepository
import kpfu.itis.valisheva.knb_game.login.utils.exceptions.NotFoundUserExceptions
import javax.inject.Inject
import kotlin.coroutines.coroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

private const val CHILD_EL_TAG = "Child Event"

class PlayerRepositoryImpl@Inject constructor(
    private val auth: FirebaseAuth,
    private val dbReference: DatabaseReference
) : PlayerRepository {

    override suspend fun searchPlayers(): ArrayList<Player> = suspendCoroutine { coroutine ->
        val players : ArrayList<Player> = arrayListOf()
        dbReference.child("players").addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d(CHILD_EL_TAG, "onChildAdded:" + snapshot.childrenCount)
                if( snapshot.childrenCount.toInt() == 5){
                    snapshot.children.forEach {
                        players.add(it.value as Player)
                    }
                    coroutine.resume(players)
                }
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
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
//val user = auth.currentUser
//if(user !=null){
//    dbReference.child("players")
//        .child(user.uid)
//        .get()
//        .addOnSuccessListener { dbPlayers ->
//            val player  = Player(
//                email = dbPlayers.child("email").value.toString(),
//                name = dbPlayers.child("name").value.toString(),
//                moneyCnt = dbPlayers.child("moneyCnt").value.toString().toInt(),
//                creditSum = dbPlayers.child("creditSum").value.toString().toInt(),
//                cntPaper = dbPlayers.child("cntPaper").value.toString().toInt(),
//                cntScissors = dbPlayers.child("cntScissors").value.toString().toInt(),
//                cntStone = dbPlayers.child("cntStone").value.toString().toInt(),
//                cntStar = dbPlayers.child("cntStar").value.toString().toInt(),
//                status = dbPlayers.child("status").value.toString().toBoolean()
//            )
//            it.resume(player)
//        }
//        .addOnFailureListener{ ex ->
//            it.resumeWithException(ex)
//        }
//} else{
//    it.resumeWithException(NotFoundUserExceptions("Player not found"))
//}


