
import feature.other.service.AppRepository
import io.realm.kotlin.Realm
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class KoinHelper : KoinComponent {
    val getRealm: Realm by inject()
    val appRepository: AppRepository by inject()
}
