package com.pierredlvm.todo.user

import android.net.Uri
import com.pierredlvm.todo.network.Api
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import android.content.ContentResolver

class UserInfoRepository {

    private val userWebService = Api.userWebService


    suspend fun update(newInfo: UserInfo):UserInfo?
    {
        val infoResponse = userWebService.update(newInfo);

        if(infoResponse.isSuccessful)
        {
            return infoResponse.body();


        }
        else
        {
            return null;
        }
    }


    suspend fun load() : UserInfo?
    {
        val response = userWebService.getInfo();
        return if (response.isSuccessful) response.body() else null
    }



}