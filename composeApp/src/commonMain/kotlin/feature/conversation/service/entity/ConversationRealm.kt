package feature.conversation.service.entity

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class ConversationRealm : RealmObject {
    @PrimaryKey
    var id = 0
    var textIndopak = ""
    var textUthmani = ""
    var textTransliteration = ""
    var textTranslationId = ""
    var textTranslationEn = ""
}
