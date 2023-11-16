package com.example.myapplication.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.models.Person
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import kotlin.reflect.typeOf

class PersonsRepository {

    private val baseUrl = "https://birthdaysrest.azurewebsites.net/api/"
    // the specific (collection) part of the URL is on the individual methods in the interface FrindstoreService

    //"http://anbo-restserviceproviderPersonss.azurewebsites.net/Service1.svc/"
    private val personsService : PersonsService
    val PersonsLiveData: MutableLiveData<List<Person>> = MutableLiveData<List<Person>>()
    val errorMessageLiveData: MutableLiveData<String> = MutableLiveData()
    val updateMessageLiveData: MutableLiveData<String> = MutableLiveData()

    init {
        //val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val build: Retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create()) // GSON
            //.addConverterFactory(KotlinJsonAdapterFactory)
            //.addConverterFactory(MoshiConverterFactory.create(moshi)) // Moshi, added to Gradle dependencies
            .build()
        personsService = build.create(PersonsService::class.java)
        getPersons3()
    }

    fun onFailureMessage(t: Throwable){
        errorMessageLiveData.postValue(t.message)
        Log.d("APPLE","my method. " + t.message!!)
    }

    fun listResponceFail(response: Response<List<Person>>){
        val message = response.code().toString() + " " + response.message()
        errorMessageLiveData.postValue(message)
        Log.d("APPLE", message)
    }

    fun checkResponse(response: Response<Person>, operation: String){
        if (response.isSuccessful) {
            Log.d("APPLE", "$operation: " + response.body())
            updateMessageLiveData.postValue("$operation: " + response.body())
        } else {
            val message = response.code().toString() + " " + response.message()
            errorMessageLiveData.postValue(message)
            Log.d("APPLE", message)
        }
    }
    fun getPersons(user_id: String?){
        personsService.getUserPersons(user_id).enqueue(object : Callback<List<Person>>{
            override fun onResponse(call: Call<List<Person>>, response: Response<List<Person>>) {
                if (response.isSuccessful) {
                    Log.d("APPLE", personsService.getUserPersons(user_id).toString() )
                    Log.d("APPLE",user_id +"rdszxdfgho")
                    val b: List<Person>? = response.body()
                    PersonsLiveData.postValue(b!!)
                    errorMessageLiveData.postValue("")
                } else {
                    listResponceFail(response)
                }
            }

            override fun onFailure(call: Call<List<Person>>, t: Throwable) {
                onFailureMessage(t)
            }
        })
    }

    fun getPersons3() {
        personsService.getAllPersons().enqueue(object : Callback<List<Person>> {
            override fun onResponse(call: Call<List<Person>>, response: Response<List<Person>>) {
                if (response.isSuccessful) {
                    //Log.d("APPLE", response.body().toString())
                    val b: List<Person>? = response.body()
                    PersonsLiveData.postValue(b!!)
                    errorMessageLiveData.postValue("")
                } else {
                    val message = response.code().toString() + " " + response.message()
                    errorMessageLiveData.postValue(message)
                    Log.d("APPLE", message)
                }
            }

            override fun onFailure(call: Call<List<Person>>, t: Throwable) {
                //booksLiveData.postValue(null)
                errorMessageLiveData.postValue(t.message)
                Log.d("APPLE", t.message!!)
            }
        })
    }
    fun getPersonss2(userId: String?) {
        personsService.getAllPersons().enqueue(object : Callback<List<Person>> {
            override fun onResponse(call: Call<List<Person>>, response: Response<List<Person>>) {
                if (response.isSuccessful) {
                    Log.d("APPLE", response.body().toString() + "repo")
                    val b: List<Person>? = response.body()
                    val userPersonss: MutableList<Person> = mutableListOf()
                    if (b != null) {
                        for(Person in b) {
                            if(Person.userId == userId) {
                                Log.d("APPLE", userId.toString())
                                userPersonss.add(Person)

                            }
                        }
                    }
                    PersonsLiveData.postValue(userPersonss)
                    errorMessageLiveData.postValue("")
                } else {
                    listResponceFail(response)
                }
            }

            override fun onFailure(call: Call<List<Person>>, t: Throwable) {
                onFailureMessage(t)
            }
        })
    }

    fun add(Persons: Person) {
        personsService.savePerson(Persons).enqueue(object : Callback<Person> {
            override fun onResponse(call: Call<Person>, response: Response<Person>) {
                checkResponse(response, "Added")


            }

            override fun onFailure(call: Call<Person>, t: Throwable) {
                onFailureMessage(t)
            }
        })
    }
    fun delete(id: Int) {
        personsService.deletePerson(id).enqueue(object : Callback<Person> {
            override fun onResponse(call: Call<Person>, response: Response<Person>) {
                checkResponse(response, "Deleted")
            }

            override fun onFailure(call: Call<Person>, t: Throwable) {
                onFailureMessage(t)
            }
        })
    }


    fun update(person: Person) {
        personsService.updatePerson(person.id , person).enqueue(object : Callback<Person> {
            override fun onResponse(call: Call<Person>, response: Response<Person>) {
                checkResponse(response, "Updated")
            }

            override fun onFailure(call: Call<Person>, t: Throwable) {
                onFailureMessage(t)
            }
        })
    }

    fun sortByName() {
        PersonsLiveData.value = PersonsLiveData.value?.sortedBy { it.name.uppercase() }
    }

    fun sortByNameDescending() {
        PersonsLiveData.value = PersonsLiveData.value?.sortedByDescending { it.name.uppercase() }
    }

    fun sortByAge() {
        PersonsLiveData.value = PersonsLiveData.value?.sortedBy { it.age }
    }

    fun sortByAgeDescending() {
        PersonsLiveData.value = PersonsLiveData.value?.sortedByDescending { it.age }
    }

    fun sortByBirth() {
        val sdf = SimpleDateFormat("dd-MM")
        PersonsLiveData.value = PersonsLiveData.value?.sortedBy {
            sdf.parse("${it.birthDayOfMonth.toString()}-${it.birthMonth.toString()}")
        }
    }

    fun sortByBirthDescending() {
        val sdf = SimpleDateFormat("dd-MM")
        PersonsLiveData.value = PersonsLiveData.value?.sortedByDescending {
            sdf.parse("${it.birthDayOfMonth.toString()}-${it.birthMonth.toString()}")
        }

    }

    fun filter(condition: String, PersonsId: String?){
        personsService.getUserPersons(PersonsId).enqueue(object : Callback<List<Person>> {
            override fun onResponse(call: Call<List<Person>>, response: Response<List<Person>>) {
                if (response.isSuccessful) {
                    val b: List<Person>? = response.body()
                    val tryNum: String = condition

                    if(tryNum.toIntOrNull() != null) {
                        PersonsLiveData.value = b?.filter { person -> person.age == tryNum.toInt()}
                    }
                    else {
                        PersonsLiveData.value = b?.filter { person -> person.name.uppercase().contains(condition.uppercase()) }
                    }
                } else {
                    listResponceFail(response)
                }
            }

            override fun onFailure(call: Call<List<Person>>, t: Throwable) {
                onFailureMessage(t)
            }
        })
    }

}