package com.monitoringtendepay.core.di.network

import com.google.gson.Gson
import java.lang.reflect.Type

object CustomParser {
    private val gson = Gson()

    fun <T> fromJson(json: String, type: Type): T? {
        println("JSON to parse: $json")
        try {
            return gson.fromJson(json, type)
        } catch (e: Exception) {
            println("Error parsing JSON: ${e.message}")
            return null
        }
    }

    fun toJson(obj: Any): String {
        return gson.toJson(obj)
    }
}
