package com.ssafy.cobaltcoffee.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.ssafy.cobaltcoffee.dto.User


private const val TAG = "UserService_코발트"
class UserService : Service() {

    private lateinit var database: FirebaseDatabase
    private lateinit var myRef : DatabaseReference
    var isDuplicate = false

    override fun onCreate() {
        super.onCreate()
        database = Firebase.database("https://cobaltcoffee-61052-default-rtdb.asia-southeast1.firebasedatabase.app")
        myRef = database.getReference("user")
    }
    override fun onBind(intent: Intent?): IBinder? {
        return mBinder
    }

    //유저 등록
    fun insert(user: User) {
        val id = user.id.replace('.',',')
        myRef.child(id).setValue(user)
    }

    //아이디 중복 체크
    fun duplicateCheck(id: String): Boolean {
        var userId = ""
        if (id.contains("."))  userId = id.replace('.',',')

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    isDuplicate = userId == snapshot.key
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                
            }
        })
       return isDuplicate
    }
//
//    //물품 조회
//    fun selectAllMask(): ArrayList<Mask> {
//        return maskDao.selectAllMask()
//    }
//
//    //물품정보 변경
//    fun updateMask(mask: Mask): Int {
//        return maskDao.updateMask(mask)
//    }
//
//    //물품 삭제
//    fun deleteMask(id: Int): Int {
//        return maskDao.deleteMask(id)
//    }

    private val mBinder: IBinder = MyLocalBinder()

    inner class MyLocalBinder : Binder(){
        fun getService() : UserService = this@UserService
    }
}