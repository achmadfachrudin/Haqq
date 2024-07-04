import android.app.Activity

private const val SP_NAME = "kmm_app"

actual typealias KMMContext = Activity

actual fun KMMContext.saveBoolean(key: String, value: Boolean) {
    getSpEditor().putBoolean(key, value).apply()
}

actual fun KMMContext.saveString(key: String, value: String) {
    getSpEditor().putString(key, value).apply()
}

actual fun KMMContext.saveInt(key: String, value: Int) {
    getSpEditor().putInt(key, value).apply()
}

actual fun KMMContext.saveLong(key: String, value: Long) {
    getSpEditor().putLong(key, value).apply()
}

actual fun KMMContext.saveDouble(key: String, value: Double) {
    getSpEditor().putFloat(key, value.toFloat()).apply()
}

actual fun KMMContext.loadBoolean(key: String, default: Boolean): Boolean {
    return getSp().getBoolean(key, default)
}

actual fun KMMContext.loadString(key: String): String {
    return getSp().getString(key, null).orEmpty()
}

actual fun KMMContext.loadInt(key: String, default: Int): Int {
    return getSp().getInt(key, default)
}

actual fun KMMContext.loadLong(key: String, default: Long): Long {
    return getSp().getLong(key, default)
}

actual fun KMMContext.loadDouble(key: String, default: Double): Double {
    return getSp().getFloat(key, default.toFloat()).toDouble()
}

private fun KMMContext.getSp() = getSharedPreferences(SP_NAME, 0)
private fun KMMContext.getSpEditor() = getSp().edit()