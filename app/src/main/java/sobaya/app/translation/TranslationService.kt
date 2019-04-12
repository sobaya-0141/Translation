package sobaya.app.translation

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ClipboardManager
import android.content.Context
import android.graphics.PixelFormat
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.TextView
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import sobaya.app.translation.repository.TranslationRepository
import kotlin.concurrent.thread

class TranslationService : LifecycleService() {

    private val text = MutableLiveData<String>()
    private val repository: TranslationRepository by inject()

    override fun onCreate() {
        super.onCreate()
        val id = "translation"
        val nm = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(id, applicationContext.getString(R.string.app_name),
            NotificationManager.IMPORTANCE_DEFAULT)

        nm?.let {
            it.createNotificationChannel(channel)
            val notification = Notification.Builder(applicationContext, id)
                .setSmallIcon(android.R.drawable.ic_menu_agenda)
                .build()
            startForeground(1, notification)
        }

        val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        cm.addPrimaryClipChangedListener {
            val data = cm.primaryClip?.getItemAt(0)
            if (data != null) {
                GlobalScope.launch {
                    val result = repository.translation(data.text.toString()).await()
                    text.postValue(result)
                }
            }
        }
        text.observe(this, Observer {
            showWindow(it)
        })
    }

    private fun showWindow(text: String) {
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val view = LayoutInflater.from(this).inflate(R.layout.layout_text, null, false).apply {
            findViewById<TextView>(R.id.textTranslation).text = text
        }
        val layoutFlag = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            0,
            0,
            layoutFlag,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        )

        windowManager?.addView(view, params)
        thread {
            Thread.sleep(2000)
            windowManager?.removeView(view)
        }
    }
}