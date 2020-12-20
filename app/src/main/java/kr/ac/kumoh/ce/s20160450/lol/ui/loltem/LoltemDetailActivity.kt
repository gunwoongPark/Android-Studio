package kr.ac.kumoh.ce.s20160450.lol.ui.loltem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import kotlinx.android.synthetic.main.activity_champion_detail.itemCost
import kotlinx.android.synthetic.main.activity_champion_detail.itemImage
import kotlinx.android.synthetic.main.activity_champion_detail.itemInfo
import kotlinx.android.synthetic.main.activity_champion_detail.itemName
import kotlinx.android.synthetic.main.activity_loltem_detail.*
import kr.ac.kumoh.ce.s20160450.lol.R
import kr.ac.kumoh.ce.s20160450.lol.ui.MySingleton
import java.net.CookieHandler
import java.net.CookieManager

class LoltemDetailActivity : AppCompatActivity() {
    // QUEUE 태그와 서버 URL 지정
    companion object{
        const val QUEUE_TAG = "VolleyRequest"
        val SERVER_URL = "http://192.168.0.11:8080"
    }

    lateinit var mQueue: RequestQueue
    lateinit var imageLoader: ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loltem_detail)

        // xml 파일의 아이디로 불러온 속성에 인텐트로 받은 정보를 저장 및 적용
        itemName.text = intent.getStringExtra(LoltemFragment.NAME)
        itemCost.text = intent.getStringExtra(LoltemFragment.COST)
        itemInfo.text = intent.getStringExtra(LoltemFragment.INFO)
        itemSubinfo.text = intent.getStringExtra(LoltemFragment.SUBINFO)
        itemGrade.text = intent.getStringExtra(LoltemFragment.GRADE)

        // 이미지 경로 문자열
        val getImage = intent.getStringExtra(LoltemFragment.IMAGE)

        // 쿠키 핸들러
        CookieHandler.setDefault(CookieManager())

        mQueue = MySingleton.getInstance(application).requestQueue

        imageLoader = MySingleton.getInstance(application).imageLoader

        // 서버로부터 받은 이미지 경로 문자열을 이용해 아이템 이미지 설정
        itemImage.setImageUrl("$SERVER_URL/loltem_image/$getImage", imageLoader)

    }

    //    계속해서 QUEUE에 추가될 request들을 취소하기 위해서
    override fun onStop() {
        super.onStop()
        mQueue.cancelAll(QUEUE_TAG)
    }
}