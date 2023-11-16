package com.example.myapplication.repository

import com.example.myapplication.models.Person
import retrofit2.Call
import retrofit2.http.*

// The methods in this interface are attributed with the specific (collection related) part of the URL
// The base URL is found in the class BooksRepository

interface PersonsService {
    @GET("persons")
    fun getAllPersons(): Call<List<Person>>
    @GET("persons")
    fun getUserPersons(@Query("user_id") userId : String?): Call<List<Person>>

    @GET("persons/{personId}")
    fun getPersonById(@Path("personId") personId: Int): Call<Person>

    @POST("persons")
    fun savePerson(@Body book: Person): Call<Person>

    @DELETE("persons/{id}")
    fun deletePerson(@Path("id") id: Int): Call<Person>

    @PUT("persons/{id}")
    fun updatePerson(@Path("id") id: Int, @Body person: Person): Call<Person>
}