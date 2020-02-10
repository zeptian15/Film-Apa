package com.septian.filmapauiux

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.septian.filmapauiux.main.MainActivity
import com.septian.filmapauiux.model.Movie
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ReminderReceiver : BroadcastReceiver() {

    companion object {
        const val TYPE_REMINDER = "daily_reminder"
        const val TYPE_RELEASE = "release_reminder"

        const val EXTRA_TYPE = "type"
        const val EXTRA_NOW = "now"

        private const val ID_DAILY_REMINDER = 100
        private const val ID_RELEASE_TODAY = 101
        private const val MAX_NOTIFICATION = 2

        const val CHANNEL_ID_REMINDER = "Channel_01"
        const val CHANNEL_ID_RELEASE = "Channel_02"
        const val CHANNEL_NAME_REMINDER = "Reminder Channel"
        const val CHANNEL_NAME_RELEASE = "Release Channel"


    }

    val listDataMovies = MutableLiveData<ArrayList<Movie>>()
    private var idNotification = 0

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.getStringExtra(EXTRA_TYPE)) {
            TYPE_REMINDER -> {
                showAlarmNotification(context, ID_DAILY_REMINDER)
            }
            TYPE_RELEASE -> {
                Log.d("List", "Masuk ke sini")
//                showReleaseNotification(context, ID_RELEASE_TODAY)
                getListMovieToday(context, ID_RELEASE_TODAY)
            }
        }
    }

    fun activateDailyReminder(context: Context, isRemind: Boolean) {
        if (isRemind) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, ReminderReceiver::class.java)
            intent.putExtra(EXTRA_TYPE, TYPE_REMINDER)
            // Set Waktu jam 7 pagi
            val time = "07:00"
            // Ubah ke dalam bentuk Array untuk di Parse
            val timeInArray =
                time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeInArray[0]))
            calendar.set(Calendar.MINUTE, Integer.parseInt(timeInArray[1]))
            // Buat Pending Intent
            val pendingIntent = PendingIntent.getBroadcast(context, ID_DAILY_REMINDER, intent, 0)
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        } else {
            cancelAlarm(context, TYPE_REMINDER)
        }
    }

    fun activateReleaseToday(context: Context?, isRemind: Boolean) {
        if (isRemind) {
            val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, ReminderReceiver::class.java)
            intent.putExtra(EXTRA_TYPE, TYPE_RELEASE)
            // Set Waktu jam 8 pagi
            val time = "08:00"
            // Ubah ke dalam bentuk Array untuk di Parse
            val timeInArray =
                time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeInArray[0]))
            calendar.set(Calendar.MINUTE, Integer.parseInt(timeInArray[1]))
            calendar.set(Calendar.SECOND, 0)
            // Buat Pending Intent
            val pendingIntent = PendingIntent.getBroadcast(context, ID_RELEASE_TODAY, intent, 0)
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        } else {
            cancelAlarm(context, TYPE_RELEASE)
        }
    }

    fun isReminderActivated(context: Context?, type: String): Boolean {
        val intent = Intent(context, ReminderReceiver::class.java)
        val requestCode = if (type.equals(
                TYPE_REMINDER,
                ignoreCase = true
            )
        ) ID_DAILY_REMINDER else ID_RELEASE_TODAY

        return PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_NO_CREATE
        ) != null
    }

    private fun showAlarmNotification(context: Context, notifId: Int) {
        val notificationManagerCompat =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, CHANNEL_ID_REMINDER)
            .setSmallIcon(R.drawable.ic_dashboard_24dp)
            .setContentTitle("Selamat Pagi!")
            .setContentText("Yuk cek aplikasi nya sekarang!")
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID_REMINDER,
                CHANNEL_NAME_REMINDER,
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(CHANNEL_ID_REMINDER)
            notificationManagerCompat.createNotificationChannel(channel)
        }
        val notification = builder.build()
        notificationManagerCompat.notify(notifId, notification)
    }

    private fun showReleaseNotification(context: Context, notifId: Int, list: ArrayList<Movie>) {
        if (list != null) {
            val mNotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            val pendingIntent = PendingIntent.getActivity(
                context,
                ID_RELEASE_TODAY,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            var mBuilder: NotificationCompat.Builder
            for (i in 0 until list.size) {
                if (i < 3) {
                    mBuilder = NotificationCompat.Builder(context, CHANNEL_ID_RELEASE)
                        .setContentTitle("Baru! " + list[i].title)
                        .setContentText(list[i].description)
                        .setSmallIcon(R.drawable.ic_dashboard_24dp)
                        .setGroup("new_release_movies")
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                } else {

                    val inboxStyle = NotificationCompat.InboxStyle()
                        .addLine("Baru " + list[i].title)
                        .addLine("Baru " + list[i - 1].title)
                        .addLine("Baru " + list[i - 2].title)
                        .setBigContentTitle("$i Film baru!")
                        .setSummaryText("Dari TMDB")

                    mBuilder = NotificationCompat.Builder(context, CHANNEL_ID_RELEASE)
                        .setContentTitle("$i Film baru")
                        .setContentText("Dari TMDB")
                        .setSmallIcon(R.drawable.ic_dashboard_24dp)
                        .setGroup("new_release_movies")
                        .setGroupSummary(true)
                        .setContentIntent(pendingIntent)
                        .setStyle(inboxStyle)
                        .setAutoCancel(true)
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    /* Create or update. */
                    val channel = NotificationChannel(
                        CHANNEL_ID_RELEASE,
                        CHANNEL_NAME_RELEASE,
                        NotificationManager.IMPORTANCE_HIGH
                    )
                    mBuilder.setChannelId(CHANNEL_ID_RELEASE)
                    mNotificationManager.createNotificationChannel(channel)
                }
                val notification = mBuilder.build()
                mNotificationManager.notify(i, notification)
            }

        }
    }

    private fun cancelAlarm(context: Context?, type: String) {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java)
        val requestCode = if (type.equals(
                TYPE_REMINDER,
                ignoreCase = true
            )
        ) ID_DAILY_REMINDER else ID_RELEASE_TODAY
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        pendingIntent.cancel()

        alarmManager.cancel(pendingIntent)
    }

    private fun getListMovieToday(context: Context, notifId: Int) {
        // Request data dari API
        val client = AsyncHttpClient()
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val listMovies = ArrayList<Movie>()
        val url =
            "https://api.themoviedb.org/3/discover/movie?api_key=${BuildConfig.API_KEY}&primary_release_date.gte=${date}&primary_release_date.lte=${date}"

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                try {
                    val result = String(responseBody!!)
                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("results")

                    for (i in 0 until list.length()) {
                        val movie = list.getJSONObject(i)
                        val movieItem = Movie()
                        movieItem.id = movie.getInt("id").toString()
                        movieItem.title = movie.getString("title")
                        if (movie.getString("overview") == "") {
                            movieItem.description =
                                "Maaf deskripsi tidak tersedia"
                        } else {
                            movieItem.description = movie.getString("overview")
                        }
                        movieItem.vote = movie.getDouble("vote_average")
                        movieItem.language = movie.getString("original_language")
                        movieItem.poster = movie.getString("poster_path")
                        movieItem.background = movie.getString("backdrop_path")

                        listMovies.add(movieItem)
                        Log.d("List", "Dari Fungsi : " + movie.getString("title"))
                        showReleaseNotification(context, notifId, listMovies)
                    }
                    listDataMovies.postValue(listMovies)
                } catch (e: Exception) {
                    Log.d("Exception : ", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                if (error != null) {
                    Log.d("onFailure : ", error.message.toString())
                }
            }
        })
    }
}
