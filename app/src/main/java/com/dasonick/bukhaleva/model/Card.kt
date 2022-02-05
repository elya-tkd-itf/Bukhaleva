package com.dasonick.bukhaleva.model

data class Card(
    val id: Int? = null,
    val description: String,
    val votes: Int? = null,
    val author: String? = null,
    val date: String? = null,
    val gifURL: String,
    val gifSize: Int? = null,
    val previewURL: String? = null,
    val videoURL: String? = null,
    val videoPath: String? = null,
    val videoSize: Int? = null,
    val type: String? = null,
    val width: Int? = null,
    val height: Int? = null,
    val commentsCount: Int? = null,
    val fileSize: Int? = null,
    val canVote: Boolean? = null
)