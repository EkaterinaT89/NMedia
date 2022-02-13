package ru.netology.nmedia.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import ru.netology.nmedia.R
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.exception.ActionNotFoundException
import kotlin.random.Random

class FCMService : FirebaseMessagingService() {
    private val action = "action"
    private val content = "content"
    private val channelId = "remote"
    private val gson = Gson()

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_remote_name)
            val descriptionText = getString(R.string.channel_remote_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val actionKey = message.data[DATA_ACTION_KEY] ?: return
        val action = Action.values().single { it.key == actionKey }
        try {
            when (action) {
                Action.Like -> {
                    val content = message.data[DATA_CONTENT_KEY]
                    val like = gson.fromJson(content, Like::class.java)
                    handleLike(like)
                }
                Action.NewPost -> {
                    val content = message.data[DATA_CONTENT_KEY]
                    val newPost = gson.fromJson(content, Post::class.java)
                    handleNewPost(newPost)
                }
            }
        } catch (e: ActionNotFoundException) {
            println("Уведомления для такого действия не предусмотрены.")
        }
    }

    override fun onNewToken(token: String) {
        Log.d("FCMService", "FCM token $token")
    }

    @SuppressLint("StringFormatInvalid")
    private fun handleLike(like: Like) {
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(
                getString(
                    R.string.notification_user_liked,
                    like.userName,
                    like.postAuthor
                )
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(this)
            .notify(Random.nextInt(100_000), notification)
    }

    private fun handleNewPost(post: Post) {
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(
                getString(
                    R.string.notification_new_post,
                    post.author,
                    post.content
                )
            )
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(post.content)
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(this)
            .notify(Random.nextInt(100_000), notification)
    }

    companion object {
        private const val DATA_ACTION_KEY = "action"
        private const val DATA_CONTENT_KEY = "content"
    }
}

enum class Action(
    val key: String
) {
    Like("like"),
    NewPost("newPost")
}

data class Like(
    val userId: Long,
    val userName: String,
    val postId: Long,
    val postAuthor: String,
) {

}