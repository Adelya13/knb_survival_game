package kpfu.itis.valisheva.knb_game.basic_game.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kpfu.itis.valisheva.knb_game.basic_game.domain.models.LocalRoom
import kpfu.itis.valisheva.knb_game.basic_game.domain.models.Player
import kpfu.itis.valisheva.knb_game.basic_game.domain.repositories.PlayerRepository
import kpfu.itis.valisheva.knb_game.basic_game.utils.exceptions.ExcessNumberOfPlayersException
import kpfu.itis.valisheva.knb_game.basic_game.utils.exceptions.NotFoundStarsException
import kpfu.itis.valisheva.knb_game.login.utils.exceptions.NotFoundUserExceptions
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

private const val CHILD_EL_TAG = "Child Event"

private const val PLAYERS_CNT = 3

class PlayerRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val dbReference: DatabaseReference,
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
                dbReference
                    .child("users")
                    .child(currUser.uid)
                    .addListenerForSingleValueEvent(object: ValueEventListener{
                        override fun onDataChange(dbUser: DataSnapshot) {
                            try {
                                val player = Player(
                                    uid = dbUser.key.toString(),
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
                        }

                        override fun onCancelled(error: DatabaseError) {
                            it.resumeWithException(NotFoundUserExceptions("Нет сети"))
                        }

                    })
            }

        }else{
            it.resumeWithException(NotFoundUserExceptions("Пользователь не найден"))
        }
    }

    override suspend fun searchPlayers(): ArrayList<Player> = suspendCoroutine { coroutine ->
        val players : ArrayList<Player> = arrayListOf()
        dbReference
            .child("players")
            .addChildEventListener(object: ChildEventListener{

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if(!auth.currentUser?.email.equals(snapshot.child("email").value.toString())){
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

                    players.add(player)
                    Log.d(CHILD_EL_TAG, "onChildAdded:" + snapshot.value.toString())
                }
                if( players.size + 1 == PLAYERS_CNT){
                    coroutine.resume(players)
                }
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
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
                .addListenerForSingleValueEvent(object: ValueEventListener{
                    override fun onDataChange(dbPlayers: DataSnapshot) {
                        it.resume(
                            dbPlayers.child("cntStar")
                                .value
                                .toString()
                                .toInt()
                        )
                    }
                    override fun onCancelled(error: DatabaseError) {
                        it.resumeWithException(NotFoundStarsException(error.message))
                    }
                })

        } else{
            it.resumeWithException(NotFoundUserExceptions("Player not found"))
        }
    }

    override suspend fun requestLocalChallenge(uid: String) : ArrayList<Player> {
        val playersInLocalGame = arrayListOf<Player>()
        try{
            val allUidInLocalRoom = findPlayersUidInLocalGame()
            if(allUidInLocalRoom.contains(uid)){
                throw NotFoundUserExceptions("Player not found")
            }else{
                if(addNewRoom(uid)){
                    playersInLocalGame.add(findPlayerByUuid(uid))
                    playersInLocalGame.add(findPlayerByUuid(auth.currentUser?.uid.toString()))
                }
            }

        }catch(ex: NotFoundUserExceptions){
            if(addNewRoom(uid)){
                playersInLocalGame.add(findPlayerByUuid(uid))
                playersInLocalGame.add(findPlayerByUuid(auth.currentUser?.uid.toString()))
            }
        }
        return playersInLocalGame

    }

    private fun addNewRoom(uid: String) : Boolean{
        val user = auth.currentUser
        if(user !=null) {
            val room = LocalRoom(
                user.uid,
                uid,
                false
            )
            dbReference
                .child("localGames")
                .child(user.uid)
                .setValue(room)
            return true
        }
        return false
    }

    override suspend fun updateLocalChallenge(): ArrayList<Player> {
        val players = arrayListOf<Player>()
        val allUid = arrayListOf<String>()

        allUid.addAll(findPlayersUidInLocalGame())
        allUid.forEach {
            players.add(findPlayerByUuid(it))
        }
        return players
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

    override suspend fun searchYourselfInLocalGame(): String {
        return findYourselfInLocalGame()
    }

    override suspend fun findPlayerByUid(uid: String): Player {
        return findPlayerByUuid(uid)
    }

    override suspend fun setStatus(status: Boolean, uid : String) {
        dbReference
            .child("localGames")
            .child(uid)
            .child("status")
            .setValue(status)
        if(!status){
            dbReference
                .child("localGames")
                .child(uid)
                .removeValue()
        }
    }

    private suspend fun findYourselfInLocalGame() : String = suspendCoroutine{
        dbReference
            .child("localGames")
            .addChildEventListener(object : ChildEventListener{
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    if(snapshot.child("opponentUid").equals(auth.currentUser?.uid)){
                        val uid = snapshot.child("ownerUid").value.toString()
                        it.resume(uid)
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    Log.d("LOCAL_GAME_REMOVED", "onLocalGameRemoved: success")
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    private suspend fun findPlayersUidInLocalGame() : ArrayList<String> = suspendCoroutine {
        val uidList = arrayListOf<String>()
        val uidListTest = arrayListOf<String>()
        dbReference
            .child("localGames")
            .get()
            .addOnSuccessListener {uidPlayers->
                if(uidPlayers.childrenCount.toInt() != 0) {
                    uidPlayers.children.forEach { data->
                        val uid1 = data.child("ownerUid").value.toString()
                        val uid2 = data.child("opponentUid").value.toString()
                        uidListTest.add(uid1)
                        uidListTest.add(uid2)
                    }
                    uidList.addAll(uidListTest)
                    it.resume(uidList)
                }else{
                    it.resumeWithException(NotFoundUserExceptions("Player not found"))
                }
            }
            .addOnFailureListener{ex->
                it.resumeWithException(ex)
            }
    }
}
