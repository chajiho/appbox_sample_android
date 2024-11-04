package co.kr.appboxtest.appbox_test_and

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView

class CustomAdapter(context: Context, private val items: List<AppboxFuncCheck>) :
    ArrayAdapter<AppboxFuncCheck>(context, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item_layout, parent, false)

        val textView = view.findViewById<TextView>(R.id.itemTextView)
        val checkBox = view.findViewById<CheckBox>(R.id.itemCheckBox)
        val button = view.findViewById<Button>(R.id.itemButton)

        val item = items[position]

        textView.text = item.name
        checkBox.setOnCheckedChangeListener(null)
        checkBox.isChecked = item.check


        // 버튼 클릭 리스너 예시
        button.setOnClickListener {
            // 버튼 클릭 시 동작 정의
        }

        // 체크박스 상태 관리 (필요시 추가 구현 가능)
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            // 체크박스 체크 상태 변화 처리
            item.check = isChecked
        }


        return view
    }
}