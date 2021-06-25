package com.app.dvaraediarytask.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.app.dvaraediarytask.R
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.activity_search.btnSearch
import kotlinx.android.synthetic.main.activity_search.edtMobile
import kotlinx.android.synthetic.main.activity_search.tvExit
import java.util.HashMap


class SearchActivity : AppCompatActivity() {
    private var mFirebaseReference: DatabaseReference? = null
    private var mFirebaseDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        try {
            FirebaseApp.initializeApp(this)
        } catch (e: Exception) {
        }
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mFirebaseReference = mFirebaseDatabase!!.getReference()

        btnSearch.setOnClickListener {
            validateData()
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
            mFirebaseReference!!.child("SpeedRecords").child(mMobile).orderByKey().limitToLast(1).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Log.v("DATA", "e "+error.message)
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val map = snapshot.value as HashMap<Any, Any>
                        map.entries.forEach {
                            val s = it.value as (HashMap<String, String>)
                            tvISpeed.setText(""+s.get("upspeed"))
                            tvCTime.setText(""+s.get("datetime"))
                            Log.v("DATA", ""+s)
                        }
                    }
                }
            })
        }
    }
}