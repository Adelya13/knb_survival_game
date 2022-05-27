package kpfu.itis.valisheva.knb_game.localGame.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kpfu.itis.valisheva.knb_game.basic_game.domain.models.Player
import kpfu.itis.valisheva.knb_game.localGame.domain.repository.LocalGamePlayersRepository
import kpfu.itis.valisheva.knb_game.login.utils.exceptions.NotFoundUserExceptions
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class LocalGamePlayersRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val dbReference: DatabaseReference,
): LocalGamePlayersRepository {


    override suspend fun waitOpponentDecision(): Boolean = suspendCoroutine { coroutine->
        dbReference
            .child("localGames")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    val status : Boolean = snapshot.child("status").value.toString().toBooleanStrict()
                    coroutine.resume(status)
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {

                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    override suspend fun showPlayerInfo(): Player {
        val user = auth.currentUser
        if(user != null) {
            return findPlayerByUuid(user.uid)
        }else{
            throw NotFoundUserExceptions("You didn't auth")
        }
    }

    override suspend fun setCard(card: String) {
        val user = auth.currentUser
        var cntCard = 0
        if(user != null) {
            dbReference
                .child("players")
                .child(user.uid)
                .get()
                .addOnSuccessListener {
                    cntCard = it.child(card).value.toString().toInt()
                    dbReference
                        .child("players")
                        .child(user.uid)
                        .child(card)
                        .setValue(cntCard-1)
                }
        }
    }

    override suspend fun waitOpponentChoose(opponentUid: String) : String{
        val player = findPlayerByUuid(opponentUid)
        return waitOpponentInLG(opponentUid,player)
    }

    private suspend fun waitOpponentInLG(opponentUid: String, player: Player) : String{
        val changedPlayer = waitOpponent(opponentUid)
        return when {
            player.cntScissors != changedPlayer.cntScissors -> "cntScissors"
            player.cntPaper != changedPlayer.cntPaper -> "cntPaper"
            player.cntStone != changedPlayer.cntStone -> "cntStone"
            else -> waitOpponentInLG(opponentUid, player)
        }
    }

    private suspend fun waitOpponent(opponentUid: String): Player = suspendCoroutine { coroutine ->
        dbReference
            .child("players")
            .child(opponentUid)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val newPlayer = Player(
                        uid = snapshot.key.toString(),
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
                    coroutine.resume(newPlayer)
                }

                override fun onCancelled(error: DatabaseError) {
                    coroutine.resumeWithException(error.toException())
                }

            })
    }

    override suspend fun setLocalGameResults(gameStatus: Boolean?, opponentUid: String) {
        val user = auth.currentUser
        if(user != null) {
           if(gameStatus == true){
               setStars(user.uid, opponentUid)
           }
            if(gameStatus == false){
                setStars(opponentUid,user.uid)
            }
            deleteRoom(user.uid)
        }
    }
    private fun deleteRoom(uid: String){
        dbReference
            .child("localGames")
            .child(uid)
            .removeValue()
    }

    private suspend fun setStars(winnerUid: String, looserUid: String){
        val winnerStars = getStarsByUid(winnerUid) + 1
        val looserStars = getStarsByUid(looserUid) - 1
        dbReference
            .child("players")
            .child(winnerUid)
            .child("cntStar")
            .setValue(winnerStars)
        dbReference
            .child("players")
            .child(looserUid)
            .child("cntStar")
            .setValue(looserStars)

    }

    private suspend fun getStarsByUid(uid: String) : Int = suspendCoroutine{ cor->
        dbReference
            .child("players")
            .child(uid)
            .get()
            .addOnSuccessListener {
                val stars = it.child("cntStar").value.toString().toInt()
                cor.resume(stars)
            }
            .addOnFailureListener{
                cor.resumeWithException(it)
            }
    }

    private suspend fun findPlayerByUuid(uid: String) : Player = suspendCoroutine {
        dbReference
            .child("players")
            .child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val player = Player(
                        uid = snapshot.key.toString(),
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
                    it.resume(player)
                }

                override fun onCancelled(error: DatabaseError) {
                    it.resumeWithException(error.toException())
                }
            })
    }
}
