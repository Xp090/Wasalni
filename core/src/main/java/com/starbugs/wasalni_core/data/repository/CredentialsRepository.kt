package com.starbugs.wasalni_core.data.repository

import com.starbugs.wasalni_core.data.source.SharedPreferenceSource

class CredentialsRepository(private val sharedPreferenceSource: SharedPreferenceSource) {

     var userToken:String? = sharedPreferenceSource.get(SharedPreferenceSource.TOKEN_KEY)
         set(value) {
            field = value
            sharedPreferenceSource.set(SharedPreferenceSource.TOKEN_KEY,value)
        }

}