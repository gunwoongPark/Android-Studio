package kr.ac.kumoh.ce.s20160450.lol.ui.champion

import android.app.Application
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

class SkillViewModel (application: Application):AndroidViewModel(application){
    companion object{
        const val QUEUE_TAG = "VolleyRequest"
        val SERVER_URL = "http://192.168.0.11:8080"
    }

    private var mQueue:RequestQueue
    data class Skill(var id:Int, var champ_id:Int, var skill_key:String, var image:String, var info:String, var cost:String, var cool:String, var name:String)

    var list = MutableLiveData<ArrayList<Skill>>()
    private val skill = ArrayList<Skill>()

    val imageLoader:ImageLoader
    init {
        list.value = skill
        mQueue = MySingleton.getInstance(application).requestQueue
        imageLoader= MySingleton.getInstance(application).imageLoader
    }

    fun getImageUrl(i: Int): String = "$SERVER_URL/skill_image/" + URLEncoder.encode(skill[i].image, "utf-8")
    fun getSkill(i:Int)=skill[i]
    fun getSize()=skill.size

    fun requestSkill(id:Int){
        val request = JsonArrayRequest(
                Request.Method.GET,
                SERVER_URL+"/skills?id=$id",
                null,
                {
                    skill.clear()
                    parseJson(it)
                    list.value = skill
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

    private fun parseJson(items:JSONArray){
       for(i in 0 until items.length()){
           val item: JSONObject = items[i] as JSONObject
           val id = item.getInt("id")
           val champ_id = item.getInt("champ_id")
           val skill_key = item.getString("skill_key")
           val image = item.getString("image")
           val info = item.getString("info")
           val cost = item.getString("cost")
           val cool = item.getString("cool")
           val name = item.getString("name")

           skill.add(Skill(id, champ_id, skill_key, image, info, cost, cool, name))
       }
    }
}