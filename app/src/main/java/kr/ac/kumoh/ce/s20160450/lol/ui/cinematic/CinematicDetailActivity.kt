package kr.ac.kumoh.ce.s20160450.lol.ui.cinematic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.MediaController
import com.android.volley.RequestQueue
import kotlinx.android.synthetic.main.activity_cinematic_detail.*
import kr.ac.kumoh.ce.s20160450.lol.R

class CinematicDetailActivity : AppCompatActivity() {

    companion object{
        const val QUEUE_TAG = "VolleyRequesta"
        val SERVER_URL = "http://172.30.1.4:8080"
    }

    lateinit var  mQueue:RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //        상태바 없애기
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_cinematic_detail)



        var path = intent.getStringExtra(CinematicFragment.PATH)

        videoView.setVideoPath(path)
        videoView.setMediaController(MediaController(this))
        videoView.start()

    //            holder.path.setVideoPath(model.getVideoUrl(position))
//            holder.path.setMediaController(MediaController(holder.path.context))
//            holder.path.pause()
    }
}