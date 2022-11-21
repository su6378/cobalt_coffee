package com.ssafy.cobaltcoffee.config

import android.Manifest
import android.app.Application
import android.database.sqlite.SQLiteDatabase
import com.ssafy.cobaltcoffee.intercepter.AddCookiesInterceptor
import com.ssafy.smartstore.intercepter.ReceivedCookiesInterceptor
import com.ssafy.smartstore.util.SharedPreferencesUtil
import com.ssafy.cobaltcoffee.repository.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val TAG = "ApplicationClass_코발트"
class ApplicationClass : Application() {

    companion object{
        // ipconfig를 통해 ip확인하기
        // 핸드폰으로 접속은 같은 인터넷으로 연결 되어있어야함 (유,무선)
        const val SERVER_URL = "http://43.200.104.181:9999/"
        const val IMG_URL = "http://43.200.104.181/"
        const val MENU_IMGS_URL = "${IMG_URL}imgs/menu/"
        const val GRADE_URL = "${IMG_URL}imgs/grade/"
        const val IMGS_URL = "${IMG_URL}imgs/"

        lateinit var sharedPreferencesUtil: SharedPreferencesUtil
        lateinit var retrofit: Retrofit

        // 모든 퍼미션 관련 배열
        val requiredPermissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
        )

        // 주문 준비 완료 확인 시간 1분
        const val ORDER_COMPLETED_TIME = 60*1000

        // 테이블 번호
        var tableNum = -1
    }

    override fun onCreate() {
        super.onCreate()
        UserRepository.initialize(this)
        ProductRepository.initialize(this)
        OrderRepository.initialize(this)
        CartRepository.initialize(this)

        //shared preference 초기화
        sharedPreferencesUtil = SharedPreferencesUtil(applicationContext)

        // 레트로핏 인스턴스를 생성하고, 레트로핏에 각종 설정값들을 지정해줍니다.
        // 연결 타임아웃시간은 5초로 지정이 되어있고, HttpLoggingInterceptor를 붙여서 어떤 요청이 나가고 들어오는지를 보여줍니다.
        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(5000, TimeUnit.MILLISECONDS)
            .connectTimeout(5000, TimeUnit.MILLISECONDS)
            // 로그캣에 okhttp.OkHttpClient로 검색하면 http 통신 내용을 보여줍니다.
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(AddCookiesInterceptor())
            .addInterceptor(ReceivedCookiesInterceptor())
            .connectTimeout(30, TimeUnit.SECONDS).build()

        // 앱이 처음 생성되는 순간, retrofit 인스턴스를 생성
        retrofit = Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

}