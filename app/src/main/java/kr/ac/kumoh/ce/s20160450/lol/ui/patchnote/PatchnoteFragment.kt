package kr.ac.kumoh.ce.s20160450.lol.ui.patchnote

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import kr.ac.kumoh.ce.s20160450.lol.R

class PatchnoteFragment : Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_patchnote, container, false)
        val webView = root.findViewById<WebView>(R.id.webView)

        // 웹 뷰에 대한 적용
        webView.apply {
            settings.javaScriptEnabled = true
            settings.blockNetworkImage = false
            settings.loadsImagesAutomatically= true
            settings.domStorageEnabled=true
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                settings.safeBrowsingEnabled = true
            }

            settings.useWideViewPort = true
            settings.loadWithOverviewMode=true
            settings.javaScriptCanOpenWindowsAutomatically = true
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
                settings.mediaPlaybackRequiresUserGesture = false
            }

            settings.domStorageEnabled = true
            settings.setSupportMultipleWindows(true)
            settings.loadWithOverviewMode = true
            settings.allowContentAccess = true
            settings.setGeolocationEnabled(true)
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN){
                settings.allowUniversalAccessFromFileURLs = true
            }

            settings.allowFileAccess = true
            webView.fitsSystemWindows=true
            webView.webViewClient = WebViewClient()
        }

        webView.loadUrl("https://kr.leagueoflegends.com/ko-kr/news/tags/patch-notes")
        return root
    }
}