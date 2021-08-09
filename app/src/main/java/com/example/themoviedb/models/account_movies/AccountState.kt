package com.example.themoviedb.models.account_movies

data class AccountState(
    var movieState: AccountMovieStateResponse,
    var isUserLoginIn: Boolean
)
