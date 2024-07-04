package feature.study.service.mapper

import feature.study.service.entity.NoteRealm
import feature.study.service.model.Note

internal fun NoteRealm.mapToNote(): Note =
    Note(
        id = id,
        title = title,
        text = text,
        speaker = speaker,
        kitab = kitab,
        createdAt = createdAt,
        studyAt = studyAt,
    )
