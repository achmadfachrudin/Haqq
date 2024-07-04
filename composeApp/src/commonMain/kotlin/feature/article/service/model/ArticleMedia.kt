package feature.article.service.model

data class ArticleMedia(
    val name: String,
    val feedUrl: String,
    val articles: List<ArticleItem>,
) {
    data class ArticleItem(
        val url: String,
        val title: String,
        val description: String,
        val date: String,
    )
}
