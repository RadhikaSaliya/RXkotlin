package com.user.canopas.rxkotlin

import io.reactivex.Observable
import retrofit2.http.GET

interface ApiService {
    @GET("jsonActors")
    fun getActors():Observable<Actorwapper>

}