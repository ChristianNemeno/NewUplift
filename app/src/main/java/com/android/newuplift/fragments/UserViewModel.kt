package com.android.newuplift.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.newuplift.database.QuoteDao
import com.android.newuplift.utility.UserAccount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class UserViewModel(private val quoteDao: QuoteDao) : ViewModel() {
    private val _userDetails = MutableLiveData<UserAccount>()
    val userDetails: LiveData<UserAccount> = _userDetails

    fun loadUserDetails(userId: Int) {
        viewModelScope.launch {
            val user = withContext(Dispatchers.IO) {
                quoteDao.getUserDetails(userId)
            }
            _userDetails.postValue(user)
        }
    }

    fun updateUserDetails(userId: Int, updatedUser: UserAccount) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                quoteDao.updateUserDetails(userId, updatedUser)
            }
            loadUserDetails(userId)
        }
    }

}