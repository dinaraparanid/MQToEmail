package com.paranid5.mq_to_email

import com.rabbitmq.client.ConnectionFactory
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private const val HOST = "localhost"
private const val QUEUE_USER_MESSAGE = "user_message"

private val json = Json {
    ignoreUnknownKeys = true
}

fun main() {
    ConnectionFactory().apply { host = HOST }.newConnection().use { connection ->
        connection.createChannel().use { channel ->
            val durable = false
            val exclusive = false
            val autoDelete = true
            val args = null

            channel.queueDeclare(QUEUE_USER_MESSAGE, durable, exclusive, autoDelete, args)

            while (true) {
                val message = MQMessage(username = "bober", message = "kurwa", timestampSecs = System.currentTimeMillis() / 1000.0)
                val encoded = json.encodeToString(message).toByteArray()

                channel.basicPublish("", QUEUE_USER_MESSAGE, null, encoded)
                Thread.sleep(5000)
            }
        }
    }
}
