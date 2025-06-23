package feature.study.service.mapper

import feature.study.service.entity.NoteRoom
import feature.study.service.model.Note

internal fun NoteRoom.mapToNote(): Note =
    Note(
        id = id,
        title = title,
        text = text,
        speaker = speaker,
        kitab = kitab,
        createdAt = createdAt,
        studyAt = studyAt,
    )
