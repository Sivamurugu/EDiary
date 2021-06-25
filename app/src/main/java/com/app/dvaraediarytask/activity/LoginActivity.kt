package com.app.dvaraediarytask.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.app.dvaraediarytask.R
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnSubmit.setOnClickListener {
          validateData()
        }
        btnSearch.setOnClickListener {
            startActivity(Intent(this,SearchActivity::class.java))
        }
        tvExit.setOnClickListener {
            finish()
        }
    }

    private fun validateData() {
        val mMobile=edtMobile.text.toString().trim()
        if(TextUtils.isEmpty(mMobile)){
            edtMobile!!.error="Required"
        } else if(mMobile.length!=10){
            edtMobile!!.error="Required 10 Digit Mobile No"
        }else {
            startActivity(Intent(this, MainActivity::class.java).putExtra("MOB",mMobile))
            finish()
        }
    }
}