package kpfu.itis.valisheva.knb_game.start_game.presentation

import android.text.TextUtils
import androidx.appcompat.widget.AppCompatEditText
import com.google.android.material.textfield.TextInputLayout

class CreditValidator {

    fun checkCredit(inputCredit: AppCompatEditText, etCredit: TextInputLayout): Boolean {
        var valid = true
        try{
            val credit = etCredit.toString()
            val creditInput = inputCredit.text.toString().toInt()
            when {
                TextUtils.isEmpty(credit) -> {
                    etCredit.error = "Возьмите кредит!"
                    valid = false
                }
                (creditInput < 1 || creditInput > 10) -> {
                    etCredit.error = "Кредит можно взять от 1 до 10, где 1-10 - это 1 млн.-10 млн. соответственно"
                    valid = false
                }
                else -> {
                    etCredit.error = null
                }
            }

        }catch (ex: NumberFormatException){
            valid = false
            etCredit.error = "Возьмите кредит от 1 до 10, где 1-10 - это 1 млн.-10 млн. соответственно"
        }
        return valid
    }
}
