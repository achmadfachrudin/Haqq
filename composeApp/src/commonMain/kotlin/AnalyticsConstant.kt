import io.github.aakira.napier.Napier

object AnalyticsConstant {
    const val SCREEN_NAME = "screen_name"
    const val HOME_SCREEN = "Home screen"

    fun trackScreen(screen: String) {
        Napier.d { "$SCREEN_NAME $screen" }
    }
}
