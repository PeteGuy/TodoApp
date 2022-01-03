package com.pierredlvm.todo.form

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignUpResponse(
    @SerialName("token")
    val token : String
)
