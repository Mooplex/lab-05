package com.example.listycity

import androidx.compose.runtime.mutableStateListOf
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class CityRepository {
    private val db = Firebase.firestore
    private val citiesRef = db.collection("cities")
    private val _cities = mutableStateListOf(
        City("Edmonton", "AB"),
        City("Vancouver", "BC"),
        City("Toronto", "ON")
    )
    init { // When initializing the repository, fetch all cities from Firestore
        citiesRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                // Handle error
                return@addSnapshotListener
            }

            _cities.clear()
            snapshot?.documents?.forEach { document ->
                val city = document.toObject(City::class.java)
                if (city != null) {
                    _cities.add(city)
                }
            }


        }
    }
    val cities: List<City>
        get() = _cities

    fun addCity(city: City) {
        citiesRef.document(city.name).set(city)
    }


    fun updateCity(oldCity: City, updatedCity: City) {
        // update the city in Firestore
        citiesRef.document(oldCity.name).set(updatedCity)

        // update the city in the local list
        val index = _cities.indexOf(oldCity)
        if (index != -1) {
            _cities[index] = updatedCity
        }
    }
    fun deleteCity(city: City) {
        /*
        val index = _cities.indexOf(city)
        if (index != -1) {
            _cities.removeAt(index)
        }

         */
    }
}