package kpfu.itis.valisheva.knb_game.start_game.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kpfu.itis.valisheva.knb_game.R
import kpfu.itis.valisheva.knb_game.databinding.FragmentProfileBinding


private const val DB_URL = "https://gameofrockpaperscissors-default-rtdb.europe-west1.firebasedatabase.app/"
class ProfileFragment: Fragment(R.layout.fragment_profile) {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentProfileBinding


    val db : FirebaseDatabase  = Firebase.database(DB_URL)
    val ref = db.reference


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)
        auth = Firebase.auth
        val user = auth.currentUser
        checkUser(user)

        binding.btnContinue.setOnClickListener{
            auth.signOut()
            findNavController().navigate(
                R.id.action_profileFragment_to_signInFragment
            )
        }
    }
    private fun signOut() {
        auth.signOut()
    }
    override fun onStart() {
        super.onStart()
        val user = auth.currentUser
        checkUser(user)
    }

//    private fun getUserName(user: FirebaseUser): String? {
//        return user.displayName
//    }
//    private fun getEmail(user: FirebaseUser): String? {
//        return user.email
//    }

    private fun displayInformation(user: FirebaseUser){
        with(binding){
            tvNumber.visibility = View.INVISIBLE
            tvNumberPut.visibility = View.INVISIBLE
            ivFirstStar.visibility = View.INVISIBLE
            ivSecondStar.visibility = View.INVISIBLE
            ivThirdStar.visibility = View.INVISIBLE
            tvChoose.visibility = View.INVISIBLE
            tvCreditPut.visibility = View.INVISIBLE
            tvCards.visibility = View.INVISIBLE
            tvPercent.visibility = View.INVISIBLE
            tvMoneyPercentPut.visibility = View.INVISIBLE
            tvScissors.visibility = View.INVISIBLE
            tvScissorsPut.visibility = View.INVISIBLE
            tvPaper.visibility = View.INVISIBLE
            tvMoneyPaperPut.visibility = View.INVISIBLE
            tvStone.visibility = View.INVISIBLE
            tvStonePut.visibility = View.INVISIBLE

            ref.child("users")
                .child(user.uid)
                .get().addOnSuccessListener { user ->
                tvNamePut.text = user.child("name").value.toString()
                tvMoneyCntPut.text = user.child("moneyCnt").value.toString()
            }

        }

    }

    private fun checkUser(user: FirebaseUser?){
        if (user == null) {
            // Not signed in, launch the Sign In
            findNavController().navigate(
                R.id.action_profileFragment_to_signInFragment
            )
        } else{
            displayInformation(user)
        }
    }

}
