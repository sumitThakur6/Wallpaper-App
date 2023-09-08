package com.example.wallpaperapp.models

data class SearchImageResponse(
    val results: List<Result>,
    val total: Int,
    val total_pages: Int
)