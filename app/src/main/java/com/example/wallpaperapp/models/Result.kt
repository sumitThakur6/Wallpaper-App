package com.example.wallpaperapp.models

data class Result(
    val alt_description: String,
    val blur_hash: String,
    val color: String,
    val created_at: String,
    val description: String,
    val height: Int,
    val id: String,
    val liked_by_user: Boolean,
    val likes: Int,
    val promoted_at: String,
    val slug: String,
    val sponsorship: Any,
    val updated_at: String,
    val urls: Urls,
    val width: Int
)