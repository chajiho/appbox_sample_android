package co.kr.appboxtest.appbox_test_and

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import co.kr.appboxtest.appbox_test_and.databinding.ActivityMainBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    public var funCheckList : MutableList<AppboxFuncCheck> = mutableListOf()

    val urlstr = "https://www.google.co.kr/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // 버튼 클릭 시 하단 메뉴 열기
        findViewById<Button>(R.id.showBottomSheetButton).setOnClickListener {
            showBottomSheet()
        }


        val webView = binding.webView
        webView.settings.javaScriptEnabled = true
        webView.addJavascriptInterface(WebAppInsterface(this), "androidCallAppBox")

        funCheckList.add(AppboxFuncCheck(1010, "1010", true))
        funCheckList.add(AppboxFuncCheck(1020, "1020", true))
        funCheckList.add(AppboxFuncCheck(1021, "1021", true))
        funCheckList.add(AppboxFuncCheck(1022, "1022", true))
        funCheckList.add(AppboxFuncCheck(1030, "1030", true))
        funCheckList.add(AppboxFuncCheck(1040, "1040", false))
        funCheckList.add(AppboxFuncCheck(1050, "1050", false))
        funCheckList.add(AppboxFuncCheck(1060, "1060", false))
        funCheckList.add(AppboxFuncCheck(1070, "1070", false))
        funCheckList.add(AppboxFuncCheck(1180, "1180", false))

        funCheckList.add(AppboxFuncCheck(1160, "1160", false))
        funCheckList.add(AppboxFuncCheck(1161, "1161", false))
        funCheckList.add(AppboxFuncCheck(1165, "1165", false))

        funCheckList.add(AppboxFuncCheck(1080, "1080", false))
        funCheckList.add(AppboxFuncCheck(1090, "1090", false))

        funCheckList.add(AppboxFuncCheck(1100, "1100", false))
        funCheckList.add(AppboxFuncCheck(1110, "1110", false))
        funCheckList.add(AppboxFuncCheck(1120, "1120", false))
        funCheckList.add(AppboxFuncCheck(1130, "1130", false))

        funCheckList.add(AppboxFuncCheck(1170, "1170", false))
        funCheckList.add(AppboxFuncCheck(1190, "1190", false))
        funCheckList.add(AppboxFuncCheck(1140, "1140", false))
        funCheckList.add(AppboxFuncCheck(1150, "1150", false))

        funCheckList.add(AppboxFuncCheck(2000, "1150", false))

        funCheckList.add(AppboxFuncCheck(1000, "1000", false))
        funCheckList.add(AppboxFuncCheck(1001, "1001", false))

        funCheckList.add(AppboxFuncCheck(2100, "2100", false))


        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)

                val firstJS = """
                    document.addEventListener("DOMContentLoaded", function() {
                        var script = document.createElement('script');
                        script.src="https://pk4u.kr/doc/appbox/appbox.js";
                        document.head.appendChild(script);
                        
                        var link = document.createElement('link');
                        link.rel="stylesheet";
                        link.href="https://pk4u.kr/doc/appbox/appbox.css";
                        document.head.appendChild(link);
                    });
                    """

                webView.evaluateJavascript(firstJS) { result ->
                    println("JavaScript Result: $result")
                }
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                var items: MutableList<Item> = mutableListOf()

                funCheckList.forEach {value ->
                    if(value.check == true){
                        items.add(Item(value.code, value.name, value.check))
                    }
                }

                val jsonString = convertToJson(items)


                webView.evaluateJavascript("sabi($jsonString);") { result ->
                    println("JavaScript Result: $result")
                    webView.evaluateJavascript("samCollectionLinks();", null)
                }
            }
        }

        webView.loadUrl(urlstr)


    }

    fun convertToJson(customItemList: MutableList<Item>): String {
        val jsonArray = JSONArray()

        for (item in customItemList) {
            val jsonObject = JSONObject().apply {
                put("id", item.code)
                put("name", item.name)
                put("check", item.check)
            }
            jsonArray.put(jsonObject)
        }
        return jsonArray.toString()
    }

    private fun showBottomSheet() {
        // BottomSheetDialog 생성
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.bottom_manu_layout, null)

        // 레이아웃을 BottomSheetDialog에 설정
        bottomSheetDialog.setContentView(view)

        // BottomSheetBehavior 가져오기
//        val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
//        val behavior = BottomSheetBehavior.from(bottomSheet!!)

        // BottomSheet의 높이 설정 (필요에 따라 조정)
//        behavior.peekHeight = 200 // 픽 높이 설정


        // ListView에 대한 데이터 설정
        val listView = view.findViewById<ListView>(R.id.funListView)
//        val items = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")

//        val adapter = ArrayAdapter(this, R.layout.list_item_layout, items)
        val adapter = CustomAdapter(this, funCheckList)
        listView.adapter = adapter

        // 레이아웃의 버튼에 대한 클릭 리스너 설정
        view.findViewById<Button>(R.id.actionButton).setOnClickListener {
            // 여기서 버튼 클릭 시 동작을 정의
            bottomSheetDialog.dismiss() // 닫기
        }

        // BottomSheetDialog 표시
        bottomSheetDialog.show()
    }
}

class WebAppInsterface(private val context: Context) {
    @JavascriptInterface
    fun appFunc(message: String) {
        try{
            val body = JSONObject(message)
            println("#### body : $body")

            val code = body.getInt("code")

            when(code) {
                0 -> {

                }
                else -> {

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun sysFunc(message: String) {
        try{
            val body = JSONObject(message)
            println("#### body : $body")

            val code = body.getInt("code")

            when(code) {
                0 -> {

                }
                else -> {

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

public class AppboxFuncCheck {
    var code: Int = -1
    var name: String = ""
    var check = false

    constructor(code: Int, name: String, check: Boolean) {
        this.code = code
        this.name = name
        this.check = check
    }

}


data class Item(
    val code: Int,
    val name: String,
    val check: Boolean
)