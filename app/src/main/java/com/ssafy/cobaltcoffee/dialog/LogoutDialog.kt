package com.ssafy.cobaltcoffee.dialog

import android.app.Dialog
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.ssafy.cobaltcoffee.databinding.DialogLogoutBinding

class LogoutDialog(private val context : AppCompatActivity) {
    private lateinit var binding: DialogLogoutBinding
    private val dialog = Dialog(context)

    private lateinit var listener : LogoutDialogOKClickedListener

    fun show(content : String) {
        binding = DialogLogoutBinding.inflate(context.layoutInflater)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        dialog.setContentView(binding.root)     //다이얼로그에 사용할 xml 파일을 불러옴
        dialog.setCancelable(false)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함

        val width = (context.resources.displayMetrics.widthPixels * 0.90).toInt()
        val height = WindowManager.LayoutParams.WRAP_CONTENT

        dialog.window?.setLayout(width, height)

        binding.content.text = content //부모 액티비티에서 전달 받은 내용 세팅

        //ok 버튼 동작
        binding.okBtn.setOnClickListener {
            listener.onOKClicked()
            dialog.dismiss()
        }

        //cancel 버튼 동작
        binding.cancelBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    fun setOnOKClickedListener(listener: () -> Unit) {
        this.listener = object: LogoutDialogOKClickedListener {
            override fun onOKClicked() {
                listener()
            }
        }
    }


    interface LogoutDialogOKClickedListener {
        fun onOKClicked()
    }
}