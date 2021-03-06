package kr.ac.kumoh.ce.s20160450.lol.ui.champion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.NetworkImageView
import kotlinx.android.synthetic.main.activity_champion_detail.*
import kr.ac.kumoh.ce.s20160450.lol.R
import kr.ac.kumoh.ce.s20160450.lol.ui.MySingleton
import java.net.CookieHandler
import java.net.CookieManager

class ChampionDetailActivity : AppCompatActivity() {
    // QUEUE 태그와 서버 URL 지정
    companion object{
        const val QUEUE_TAG = "VolleyRequest"
        val SERVER_URL = "http://192.168.0.11:8080"
    }

    lateinit var mQueue: RequestQueue
    lateinit var imageLoader: ImageLoader

    private lateinit var model: SkillViewModel
    private val mAdapter = SkillAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_champion_detail)

        // xml 파일의 아이디로 불러온 속성에 인텐트로 받은 정보를 저장 및 적용
        itemName.text = intent.getStringExtra(ChampionFragment.NAME)
        itemCost.text = intent.getStringExtra(ChampionFragment.POSITION)
        itemInfo.text = intent.getStringExtra(ChampionFragment.INFO)

        // 이미지 경로 문자열
        val getImage = intent.getStringExtra(ChampionFragment.IMAGE)

        // 쿠키 핸들러
        CookieHandler.setDefault(CookieManager())

        mQueue = MySingleton.getInstance(application).requestQueue

        imageLoader = MySingleton.getInstance(application).imageLoader

        // 서버로부터 받은 이미지 경로 문자열을 이용해 아이템 이미지 설정
        itemImage.setImageUrl("$SERVER_URL/champion_image/$getImage", imageLoader)

        // 여기부터 스킬
        // 여기부터 스킬

        // 스킬을 보여줄 리사이클러 뷰 설정
        skillList.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
            adapter = mAdapter
        }

        // 뷰 모델을 SkillViewModel로 설정
        model = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(SkillViewModel::class.java)
        // 리스트의 변경을 감지하고 반영
        model.list.observe(this, Observer<ArrayList<SkillViewModel.Skill>>{
            mAdapter.notifyDataSetChanged()
        })
        model.requestSkill(intent.getIntExtra(ChampionFragment.ID,0))

    }

    // 리사이클러 뷰의 어댑터
    inner class SkillAdapter:RecyclerView.Adapter<SkillAdapter.ViewHolder>(){

        // 어댑터의 각 아이템에 들어갈 속성들 지정
        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            val skillName: TextView = itemView.findViewById<TextView>(R.id.listChampName)
            val skillKey : TextView = itemView.findViewById<TextView>(R.id.skillKey)
            val skillCool : TextView = itemView.findViewById<TextView>(R.id.skillCool)
            val skillCost : TextView = itemView.findViewById<TextView>(R.id.skillCost)
            val skillInfo: TextView = itemView.findViewById<TextView>(R.id.skillInfo)

            val skillImage: NetworkImageView = itemView.findViewById<NetworkImageView>(R.id.listChampImage)

            // network이미지 뷰는 디폴트 값 지정해야함
            init {
                skillImage.setDefaultImageResId(android.R.drawable.ic_menu_report_image)
            }
        }

        // 리스트에 올라가는 모든 요소들 수
        override fun getItemCount(): Int {
            return model.getSize()
        }

        // 최초의 화면에 출력될 약 2개의 리스트 초기화
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkillAdapter.ViewHolder {
            val view = layoutInflater.inflate(
                    R.layout.item_skill,
                    parent,
                    false)
            return ViewHolder(view)
        }

        // 새로 갱신될 요소들에 대한 데이터를 바인딩
        override fun onBindViewHolder(holder: SkillAdapter.ViewHolder, position: Int) {
            holder.skillName.text = model.getSkill(position).name
            holder.skillKey.text = model.getSkill(position).skill_key
            holder.skillCool.text = model.getSkill(position).cool
            holder.skillCost.text = model.getSkill(position).cost
            holder.skillInfo.text = model.getSkill(position).info

            holder.skillImage.setImageUrl(model.getImageUrl(position), model.imageLoader)
        }
    }

    //    계속해서 QUEUE에 추가될 request들을 취소하기 위해서
    override fun onStop() {
        super.onStop()
        mQueue.cancelAll(QUEUE_TAG)
    }
}