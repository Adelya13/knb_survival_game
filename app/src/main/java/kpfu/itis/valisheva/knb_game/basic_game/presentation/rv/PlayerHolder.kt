package kpfu.itis.valisheva.knb_game.basic_game.presentation.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kpfu.itis.valisheva.knb_game.basic_game.domain.models.Player
import kpfu.itis.valisheva.knb_game.databinding.ItemPlayerBinding

class PlayerHolder (
    private val binding: ItemPlayerBinding,
    private val action: (String) -> Unit
): RecyclerView.ViewHolder(binding.root){


    fun bind(item: Player) {
        with(binding) {
            tvName.text = item.name
        }

        visibleStars(item.cntStar)

        itemView.setOnClickListener {
            action(item.uid)
        }
    }


    private fun visibleStars(cntStar: Int){
        with(binding) {
            when (cntStar) {
                3 -> {
                    ivFirstStar.visibility = View.VISIBLE
                    ivSecondStar.visibility = View.VISIBLE
                    ivThirdStar.visibility = View.VISIBLE
                }
                2 -> {
                    ivFirstStar.visibility = View.VISIBLE
                    ivSecondStar.visibility = View.VISIBLE
                    ivThirdStar.visibility = View.INVISIBLE
                }
                1 -> {
                    ivFirstStar.visibility = View.VISIBLE
                    ivSecondStar.visibility = View.INVISIBLE
                    ivThirdStar.visibility = View.INVISIBLE
                }
                else -> {
                    ivFirstStar.visibility = View.INVISIBLE
                    ivSecondStar.visibility = View.INVISIBLE
                    ivThirdStar.visibility = View.INVISIBLE
                }
            }
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            action: (String) -> Unit
        ) = PlayerHolder(
            ItemPlayerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            action
        )
    }

}
