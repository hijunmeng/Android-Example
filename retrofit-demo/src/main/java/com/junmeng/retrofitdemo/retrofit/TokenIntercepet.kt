package com.junmeng.retrofitdemo.retrofit

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/**
 * 在请求里增加token
 *
 *
 *
 */
class TokenIntercepet :Interceptor{

    /**
     * 本例假设token放置在请求头header中，采用Bearer Token,即增加头Authorization:Bearer [your token]
     */
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var originalRequest=chain.request()
        var reqUrl=originalRequest.url.toString() //可以根据url判断是否需要增加token,比如是登录路径的话那就不用增加token了
        reqUrl?.let {
            if(it.contains("/login")){
                return chain.proceed(originalRequest)

            }
        }
        //增加token头
        val updateRequest: Request =
            originalRequest.newBuilder().header("Authorization", getBearerToken()).build()

        return chain.proceed(updateRequest)
    }

    fun getBearerToken():String{
        //todo: 在此处返回token
        var token=""
        return "Bearer $token"
    }

}