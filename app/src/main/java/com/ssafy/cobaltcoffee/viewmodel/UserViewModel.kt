package com.ssafy.cobaltcoffee.viewmodel

import androidx.lifecycle.ViewModel
import com.ssafy.cobaltcoffee.dto.User

class UserViewModel : ViewModel() {
    var currentUser = User()
}