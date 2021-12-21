package com.pierredlvm.todo.tasklist

import java.io.Serializable
import kotlinx.serialization.SerialName;



@kotlinx.serialization.Serializable
data class Task(
    @SerialName("id")
    val id: String,
    @SerialName("title")
    val title:String,
    @SerialName("description")
    val description:String = "Default description"
) : Serializable
