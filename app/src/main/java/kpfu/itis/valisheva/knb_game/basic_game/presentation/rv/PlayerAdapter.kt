package kpfu.itis.valisheva.knb_game.basic_game.presentation.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kpfu.itis.valisheva.knb_game.basic_game.domain.models.Player
import kpfu.itis.valisheva.knb_game.databinding.ItemPlayerBinding

class PlayerAdapter(
    private val list: List<Player>,
    private val itemClick: (String) -> (Unit)
) : RecyclerView.Adapter<PlayerHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerHolder {
        return PlayerHolder.create(parent, itemClick)
    }

    override fun onBindViewHolder(holder: PlayerHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}


