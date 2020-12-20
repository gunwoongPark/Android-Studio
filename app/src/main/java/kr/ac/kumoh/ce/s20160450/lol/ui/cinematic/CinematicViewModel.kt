package kr.ac.kumoh.ce.s20160450.lol.ui.cinematic

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

class CinematicViewModel(application:Application) : AndroidViewModel(application) {
    // QUEUE 태그와 서버 URL 지정
    companion object{
        const val QUEUE_TAG = "VolleyRequest"
        val SERVER_URL="http://192.168.0.11:8080"
    }

    private var mQueue:RequestQueue

    // 테이블에 저장된 형식대로 데이터를 받기 위해 만든 데이터 클래스
    data class Cinematic(var id:Int, var path:String, var title:String, var thumbnail:String)

    // MutableLiveData로 지정
    val list = MutableLiveData<ArrayList<Cinematic>>()
    private val cinematic = ArrayList<Cinematic>()

    // 이미지 로더 선언 부분
    val imageLoader:ImageLoader
    init {
        list.value = cinematic
        // 싱글톤을 활용하여 코드를 간략화
        mQueue = MySingleton(application).requestQueue
        imageLoader = MySingleton(application).imageLoader

        requestCinematic()
    }

    // 리스트 아이템에 출력될 이미지들의 경로를 불러오기 위함
    fun getThumbnailUrl(i:Int):String = "$SERVER_URL/cinematic_image/" + URLEncoder.encode(cinematic[i].thumbnail, "utf-8")

    // 디테일뷰의 비디오들의 경로를 불러오기 위함
    fun getVideoUrl(i: Int):String = "$SERVER_URL/cinematic_video/" + URLEncoder.encode(cinematic[i].path, "utf-8")

    fun getCinematic(i:Int) = cinematic[i]

    fun getSize() = cinematic.size

    // 서버와 REST 통신
    fun requestCinematic(){
        val request = JsonArrayRequest(
                Request.Method.GET,
                SERVER_URL + "/cinematic",
                null,
                {
                    cinematic.clear()
                    parseJson(it)
                    list.value = cinematic
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
            val path = item.getString("path")
            val title = item.getString("title")
            val thumbnail = item.getString("thumbnail")

            // 리스트에 저장
            cinematic.add(Cinematic(id,path,title, thumbnail))
        }
    }
}