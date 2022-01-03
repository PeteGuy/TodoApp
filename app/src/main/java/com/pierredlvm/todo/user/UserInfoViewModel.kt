package com.pierredlvm.todo.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class UserInfoViewModel : ViewModel(){
    private val repository = UserInfoRepository()
    private val _userInfo = MutableStateFlow<UserInfo>(value = UserInfo("default","default","default","default"))
    public val userInfo: StateFlow<UserInfo> = _userInfo

    fun loadInfo()
    {
        viewModelScope.launch {
            val fetchedInfo = repository.load()
            if(fetchedInfo != null)
            {
                _userInfo.value = fetchedInfo;
            }
        }
    }

    fun updateInfo(info: UserInfo) {
        viewModelScope.launch {
            val updated = repository.update( info);
            
            if(updated != null)
            {
                val oldInfo = userInfo.value
                if (oldInfo != updated)
                {
                    _userInfo.value = updated;
                }

            }
        }


    }


}