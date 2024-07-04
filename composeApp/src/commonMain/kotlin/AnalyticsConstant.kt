import io.github.aakira.napier.Napier
import kotlin.reflect.KClass

object AnalyticsConstant {
    const val SCREEN_NAME = "screen_name"
    const val HOME_SCREEN = "Home screen"

    fun trackScreen(className: KClass<*>) {
        Napier.d { "$SCREEN_NAME ${className.simpleName}" }
    }
}
