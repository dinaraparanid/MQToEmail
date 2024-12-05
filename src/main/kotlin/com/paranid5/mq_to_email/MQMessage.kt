package com.paranid5.mq_to_email

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MQMessage(
    @SerialName("username") val username: String,
    @SerialName("message") val message: String,
    @SerialName("timestamp") val timestampSecs: Double,
)
