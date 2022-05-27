package kpfu.itis.valisheva.knb_game.localGame.presentation.services

import kpfu.itis.valisheva.knb_game.R

private const val STONE = "cntStone"
private const val SCISSORS = "cntScissors"
private const val PAPER = "cntPaper"


class CardService() {
    fun isYourCardWin(opponentCard : String, yourCard: String) : Boolean? {
        if(opponentCard == STONE){
            if(yourCard == SCISSORS) return false
            if(yourCard == PAPER) return true
        } else if(opponentCard == SCISSORS){
            if(yourCard == PAPER) return false
            if(yourCard == STONE) return true
        } else if(opponentCard == PAPER){
            if(yourCard == STONE) return false
            if(yourCard == SCISSORS) return true
        }
        return null
    }
    fun getCardImage(card: String) : Int{
        if(card == STONE) return R.mipmap.ic_stone_card
        if(card == SCISSORS) return R.mipmap.ic_scissors_card
        if(card == PAPER) return R.mipmap.ic_paper_card
        return -1
    }
}
