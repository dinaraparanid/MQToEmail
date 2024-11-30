package com.paranid5.mq_to_email

import com.rabbitmq.client.*
import io.github.cdimascio.dotenv.dotenv
import kotlinx.serialization.json.Json
import java.nio.charset.Charset
import java.util.Properties
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

private const val ENV_USER_NAME = "USER_NAME"
private const val ENV_PASSWORD = "PASSWORD"
private const val ENV_FROM = "FROM"
private const val ENV_TO = "TO"

private const val EMAIL_HOST = "smtp.yandex.ru"
private const val EMAIL_PORT = "465"

private const val HOST = "localhost"
private const val QUEUE_USER_MESSAGE = "user_message"

private val json = Json {
    ignoreUnknownKeys = true
}

data class EmailMessageCredentials(
    val username: String,
    val password: String,
    val from: String,
    val to: String,
)

private fun sendEmail(msgData: MQMessage, credentials: EmailMessageCredentials) {
    val props = Properties().also { props ->
        props["mail.smtp.host"] = EMAIL_HOST
        props["mail.smtp.port"] = EMAIL_PORT
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.socketFactory.port"] = EMAIL_PORT
        props["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
    }

    val session = Session.getInstance(props, object : Authenticator() {
        override fun getPasswordAuthentication() =
            PasswordAuthentication(credentials.username, credentials.password)
    })

    runCatching {
        val emailMsg = MimeMessage(session).apply {
            setFrom(InternetAddress(credentials.from))
            setRecipients(Message.RecipientType.TO, InternetAddress.parse(credentials.to))
            subject = "Message from ${msgData.username}"
            setText(msgData.message)
        }

        Transport.send(emailMsg)
    }.onFailure { it.printStackTrace() }
}

fun main() {
    ConnectionFactory().apply { host = HOST }.newConnection().use { connection ->
        val dotenv = dotenv()

        val credentials = EmailMessageCredentials(
            username = dotenv.get(ENV_USER_NAME),
            password = dotenv.get(ENV_PASSWORD),
            from = dotenv.get(ENV_FROM),
            to = dotenv.get(ENV_TO),
        )

        connection.createChannel().use { channel ->
            val durable = false
            val exclusive = false
            val autoDelete = true
            val args = null
            val autoAck = true

            channel.queueDeclare(QUEUE_USER_MESSAGE, durable, exclusive, autoDelete, args)

            while (true) {
                channel.basicConsume(QUEUE_USER_MESSAGE, autoAck, { _, delivery ->
                    val message = json.decodeFromString<MQMessage>(
                        delivery.body.toString(Charset.forName(Charsets.UTF_8.name()))
                    )

                    sendEmail(message, credentials)
                }) { _ -> }
            }
        }
    }
}
