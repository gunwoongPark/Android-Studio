package kr.ac.kumoh.ce.s20160450.lol.ui.loltem

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

class LoltemFragment : Fragment() {

    companion object{
        const val NAME = "name"
        const val COST = "cost"
        const val INFO = "info"
        const val IMAGE = "image"
        const val SUBINFO = "subinfo"
        const val GRADE = "grade"
    }

    private lateinit var model: LoltemViewModel
    private val mAdapter = LoltemAdapter()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        model = ViewModelProvider(activity as AppCompatActivity).get(LoltemViewModel::class.java)

        model.list.observe(viewLifecycleOwner, Observer<ArrayList<LoltemViewModel.Loltem>> {
            mAdapter.notifyDataSetChanged()
        })

        val root = inflater.inflate(R.layout.fragment_loltem, container, false)

        val lsResult = root.findViewById<RecyclerView>(R.id.lsResult)
        lsResult.apply {
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
            adapter = mAdapter
        }

        return root
    }

    inner class LoltemAdapter: RecyclerView.Adapter<LoltemAdapter.ViewHolder>() {

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val txText1: TextView = itemView.findViewById<TextView>(R.id.skillName)
            val txText2: TextView = itemView.findViewById<TextView>(R.id.text2)

            val niImage: NetworkImageView = itemView.findViewById<NetworkImageView>(R.id.skillImage)

            init {
                niImage.setDefaultImageResId(android.R.drawable.ic_menu_report_image)
            }
        }

        override fun getItemCount(): Int {
            return model.getSize()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoltemAdapter.ViewHolder {
            val view = layoutInflater.inflate(
                R.layout.item_list,
                parent,
                false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: LoltemAdapter.ViewHolder, position: Int) {
            holder.txText1.text = model.getLoltem(position).name
            holder.txText2.text = model.getLoltem(position).cost.toString()

            holder.niImage.setImageUrl(model.getImageUrl(position), model.imageLoader)

            holder.itemView.setOnClickListener {
                val intent = Intent(holder.itemView?.context, LoltemDetailActivity::class.java)
                intent.putExtra(LoltemFragment.NAME, model.getLoltem(position).name)
                intent.putExtra(LoltemFragment.COST, model.getLoltem(position).cost)
                intent.putExtra(LoltemFragment.INFO, model.getLoltem(position).info)
                intent.putExtra(LoltemFragment.IMAGE, model.getLoltem(position).image)
                intent.putExtra(LoltemFragment.SUBINFO, model.getLoltem(position).subinfo)
                intent.putExtra(LoltemFragment.GRADE, model.getLoltem(position).grade)
                startActivity(intent)

            }
        }
    }
}