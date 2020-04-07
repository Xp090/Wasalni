package com.starbugs.wasalni_core.util.retrofit

import com.squareup.moshi.Moshi
import com.starbugs.wasalni_core.data.holder.NetworkState
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class MoshiConverterFactoryForNetworkState(moshi: Moshi) : Converter.Factory() {

    private val moshiConverterFactory: MoshiConverterFactory = MoshiConverterFactory.create(moshi)

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
//        val wrappedType = object : ParameterizedType {
//            override fun getActualTypeArguments(): Array<Type> = arrayOf(type)
//            override fun getOwnerType(): Type? = null
//            override fun getRawType(): Type = NetworkState::class.java
//        }
        val actualType = getParameterUpperBound(0, type as ParameterizedType)
        val moshiConverter: Converter<ResponseBody, *>? =
            moshiConverterFactory.responseBodyConverter(actualType, annotations, retrofit)
        return ResponseBodyConverter(moshiConverter as Converter<ResponseBody, *>)
    }

}


class ResponseBodyConverter<T>(private val converter: Converter<ResponseBody, T>) :
    Converter<ResponseBody, NetworkState<T>> {

    @Throws(IOException::class)
    override fun convert(responseBody: ResponseBody): NetworkState<T> {
        return try {
            val response = converter.convert(responseBody)
            NetworkState.Success(response!!)
        } catch (ex: Exception) {
            NetworkState.failureFromThrowable(ex)
        }
        
    }
}