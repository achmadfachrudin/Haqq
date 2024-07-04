package core.data

sealed class DataState<out T> {
    data class Error(
        private val error: String,
    ) : DataState<Nothing>() {
        val message: String
            get() =
                error
                    .replace(NetworkSource.Supabase.BASE_URL, "***")
                    .replace(NetworkSource.Supabase.SUPABASE_PROJECT, "***")
                    .replace(NetworkSource.Supabase.SUPABASE_API_KEY, "***")
    }

    data object Loading : DataState<Nothing>()

    data class Success<T>(
        val data: T,
    ) : DataState<T>()
}
