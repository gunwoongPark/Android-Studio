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

    // putExtra로 넘겨줄 때, 키 값 지정
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
        // 위에서 선언한 변수에 뷰 모델을 할당
        model = ViewModelProvider(activity as AppCompatActivity).get(ChampionViewModel::class.java)

        // 리스트의 데이터 변경을 감지하고 반영
        model.list.observe(viewLifecycleOwner, Observer<ArrayList<ChampionViewModel.Champion>> {
            mAdapter.notifyDataSetChanged()
        })

        val root = inflater.inflate(R.layout.fragment_champion, container, false)

        // 리사이클러 뷰 설정
        val lsResult = root.findViewById<RecyclerView>(R.id.lsResult)
        lsResult.apply {
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
            adapter = mAdapter
        }

        return root
    }

    // 리사이클러 뷰의 어댑터
    inner class ChampionAdapter: RecyclerView.Adapter<ChampionAdapter.ViewHolder>() {

        // 어댑터의 각 아이템에 들어갈 속성들 지정
        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val txText1: TextView = itemView.findViewById<TextView>(R.id.listChampName)
            val txText2: TextView = itemView.findViewById<TextView>(R.id.listChampPosition)

            val niImage: NetworkImageView = itemView.findViewById<NetworkImageView>(R.id.listChampImage)

            // network이미지 뷰는 디폴트 값 지정해야함
            init {
                niImage.setDefaultImageResId(android.R.drawable.ic_menu_report_image)
            }
        }

        // 리스트에 올라가는 모든 요소들 수
        override fun getItemCount(): Int {
            return model.getSize()
        }

        // 최초의 화면에 출력될 약 10~11개의 리스트 초기화
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChampionAdapter.ViewHolder {
            val view = layoutInflater.inflate(
                R.layout.item_champion,
                parent,
                false)
            return ViewHolder(view)
        }

        // 새로 갱신될 요소들에 대한 데이터를 바인딩
        override fun onBindViewHolder(holder: ChampionAdapter.ViewHolder, position: Int) {
            holder.txText1.text = model.getChampion(position).name
            holder.txText2.text = model.getChampion(position).position

            holder.niImage.setImageUrl(model.getImageUrl(position), model.imageLoader)

            // 상세 뷰를 intent로 띄워주기 위한 클릭 이벤트와 넘겨주는 데이터들
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