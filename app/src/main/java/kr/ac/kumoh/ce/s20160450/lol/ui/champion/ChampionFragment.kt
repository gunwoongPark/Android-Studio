package kr.ac.kumoh.ce.s20160450.lol.ui.champion

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.NetworkImageView
import kr.ac.kumoh.ce.s20160450.lol.R

class ChampionFragment : Fragment() {

    companion object{
        const val ID = "id"
        const val NAME = "name"
        const val POSITION = "position"
        const val INFO = "info"
        const val IMAGE = "image"
    }

    private lateinit var model: ChampionViewModel
    private val mAdapter = ChampionAdapter()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        model = ViewModelProvider(activity as AppCompatActivity).get(ChampionViewModel::class.java)

        model.list.observe(viewLifecycleOwner, Observer<ArrayList<ChampionViewModel.Champion>> {
            mAdapter.notifyDataSetChanged()
        })

        val root = inflater.inflate(R.layout.fragment_champion, container, false)

        val lsResult = root.findViewById<RecyclerView>(R.id.lsResult)
        lsResult.apply {
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
            adapter = mAdapter
        }

        return root
    }


    inner class ChampionAdapter: RecyclerView.Adapter<ChampionAdapter.ViewHolder>() {
        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val txText1: TextView = itemView.findViewById<TextView>(R.id.listChampName)
            val txText2: TextView = itemView.findViewById<TextView>(R.id.listChampPosition)

            val niImage: NetworkImageView = itemView.findViewById<NetworkImageView>(R.id.listChampImage)

            init {
                niImage.setDefaultImageResId(android.R.drawable.ic_menu_report_image)
            }
        }

        override fun getItemCount(): Int {
            return model.getSize()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChampionAdapter.ViewHolder {
            val view = layoutInflater.inflate(
                R.layout.item_champion,
                parent,
                false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ChampionAdapter.ViewHolder, position: Int) {
            holder.txText1.text = model.getChampion(position).name
            holder.txText2.text = model.getChampion(position).position

            holder.niImage.setImageUrl(model.getImageUrl(position), model.imageLoader)

            holder.itemView.setOnClickListener {
                val intent = Intent(holder.itemView?.context, ChampionDetailActivity::class.java)
                intent.putExtra(ID, model.getChampion(position).id)
                intent.putExtra(NAME, model.getChampion(position).name)
                intent.putExtra(POSITION, model.getChampion(position).position)
                intent.putExtra(INFO, model.getChampion(position).info)
                intent.putExtra(IMAGE, model.getChampion(position).image)
                startActivity(intent)
            }
        }
    }
}