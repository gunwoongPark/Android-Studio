package kr.ac.kumoh.ce.s20160450.lol.ui.champion

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.LruCache
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
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_champion_detail.*
import kr.ac.kumoh.ce.s20160450.lol.R
import java.net.CookieHandler
import java.net.CookieManager

class ChampionDetailActivity : AppCompatActivity() {

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

        title = intent.getStringExtra(ChampionFragment.NAME)

        itemName.text = intent.getStringExtra(ChampionFragment.NAME)
        itemCost.text = intent.getStringExtra(ChampionFragment.POSITION)
        itemInfo.text = intent.getStringExtra(ChampionFragment.INFO)

        val getImage = intent.getStringExtra(ChampionFragment.IMAGE)

        CookieHandler.setDefault(CookieManager())

        mQueue = Volley.newRequestQueue(this)

        imageLoader = ImageLoader(mQueue,
            object : ImageLoader.ImageCache{
                private val cache = LruCache<String, Bitmap>(100)
                override fun getBitmap(url: String): Bitmap? {
                    return cache.get(url)
                }
                override fun putBitmap(url: String, bitmap: Bitmap) {
                    cache.put(url,bitmap)
                }
            }
        )

        itemImage.setImageUrl("$SERVER_URL/champion_image/$getImage", imageLoader)



        // 여기부터 스킬

        skillList.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
            adapter = mAdapter
        }

        model = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(SkillViewModel::class.java)
        model.list.observe(this, Observer<ArrayList<SkillViewModel.Skill>>{
            mAdapter.notifyDataSetChanged()
        })
        model.requestSkill(intent.getIntExtra(ChampionFragment.ID,0))

    }

    inner class SkillAdapter:RecyclerView.Adapter<SkillAdapter.ViewHolder>(){
        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            val skillName: TextView = itemView.findViewById<TextView>(R.id.skillName)
            val skillKey : TextView = itemView.findViewById<TextView>(R.id.skillKey)
            val skillCool : TextView = itemView.findViewById<TextView>(R.id.skillCool)
            val skillCost : TextView = itemView.findViewById<TextView>(R.id.skillCost)
            val skillInfo: TextView = itemView.findViewById<TextView>(R.id.skillInfo)

            val skillImage: NetworkImageView = itemView.findViewById<NetworkImageView>(R.id.skillImage)

            init {
                skillImage.setDefaultImageResId(android.R.drawable.ic_menu_report_image)
            }
        }

        override fun getItemCount(): Int {
            return model.getSize()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkillAdapter.ViewHolder {
            val view = layoutInflater.inflate(
                    R.layout.item_skill,
                    parent,
                    false)
            return ViewHolder(view)
        }

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