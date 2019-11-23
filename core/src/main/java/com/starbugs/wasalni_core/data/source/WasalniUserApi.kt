package com.starbugs.wasalni_core.data.source

import com.starbugs.wasalni_core.data.model.LoginResponse
import com.starbugs.wasalni_core.data.model.User
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface WasalniUserApi {

    @FormUrlEncoded
    @POST("/user/login")
    fun login(@Field("email")  email :String
                       ,@Field("password")  password :String): Single<LoginResponse>

    @GET("/user/data")
    fun getUser(): Single<User>


}