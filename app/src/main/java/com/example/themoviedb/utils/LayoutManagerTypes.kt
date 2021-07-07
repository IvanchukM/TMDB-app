package com.example.themoviedb.utils

enum class LayoutManagerTypes {
    GRID,
    LINEAR;

    companion object {
        fun enumToString(enumString: String): LayoutManagerTypes {
            return try {
                valueOf(enumString)
            } catch (e: Exception) {
                GRID
            }
        }
    }
}
