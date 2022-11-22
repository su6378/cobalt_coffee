package com.ssafy.cobaltcoffee.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.ssafy.cobaltcoffee.database.CartDatabase
import com.ssafy.cobaltcoffee.database.CartDto
import com.ssafy.cobaltcoffee.dto.User
import com.ssafy.cobaltcoffee.repository.CartRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartViewModel(application: Application) : AndroidViewModel(application) {

    private val readAllData : LiveData<MutableList<CartDto>>
    private val repository : CartRepository

    init {
        val cartDao = CartDatabase.initialize(application)!!.cartDao()
        repository = CartRepository(cartDao)
        readAllData = repository.realAllData
    }

    fun getCartsById(userId : String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCarts(userId)
        }
    }

    fun addCart(cart : CartDto){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertCart(cart)
        }
    }

    // ViewModel에 파라미터를 넘기기 위해서, 파라미터를 포함한 Factory 객체를 생성하기 위한 클래스
    class Factory(val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CartViewModel(application) as T
        }
    }
}