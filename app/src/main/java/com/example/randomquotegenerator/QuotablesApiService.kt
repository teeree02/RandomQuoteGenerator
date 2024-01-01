package com.example.randomquotegenerator

import retrofit2.http.GET

interface QuotablesApiService{

    @GET("random")
    suspend fun getRandomQuote(): Quote

}