/**
 * val pref = SharedStorage(context)
 * let pref = SharedStorage(context : NSObject())
 */

expect class KMMContext

expect fun KMMContext.saveBoolean(
    key: String,
    value: Boolean,
)

expect fun KMMContext.saveString(
    key: String,
    value: String,
)

expect fun KMMContext.saveInt(
    key: String,
    value: Int,
)

expect fun KMMContext.saveLong(
    key: String,
    value: Long,
)

expect fun KMMContext.saveDouble(
    key: String,
    value: Double,
)

expect fun KMMContext.loadBoolean(
    key: String,
    default: Boolean,
): Boolean

expect fun KMMContext.loadString(key: String): String

expect fun KMMContext.loadInt(
    key: String,
    default: Int,
): Int

expect fun KMMContext.loadLong(
    key: String,
    default: Long,
): Long

expect fun KMMContext.loadDouble(
    key: String,
    default: Double,
): Double

class SharedStorage(
    private val context: KMMContext,
) {
    fun saveBoolean(
        key: String,
        value: Boolean,
    ) = context.saveBoolean(key, value)

    fun saveString(
        key: String,
        value: String,
    ) = context.saveString(key, value)

    fun saveInt(
        key: String,
        value: Int,
    ) = context.saveInt(key, value)

    fun saveLong(
        key: String,
        value: Long,
    ) = context.saveLong(key, value)

    fun saveDouble(
        key: String,
        value: Double,
    ) = context.saveDouble(key, value)

    fun loadBoolean(
        key: String,
        default: Boolean,
    ): Boolean = context.loadBoolean(key, default)

    fun loadString(key: String): String = context.loadString(key)

    fun loadInt(
        key: String,
        default: Int,
    ): Int = context.loadInt(key, default)

    fun loadLong(
        key: String,
        default: Long,
    ): Long = context.loadLong(key, default)

    fun loadDouble(
        key: String,
        default: Double,
    ): Double = context.loadDouble(key, default)
}
