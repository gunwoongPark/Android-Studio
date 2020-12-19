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
    companion object{
        const val QUEUE_TAG = "VolleyRequest"
        val SERVER_URL="http://192.168.0.11:8080"
    }

    private var mQueue:RequestQueue

    data class Cinematic(var id:Int, var path:String, var title:String, var thumbnail:String)

    val list = MutableLiveData<ArrayList<Cinematic>>()
    private val cinematic = ArrayList<Cinematic>()

    val imageLoader:ImageLoader
    init {
        list.value = cinematic
        mQueue = MySingleton(application).requestQueue

        imageLoader = MySingleton(application).imageLoader

        requestCinematic()
    }

    fun getThumbnailUrl(i:Int):String = "$SERVER_URL/cinematic_image/" + URLEncoder.encode(cinematic[i].thumbnail, "utf-8")

    fun getVideoUrl(i: Int):String = "$SERVER_URL/cinematic_video/" + URLEncoder.encode(cinematic[i].path, "utf-8")

    fun getCinematic(i:Int) = cinematic[i]

    fun getSize() = cinematic.size

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

    override fun onCleared() {
        super.onCleared()
        mQueue.cancelAll(QUEUE_TAG)
    }

    private fun parseJson(items: JSONArray) {
        for (i in 0 until items.length()){
            val item: JSONObject = items[i] as JSONObject
            val id = item.getInt("id")
            val path = item.getString("path")
            val title = item.getString("title")
            val thumbnail = item.getString("thumbnail")

            cinematic.add(Cinematic(id,path,title, thumbnail))
        }
    }
}