package com.junmeng.retrofitdemo.retrofit


import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path


interface GithubApi {
    //rxjava 返回Observable
    @GET("users/{user}/repos")
    fun listReposObservable(@Path("user") user: String?): Observable<String>

    //rxjava 返回Single，由于网络请求是单事件，就是请求响应，因此用single更适合
    @GET("users/{user}/repos")
    fun listReposSingle(@Path("user") user: String?): Single<String>


    //retrofit支持kotlin协程用法，增加suspend以及返回值直接为结果类型
    @GET("users/{user}/repos")
   suspend fun listReposKT(@Path("user") user: String?): String


    //返回{"code":-1}
    @GET("https://run.mocky.io/v3/dd699dac-2602-4278-a074-1eb47b496c75")
    fun testTokenInvalid():Single<String>

}