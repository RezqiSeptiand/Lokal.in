package com.example.lokalin.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.data.Repository
import com.example.data.response.RegisterResponse
import com.example.utils.ResultState

class SignUpViewModel(private val repository: Repository) : ViewModel() {
    private val _users = MutableLiveData<ResultState<RegisterResponse>?>()
    val users: LiveData<ResultState<RegisterResponse>?> get() = _users
    fun registerUser(
        name: String, address: String, phone: String, email: String, password: String
    ) {
        repository.registerData(name, address, phone, email, password)
            .observeForever { resultState ->
                _users.postValue(resultState)
            }
    }
}