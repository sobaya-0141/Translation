package sobaya.app.translation

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import sobaya.app.translation.di.module

class TranslationApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TranslationApp)
            modules(listOf(module))
        }
    }
}