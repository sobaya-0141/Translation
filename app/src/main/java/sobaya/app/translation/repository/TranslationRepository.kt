package sobaya.app.translation.repository

class TranslationRepository(val api: TranslationApi) {

    fun translation(text: String) = api.translation(text)
}