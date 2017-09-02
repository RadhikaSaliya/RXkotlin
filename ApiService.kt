package com.user.canopas.rxkotlin

import io.reactivex.Observable
import retrofit2.http.GET

/**
 * Created by canopas on 01/09/17.
 */
interface ApiService {
    @GET("jsonActors")
    fun getActors():Observable<Actorwapper>

}