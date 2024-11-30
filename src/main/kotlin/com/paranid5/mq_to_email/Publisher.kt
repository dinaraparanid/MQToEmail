package com.paranid5.mq_to_email

import com.rabbitmq.client.ConnectionFactory

private const val HOST = "localhost"
private const val QUEUE_USER_MESSAGE = "user_message"

fun main() {
    ConnectionFactory().apply { host = HOST }.newConnection().use { connection ->
        connection.createChannel().use { channel ->
            val durable = false
            val exclusive = false
            val autoDelete = true
            val args = null

            channel.queueDeclare(QUEUE_USER_MESSAGE, durable, exclusive, autoDelete, args)

            while (true) {
                channel.basicPublish("", QUEUE_USER_MESSAGE, null, "test ${System.currentTimeMillis()}".toByteArray())
                Thread.sleep(5000)
            }
        }
    }
}
