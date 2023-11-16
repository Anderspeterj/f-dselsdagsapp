package com.example.myapplication.models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.repository.PersonsRepository



class PersonsViewModel : ViewModel() {
private val repository = PersonsRepository()
    val personsLiveData: LiveData<List<Person>> = repository.PersonsLiveData
    val errorMessageLiveData: LiveData<String> = repository.errorMessageLiveData
    val updateMessageLiveData: LiveData<String> = repository.updateMessageLiveData

    init {
       // reload()
    }

    fun reload(userId: String?) {
        Log.d("APPLE", "viewmodel userId: " + userId)
        repository.getPersonss2(userId)
    }

    operator fun get(index: Int): Person? {
        return personsLiveData.value?.get(index)
    }

    fun add(person: Person) {
        repository.add(person)
    }

    fun delete(id: Int) {
        repository.delete(id)
    }

    fun update(person: Person) {
        repository.update(person)
    }

    fun sortByName() {
        repository.sortByName()
    }

    fun sortByNameDescending() {
        repository.sortByNameDescending()
    }

    fun sortByAge() {
        repository.sortByAge()
    }

    fun sortByAgeDescending() {
        repository.sortByAgeDescending()
    }
    fun sortByBirth() {
        repository.sortByBirth()
    }
    fun sortByBirthDescending() {
        repository.sortByBirthDescending()
    }

    fun filter(condition: String, userId: String?) {
        repository.filter(condition, userId)
    }

}