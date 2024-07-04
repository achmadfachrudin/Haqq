package feature.article.service.mapper

import core.util.DateUtil
import feature.article.service.entity.ArticleFeedEntity
import feature.article.service.model.ArticleMedia

internal fun ArticleFeedEntity.ArticleItemEntity.mapToArticleItem(): ArticleMedia.ArticleItem =
    ArticleMedia.ArticleItem(
        url = url,
        title = title,
        description = description.replace(Regex("<[^>]*>"), "").trim(),
        date = DateUtil.getTimeAgo(date.replace(" ", "T").plus(".000Z")),
    )
