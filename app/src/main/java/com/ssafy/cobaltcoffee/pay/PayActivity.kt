package com.ssafy.cobaltcoffee.pay

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.ssafy.cobaltcoffee.R
import com.ssafy.cobaltcoffee.adapter.CartAdapter
import com.ssafy.cobaltcoffee.adapter.PayAdapter
import com.ssafy.cobaltcoffee.database.CartDto
import com.ssafy.cobaltcoffee.databinding.ActivityPayBinding
import com.ssafy.cobaltcoffee.dto.Order
import com.ssafy.cobaltcoffee.dto.OrderDetail
import com.ssafy.cobaltcoffee.dto.User
import com.ssafy.cobaltcoffee.repository.OrderRepository
import com.ssafy.cobaltcoffee.util.CommonUtils
import com.ssafy.cobaltcoffee.util.RetrofitCallback
import com.ssafy.cobaltcoffee.viewmodel.CartViewModel
import com.ssafy.cobaltcoffee.viewmodel.UserViewModel
import kr.co.bootpay.android.Bootpay
import kr.co.bootpay.android.constants.BootpayBuildConfig
import kr.co.bootpay.android.events.BootpayEventListener
import kr.co.bootpay.android.models.BootExtra
import kr.co.bootpay.android.models.BootItem
import kr.co.bootpay.android.models.BootUser
import kr.co.bootpay.android.models.Payload

private const val TAG = "PayActivity_코발트"
class PayActivity : AppCompatActivity() {
    var applicationId = "61c51513e38c300022d2d173"
    //    var applicationId = "5b8f6a4d396fa665fdc2b5e8" test

    private lateinit var binding: ActivityPayBinding

    private val userViewModel : UserViewModel by viewModels()
    private var totalCount = 0
    private var totalPrice = 0

    private lateinit var payAdapter: PayAdapter
    private lateinit var cartList : MutableList<CartDto>
    private var checkInsert = false

    private lateinit var cartViewModel: CartViewModel

    private lateinit var orderDetailList : MutableList<OrderDetail>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (BootpayBuildConfig.DEBUG) {
            applicationId = "5b8f6a4d396fa665fdc2b5e8"
        }

        init()

    }

    private fun init(){

        userViewModel.currentUser = intent.getSerializableExtra("user") as User
        totalCount = intent.getIntExtra("totalCount",0)
        
        // 뷰모델 연결
        cartViewModel = ViewModelProvider(this, CartViewModel.Factory(application)).get(CartViewModel::class.java)
        
        initAdapter()

        binding.apply {

            payUserName.text = userViewModel.currentUser.name //사용자 닉네임
            payUserEmail.text = userViewModel.currentUser.id //사용자 아이디

        }

        initTb()

        binding.payBtn.setOnClickListener {
            PaymentTest(binding.root)
            
        }
    }

    //툴바 적용하기
    private fun initTb() {
        binding.apply {
            setSupportActionBar(payToolBar.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
            payToolBar.toolbarTitle.text = "결제하기"
        }
    }

    //뒤로가기 버튼 클릭 시
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initAdapter(){
        //장바구니 상품가져오기
        cartList = mutableListOf()
        cartViewModel.readAllData.observe(this@PayActivity) {
            if (!checkInsert){
                for(cart in it){
                    //room에 저장되어있는 장바구니 품목에서 해당 아이디만 해당하는 주문 목록을 리스트에 추가
                    if(cart.userId == userViewModel.currentUser.id){
                        cartList.add(cart)
                        totalCount += cart.quantity
                        totalPrice += cart.totalPrice
                    }
                }
                binding.payTotalMoney.text = CommonUtils.makeComma(totalPrice)
                checkInsert = true
            }

            binding.apply {
                payAdapter = PayAdapter(cartList)

                payRv.apply {
                    layoutManager = LinearLayoutManager(this@PayActivity, LinearLayoutManager.VERTICAL, false)
                    adapter = payAdapter
                    //원래의 목록위치로 돌아오게함
                    adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                }
            }
        }
    }

    private fun makeOrder() {
        // 주문 정보 생성
        orderDetailList = mutableListOf()

        for (cart in cartList) {
            orderDetailList.add(OrderDetail(0,0,cart.productId,cart.quantity))
        }

        val order = Order()
        order.userId = userViewModel.currentUser.id
        order.orderTable = "cobalt_table"
        order.details = orderDetailList
        order.completed = 'N'

        // 레트로핏 주문 넣기
        OrderRepository.get().makeOrder(order,MakeOrderCallback())

    }

    inner class MakeOrderCallback: RetrofitCallback<Boolean> {
        override fun onSuccess( code: Int, result: Boolean) {
            if (result) {

            }else{
                Snackbar.make(binding.root,"회원가입에 실패했습니다.", Snackbar.LENGTH_SHORT).show()
            }
        }

        override fun onError(t: Throwable) {
            Log.d(TAG, t.message ?: "서버 통신오류")
        }

        override fun onFailure(code: Int) {
            Log.d(TAG, "onResponse: Error Code $code")
        }
    }

    fun PaymentTest(v: View?) {
        val extra = BootExtra()
            .setCardQuota("0,2,3").setOfferPeriod("구매일로부터 3개월") // 일시불, 2개월, 3개월 할부 허용, 할부는 최대 12개월까지 사용됨 (5만원 이상 구매시 할부허용 범위)
        val items: MutableList<BootItem> = ArrayList()
        val item1 = BootItem().setName("마우's 스").setId("ITEM_CODE_MOUSE").setQty(1).setPrice(500.0)
        val item2 = BootItem().setName("키보드").setId("ITEM_KEYBOARD_MOUSE").setQty(1).setPrice(500.0)
        items.add(item1)
        items.add(item2)
        val payload = Payload()

        payload.setApplicationId(applicationId)
            .setOrderName("부트페이 결제테스트")
            .setPg("나이스페이")
            .setMethod("카드")
            .setOrderId("1234")
            .setPrice(1000.0)
            .setUser(getBootUser())
            .setExtra(extra).items = items

        val map: MutableMap<String, Any> = HashMap()
        map["1"] = "abcdef"
        map["2"] = "abcdef55"
        map["3"] = 1234
        payload.metadata = map
        //        payload.setMetadata(new Gson().toJson(map));
        Bootpay.init(supportFragmentManager, applicationContext)
            .setPayload(payload)
            .setEventListener(object : BootpayEventListener {
                override fun onCancel(data: String) {
                    Log.d("bootpay", "cancel: $data")
                }

                override fun onError(data: String) {
                    Log.d("bootpay", "error: $data")
                }

                override fun onClose() {
                    Bootpay.removePaymentWindow()
                }

                override fun onIssued(data: String) {
                    Log.d("bootpay", "issued: $data")
                }

                override fun onConfirm(data: String): Boolean {
                    Log.d("bootpay", "confirm: $data")
                    //                        Bootpay.transactionConfirm(data); //재고가 있어서 결제를 진행하려 할때 true (방법 1)
                    return true //재고가 있어서 결제를 진행하려 할때 true (방법 2)
                    //                        return false; //결제를 진행하지 않을때 false
                }

                override fun onDone(data: String) { //결제 완료시 해당 유저의 장바구니 목록(내부DB)은 삭제
                    cartViewModel.clearCart(userViewModel.currentUser.id)
                    makeOrder()
                    val intent = Intent(this@PayActivity,PayDoneActivity::class.java)
                    intent.putExtra("user",userViewModel.currentUser)
                    intent.putExtra("totalCount",totalCount)
                    intent.putExtra("totalPrice",totalPrice)
                    startActivity(intent)
                }
            }).requestPayment()
    }

    fun getBootUser(): BootUser? {
        val userId = "1234114ss121"
        val user = BootUser()
        user.id = userId
        user.area = "서울"
        user.gender = 1 //1: 남자, 0: 여자
        user.email = "test1234@gmail.com"
        user.phone = "010-1234-4567"
        user.birth = "1988-06-10"
        user.username = "홍길동"
        return user
    }
}