package kr.ac.kumoh.ce.s20160450.lol.ui.champion

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
import org.json.JSONArray
import org.json.JSONObject
import java.net.URLEncoder

class ChampionViewModel(application: Application) : AndroidViewModel(application) {
    companion object{
        const val QUEUE_TAG = "VolleyRequest"
        val SERVER_URL = "http://192.168.0.123:8080"
    }

    private var mQueue: RequestQueue

    data class Champion(var id:Int, var name: String, var position:String, var image: String, var info:String)

    val list = MutableLiveData<ArrayList<Champion>>()
    private val champion = ArrayList<Champion>()

    val imageLoader: ImageLoader
    init {
        list.value = champion
        mQueue = Volley.newRequestQueue(application)

        imageLoader = ImageLoader(mQueue,
            object : ImageLoader.ImageCache {
                private val cache = LruCache<String, Bitmap>(100)
                override fun getBitmap(url: String): Bitmap? {
                    return cache.get(url)
                }
                override fun putBitmap(url: String, bitmap: Bitmap) {
                    cache.put(url, bitmap)
                }
            })

        requestChampion()
    }

    fun getImageUrl(i: Int): String = "$SERVER_URL/champion_image/" + URLEncoder.encode(champion[i].image, "utf-8")

    fun getChampion(i:Int)=champion[i]

    fun getSize()=champion.size

    fun requestChampion(){

        val request = JsonArrayRequest(
            Request.Method.GET,
            SERVER_URL + "/champion",
            null,
            {
                champion.clear()
                parseJson(it)
                list.value = champion
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
            val name = item.getString("name")
            val position = item.getString("position")
            val image = item.getString("image")
            val info = item.getString("info")

            champion.add(Champion(id,name,position,image,info))
        }
    }

}