package com.app.dvaraediarytask.activity

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.dvaraediarytask.R
import com.app.dvaraediarytask.model.SpeedData
import com.app.dvaraediarytask.utils.Utils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    var handler: Handler?= null
    var runnable: Runnable?= null
    private var mFirebaseReference: DatabaseReference? = null
    private var mFirebaseDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null
    var mUpSpeed=""
    var mMobileNo=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        handler=Handler()
        try {
            FirebaseApp.initializeApp(this)
        } catch (e: Exception) {
        }
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mFirebaseReference = mFirebaseDatabase!!.getReference()

        mMobileNo=intent.getStringExtra("MOB")!!
        tvWelcome.setText("Welcome "+mMobileNo)

        runnable= Runnable {
            showData()
            handler!!.postDelayed(runnable!!,1000)
        }
        handler!!.postDelayed(runnable!!,2000)

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
        if(Utils.isOnline(this)){
            val timestamp=""+System.currentTimeMillis()
            val data=SpeedData(timestamp,Utils.getCurrentDateTime(),timestamp,mUpSpeed,mMobileNo)
            saveData(data)
        }else{
            Toast.makeText(this,"Your in Offline!",Toast.LENGTH_LONG).show()
        }
    }

    private fun showData() {
        if (Utils.isOnline(this)) {
            tvIStatus.setText("Connected Internet!")
            tvIStatus.setTextColor(resources.getColor(R.color.colorGreen))
            tvISpeed.setText(getSpeed())
            tvCTime.setText(Utils.getCurrentDateTime())
        } else {
            tvIStatus.setText("Could not connected to Intenet!")
            tvIStatus.setTextColor(resources.getColor(R.color.colorRed))
            tvISpeed.setText("-")
            tvCTime.setText(Utils.getCurrentDateTime())
        }
    }

    private fun getSpeed(): String {
        // Connectivity Manager
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // Network Capabilities of Active Network
        var nc: NetworkCapabilities? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            nc = cm.getNetworkCapabilities(cm.activeNetwork)
        } else {

        }

        // DownSpeed in MBPS
        val downSpeed = (nc!!.linkDownstreamBandwidthKbps) / 1000

        // UpSpeed  in MBPS
        val upSpeed = (nc!!.linkUpstreamBandwidthKbps) / 1000
        mUpSpeed=""+upSpeed+" Mbps"
        // Toast to Display DownSpeed and UpSpeed
        return "Up Speed: $upSpeed Mbps \nDown Speed: $downSpeed Mbps"
    }

    override fun onDestroy() {
        super.onDestroy()
        removeCallback()
    }

    private fun removeCallback(){
        try {
            if(runnable!=null){
                handler!!.removeCallbacks(runnable!!)
            }
        } catch (e: Exception) {
        }
    }

    private fun saveData(data:SpeedData) {
        mFirebaseReference!!.child("SpeedRecords").child(data.mobileno).child(data.id).setValue(data).addOnSuccessListener(object :
            OnSuccessListener<Void> {
            override fun onSuccess(p0: Void?) {
                Toast.makeText(this@MainActivity,"Successfully Saved!",Toast.LENGTH_LONG).show()
            }
        }).addOnFailureListener(object : OnFailureListener {
            override fun onFailure(p0: java.lang.Exception) {
                Toast.makeText(this@MainActivity,"Record Not Saved!",Toast.LENGTH_LONG).show()
            }
        })
    }
}