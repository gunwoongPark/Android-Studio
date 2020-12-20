package kr.ac.kumoh.ce.s20160450.lol.ui.loltem

import android.app.Application
import android.graphics.Bitmap
import android.util.LruCache
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import kr.ac.kumoh.ce.s20160450.lol.ui.MySingleton
import org.json.JSONArray
import org.json.JSONObject
import java.net.URLEncoder

class LoltemViewModel(application: Application) : AndroidViewModel(application) {
    // QUEUE 태그와 서버 URL 지정
    companion object{
        const val QUEUE_TAG = "VolleyRequest"
        val SERVER_URL = "http://192.168.0.11:8080"
    }

    private var mQueue: RequestQueue

    // 테이블에 저장된 형식대로 데이터를 받기 위해 만든 데이터 클래스 
    data class Loltem(var id:Int, var name: String, var info:String, var image: String, var cost:Int, var subinfo:String, var grade:String)

    // MutableLiveData로 지정
    val list = MutableLiveData<ArrayList<Loltem>>()
    private val loltem = ArrayList<Loltem>()

    // 이미지 로더 선언 부분
    val imageLoader: ImageLoader
    init {
        list.value = loltem
        // 싱글톤을 활용하여 코드를 간략화
        mQueue = MySingleton.getInstance(application).requestQueue
        imageLoader = MySingleton.getInstance(application).imageLoader

        requestLoltem()
    }

    // 리스트 아이템에 출력될 이미지들의 경로를 불러오기 위함
    fun getImageUrl(i: Int): String = "$SERVER_URL/loltem_image/" + URLEncoder.encode(loltem[i].image, "utf-8")

    fun getLoltem(i:Int)=loltem[i]

    fun getSize()=loltem.size

    // 서버와 REST 통신
    fun requestLoltem(){
        val request = JsonArrayRequest(
            Request.Method.GET,
            SERVER_URL + "/loltem",
            null,
            {
                loltem.clear()
                parseJson(it)
                list.value = loltem
            },
            {
                Toast.makeText(getApplication(), it.toString(), Toast.LENGTH_LONG).show()
            }
        )

        request.tag = QUEUE_TAG
        mQueue.add(request)
    }

    // 쌓인 큐를 비워주기 위해 일종의 훅에 지정
    override fun onCleared() {
        super.onCleared()
        mQueue.cancelAll(QUEUE_TAG)
    }

    // 서버로부터 받아온 정보를 파싱하여 사용할 수 있도록 함
    private fun parseJson(items: JSONArray) {
        for (i in 0 until items.length()){
            val item: JSONObject = items[i] as JSONObject
            val id = item.getInt("id")
            val name = item.getString("name")
            val info = item.getString("info")
            val image = item.getString("image")
            val cost = item.getInt("cost")
            val subinfo = item.getString("subinfo")
            val grade = item.getString(("grade"))

            // 리스트에 저장
            loltem.add(Loltem(id,name,info,image,cost,subinfo,grade))
        }
    }

}