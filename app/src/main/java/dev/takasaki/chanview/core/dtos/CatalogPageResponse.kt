package dev.takasaki.chanview.core.dtos

data class CatalogPageResponse(
    val page: Int,
    val threads: List<CatalogResponse>
)
