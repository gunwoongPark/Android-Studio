package kr.ac.kumoh.ce.s20160450.lol.ui.cinematic

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

class CinematicFragment : Fragment() {
    companion object{
        const val TITLE = "title"
        const val PATH = "path"
    }

    private lateinit var model: CinematicViewModel
    private val mAdapter = CinematicAdapter()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        model = ViewModelProvider(activity as AppCompatActivity).get(CinematicViewModel::class.java)

        model.list.observe(viewLifecycleOwner, Observer<ArrayList<CinematicViewModel.Cinematic>> {
            mAdapter.notifyDataSetChanged()
        })

        val root = inflater.inflate(R.layout.fragment_cinematic, container, false)

        val lsResult = root.findViewById<RecyclerView>(R.id.lsResult)
        lsResult.apply {
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
            adapter = mAdapter
        }

        return root
    }
    
    inner class CinematicAdapter:RecyclerView.Adapter<CinematicAdapter.ViewHolder>(){
        
        inner class ViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
            val title: TextView = itemView.findViewById<TextView>(R.id.title)

            val thumbnail:NetworkImageView = itemView.findViewById<NetworkImageView>(R.id.thumbnail)

            init {
                thumbnail.setDefaultImageResId(android.R.drawable.ic_menu_report_image)
            }
        }

        override fun getItemCount(): Int {
            return model.getSize()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = layoutInflater.inflate(
                    R.layout.item_cinematic,
                    parent,
                    false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.title.text = model.getCinematic(position).title

            holder.thumbnail.setImageUrl(model.getThumbnailUrl(position), model.imageLoader)

            holder.itemView.setOnClickListener {
                val intent = Intent(holder.itemView?.context, CinematicDetailActivity::class.java)
                intent.putExtra(TITLE, model.getCinematic(position).title)
                intent.putExtra(PATH, model.getVideoUrl(position))
                startActivity(intent)
            }
        }
    }
}