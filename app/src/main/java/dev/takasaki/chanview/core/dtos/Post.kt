package dev.takasaki.chanview.core.dtos

import android.net.Uri

data class Post(
    val createdAt: String,
    val comment: String?,
    val image: Uri?
)
