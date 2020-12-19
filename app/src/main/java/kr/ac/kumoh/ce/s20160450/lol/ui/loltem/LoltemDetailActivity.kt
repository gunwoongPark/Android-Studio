package kr.ac.kumoh.ce.s20160450.lol.ui.loltem

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.LruCache
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_champion_detail.*
import kotlinx.android.synthetic.main.activity_champion_detail.itemCost
import kotlinx.android.synthetic.main.activity_champion_detail.itemImage
import kotlinx.android.synthetic.main.activity_champion_detail.itemInfo
import kotlinx.android.synthetic.main.activity_champion_detail.itemName
import kotlinx.android.synthetic.main.activity_loltem_detail.*
import kr.ac.kumoh.ce.s20160450.lol.R
import kr.ac.kumoh.ce.s20160450.lol.ui.MySingleton
import kr.ac.kumoh.ce.s20160450.lol.ui.champion.ChampionFragment
import java.net.CookieHandler
import java.net.CookieManager

class LoltemDetailActivity : AppCompatActivity() {

    companion object{
        const val QUEUE_TAG = "VolleyRequest"
        val SERVER_URL = "http://192.168.0.11:8080"
    }

    lateinit var mQueue: RequestQueue
    lateinit var imageLoader: ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loltem_detail)

        title = intent.getStringExtra(LoltemFragment.NAME)

        itemName.text = intent.getStringExtra(LoltemFragment.NAME)
        itemCost.text = intent.getStringExtra(LoltemFragment.COST)
        itemInfo.text = intent.getStringExtra(LoltemFragment.INFO)
        itemSubinfo.text = intent.getStringExtra(LoltemFragment.SUBINFO)
        itemGrade.text = intent.getStringExtra(LoltemFragment.GRADE)

        val getImage = intent.getStringExtra(LoltemFragment.IMAGE)

        CookieHandler.setDefault(CookieManager())

        mQueue = MySingleton.getInstance(application).requestQueue

        imageLoader = MySingleton.getInstance(application).imageLoader

        itemImage.setImageUrl("$SERVER_URL/loltem_image/$getImage", imageLoader)

    }

    //    계속해서 QUEUE에 추가될 request들을 취소하기 위해서
    override fun onStop() {
        super.onStop()
        mQueue.cancelAll(QUEUE_TAG)
    }
}