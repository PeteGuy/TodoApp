package com.pierredlvm.todo.network

import android.content.Context
import androidx.preference.PreferenceManager
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.pierredlvm.todo.user.SHARED_PREF_TOKEN_KEY
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

object Api {



    lateinit var appContext: Context

    // constantes qui serviront à faire les requêtes
    private const val BASE_URL = "https://android-tasks-api.herokuapp.com/api/"
    //private const val TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjo1NDksImV4cCI6MTY3MDMzMjgxMH0.nIeNd3VvNPqf4er_t5lsjd4A8vrBurF72mOdkJAaWSA"

    fun setUpContext(context: Context) {
        appContext = context
    }

    // client HTTP
    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                // intercepteur qui ajoute le `header` d'authentification avec votre token:
                var token = PreferenceManager.getDefaultSharedPreferences(appContext).getString(SHARED_PREF_TOKEN_KEY, "")

                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                chain.proceed(newRequest)
            }
            .build()
    }

    // sérializeur JSON: transforme le JSON en objets kotlin et inversement
    private val jsonSerializer = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    // instance de convertisseur qui parse le JSON renvoyé par le serveur:
    private val converterFactory =
        jsonSerializer.asConverterFactory("application/json".toMediaType())

    // permettra d'implémenter les services que nous allons créer:
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(converterFactory)
        .build()

    val userWebService by lazy {
        retrofit.create(UserWebService::class.java);
    }

    val tasksWebService by lazy {
        retrofit.create(TaskWebService::class.java);
    }



}