
import feature.other.service.AppRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class KoinHelper : KoinComponent {
    val appRepository: AppRepository by inject()
}
