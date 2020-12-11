package kr.ac.kumoh.ce.s20160450.lol.ui.champion

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.LruCache
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_champion_detail.*
import kr.ac.kumoh.ce.s20160450.lol.R
import java.net.CookieHandler
import java.net.CookieManager

class ChampionDetailActivity : AppCompatActivity() {

    companion object{
        const val QUEUE_TAG = "VolleyRequest"
        val SERVER_URL = "http://192.168.0.123:8080"
    }

    lateinit var mQueue: RequestQueue
    lateinit var imageLoader: ImageLoader

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
    }

    //    계속해서 QUEUE에 추가될 request들을 취소하기 위해서
    override fun onStop() {
        super.onStop()
        mQueue.cancelAll(QUEUE_TAG)
    }
}