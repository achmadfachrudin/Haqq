import platform.Foundation.NSUserDefaults
import platform.darwin.NSObject

actual typealias KMMContext = NSObject

actual fun KMMContext.saveBoolean(
    key: String,
    value: Boolean,
) {
    NSUserDefaults.standardUserDefaults.setBool(value, key)
}

actual fun KMMContext.saveString(
    key: String,
    value: String,
) {
    NSUserDefaults.standardUserDefaults.setObject(value, key)
}

actual fun KMMContext.saveInt(
    key: String,
    value: Int,
) {
    NSUserDefaults.standardUserDefaults.setInteger(value.toLong(), key)
}

actual fun KMMContext.saveLong(
    key: String,
    value: Long,
) {
    NSUserDefaults.standardUserDefaults.setInteger(value, key)
}

actual fun KMMContext.saveDouble(
    key: String,
    value: Double,
) {
    NSUserDefaults.standardUserDefaults.setDouble(value, key)
}

actual fun KMMContext.loadBoolean(
    key: String,
    default: Boolean,
): Boolean = NSUserDefaults.standardUserDefaults.boolForKey(key)

actual fun KMMContext.loadString(key: String): String = NSUserDefaults.standardUserDefaults.stringForKey(key).orEmpty()

actual fun KMMContext.loadInt(
    key: String,
    default: Int,
): Int = NSUserDefaults.standardUserDefaults.integerForKey(key).toInt()

actual fun KMMContext.loadLong(
    key: String,
    default: Long,
): Long = NSUserDefaults.standardUserDefaults.integerForKey(key)

actual fun KMMContext.loadDouble(
    key: String,
    default: Double,
): Double = NSUserDefaults.standardUserDefaults.integerForKey(key).toDouble()
