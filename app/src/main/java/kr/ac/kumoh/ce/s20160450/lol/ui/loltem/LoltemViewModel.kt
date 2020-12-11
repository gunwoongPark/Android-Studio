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
import org.json.JSONArray
import org.json.JSONObject
import java.net.URLEncoder

class LoltemViewModel(application: Application) : AndroidViewModel(application) {
    companion object{
        const val QUEUE_TAG = "VolleyRequest"
        val SERVER_URL = "http://192.168.0.123:8080"
    }

    private var mQueue: RequestQueue

    data class Loltem(var id:Int, var name: String, var info:String, var image: String, var cost:Int, var subinfo:String, var grade:String)

    val list = MutableLiveData<ArrayList<Loltem>>()
    private val loltem = ArrayList<Loltem>()

    val imageLoader: ImageLoader
    init {
        list.value = loltem
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

        requestLoltem()
    }

    fun getImageUrl(i: Int): String = "$SERVER_URL/loltem_image/" + URLEncoder.encode(loltem[i].image, "utf-8")

    fun getLoltem(i:Int)=loltem[i]

    fun getSize()=loltem.size

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

    override fun onCleared() {
        super.onCleared()
        mQueue.cancelAll(QUEUE_TAG)
    }

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

            loltem.add(Loltem(id,name,info,image,cost,subinfo,grade))
        }
    }

}