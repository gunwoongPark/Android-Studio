package kr.ac.kumoh.ce.s20160450.lol.ui.notifications

import android.graphics.Bitmap
import android.media.MediaPlayer
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.ac.kumoh.ce.s20160450.lol.R

class CinematicFragment : Fragment() {

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
            val path : VideoView = itemView.findViewById<VideoView>(R.id.videoView)
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
            holder.path.setVideoPath(model.getVideoUrl(position))
            holder.path.setMediaController(MediaController(holder.path.context))
            holder.path.pause()
        }
    }
}