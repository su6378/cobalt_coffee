package com.ssafy.cobaltcoffee.repository

import android.content.Context
import android.util.Log
import com.ssafy.cobaltcoffee.dto.User
import com.ssafy.cobaltcoffee.util.RetrofitCallback
import com.ssafy.cobaltcoffee.util.RetrofitUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "UserRepository_코발트"
class UserRepository(context: Context) {
    //로그인
    fun login(user: User, callback: RetrofitCallback<User>)  {
        RetrofitUtil.userService.login(user).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                val res = response.body()
                if(response.code() == 200){
                    if (res != null) {
                        callback.onSuccess(response.code(), res)
                    }
                } else {
                    callback.onFailure(response.code())
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                callback.onError(t)
            }
        })
    }
    //회원가입
    fun insert(user : User, callback: RetrofitCallback<Boolean>){
        RetrofitUtil.userService.insert(user).enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                val res = response.body()
                if(response.code() == 200){
                    if (res != null) {
                        callback.onSuccess(response.code(), res)
                    }
                } else {
                    callback.onFailure(response.code())
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                callback.onError(t)
            }
        })
    }
    //아이디 중복체크
    fun duplicateCheck(id : String, callback: RetrofitCallback<Boolean>){
        RetrofitUtil.userService.isUsedId(id).enqueue(object : Callback<Boolean>{
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                val res = response.body()
                if(response.code() == 200){
                    if (res != null) {
                        callback.onSuccess(response.code(), res)
                    }
                } else {
                    callback.onFailure(response.code())
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                callback.onError(t)
            }

        })
    }
    //회원정보 조회
    fun getInfo(id : String, callback: RetrofitCallback<HashMap<String, Any>>){
        RetrofitUtil.userService.getInfo(id).enqueue(object : Callback<HashMap<String,Any>>{
            override fun onResponse(call: Call<HashMap<String, Any>>, response: Response<HashMap<String, Any>>) {
                val res = response.body()
                Log.d(TAG, "onResponse: $res")
                if(response.code() == 200){
                    if (res != null) {
                        callback.onSuccess(response.code(), res)
                    }
                } else {
                    callback.onFailure(response.code())
                }
            }

            override fun onFailure(call: Call<HashMap<String, Any>>, t: Throwable) {
                callback.onError(t)
            }
        })
    }

    //회원정보 수정
    fun update(user : User, callback: RetrofitCallback<Boolean>){
        RetrofitUtil.userService.update(user).enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                val res = response.body()
                if(response.code() == 200){
                    if (res != null) {
                        callback.onSuccess(response.code(), res)
                    }
                } else {
                    callback.onFailure(response.code())
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                callback.onError(t)
            }
        })
    }

    // singleton pattern
    companion object {
        private var INSTANCE : UserRepository? =null

        fun initialize(context: Context){
            if(INSTANCE == null){
                INSTANCE = UserRepository(context)
            }
        }

        fun get() : UserRepository {
            return INSTANCE ?:
            throw IllegalStateException("UserRepository must be initialized")
        }
    }
}