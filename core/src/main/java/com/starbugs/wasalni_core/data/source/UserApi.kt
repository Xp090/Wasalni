package com.starbugs.wasalni_core.data.source

import com.starbugs.wasalni_core.data.holder.NetworkState
import com.starbugs.wasalni_core.data.model.LoginResponse
import com.starbugs.wasalni_core.data.model.User
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface UserApi {

    @FormUrlEncoded
    @POST("/auth/login")
    fun login(@Field("username")  email :String
                       ,@Field("password")  password :String): Single<NetworkState<LoginResponse>>

    @GET("/user/data")
    fun getUserData(): Single<NetworkState<User>>


}