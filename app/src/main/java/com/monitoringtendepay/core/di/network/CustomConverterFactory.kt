package com.monitoringtendepay.core.di.network

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody


class CustomConverterFactory : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> {
        return CustomResponseBodyConverter<Any>(type)
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody> {
        return CustomRequestBodyConverter<Any>()
    }

    class CustomResponseBodyConverter<T>(private val type: Type) : Converter<ResponseBody, T> {
        override fun convert(value: ResponseBody): T? {
            // Implement your custom deserialization logic here
            val jsonString = value.string()
            return CustomParser.fromJson(jsonString, type)
        }
    }

    class CustomRequestBodyConverter<T : Any> : Converter<T, RequestBody> {
        override fun convert(value: T): RequestBody {
            val jsonString = CustomParser.toJson(value)
            return RequestBody.create("application/json; charset=UTF-8".toMediaType(), jsonString)
        }
    }
}

