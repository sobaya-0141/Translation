package sobaya.app.translation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE = 100

    private fun checkPermission() = Settings.canDrawOverlays(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (checkPermission()) {
            startForegroundService(Intent(this, TranslationService::class.java))
            finish()
        } else
            requestPermission()
    }

    private fun requestPermission() {
        startActivityForResult(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:${packageName}")), REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == REQUEST_CODE) {
            if (checkPermission())
                startForegroundService(Intent(this, TranslationService::class.java))
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}