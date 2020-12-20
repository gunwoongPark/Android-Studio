package kr.ac.kumoh.ce.s20160450.lol.ui.cinematic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.MediaController
import com.android.volley.RequestQueue
import kotlinx.android.synthetic.main.activity_cinematic_detail.*
import kr.ac.kumoh.ce.s20160450.lol.R

class CinematicDetailActivity : AppCompatActivity() {

    lateinit var  mQueue:RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 상태바 없애기
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_cinematic_detail)


        // 동영상 경로 받아오기
        var path = intent.getStringExtra(CinematicFragment.PATH)

        // videoView에 경로로 적용
        videoView.setVideoPath(path)
        // 일시정지, 넘기기 등 가능한 컨트롤 바
        videoView.setMediaController(MediaController(this))
        // 재생
        videoView.start()
    }
}