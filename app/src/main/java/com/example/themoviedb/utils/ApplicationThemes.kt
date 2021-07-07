package com.example.themoviedb.utils

enum class ApplicationThemes {
    LIGHT,
    DARK,
    AUTO;

    companion object {
        fun enumToString(enumString: String): ApplicationThemes {
            return try {
                valueOf(enumString)
            } catch (e: Exception) {
                AUTO
            }
        }
    }
}