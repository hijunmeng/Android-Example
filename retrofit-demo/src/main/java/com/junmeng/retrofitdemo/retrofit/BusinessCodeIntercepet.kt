package com.junmeng.retrofitdemo.retrofit


import com.junmeng.retrofitdemo.LoginActivity
import com.junmeng.retrofitdemo.app.CurrentActivityManager
import okhttp3.Interceptor
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

/**
 * 业务响应码拦截器示例
 * 此处假设响应结果按照 {code:1,msg:"",data:null}的格式
 *
 */
class BusinessCodeIntercepet : Interceptor {

    /**
     * 本例假设token放置在请求头header中，采用Bearer Token,即增加头Authorization:Bearer [your token]
     */
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var response = chain.proceed(chain.request())
        handleResponse(response)
        return response
    }

    fun handleResponse(response: Response) {
        if (!response.isSuccessful) {
            return
        }
        var str = response.body?.string()
        var obj = JSONObject(str)
        if (!obj.has("code")) {
            return
        }
        if (obj["code"] == -1) {//这里假设-1表示token失效
            //todo:刷新token后重新请求或跳转到登录页
            //由于每个app都有首页，因此可以从首页跳转到登录页，登录页采用SingleTask模式
            CurrentActivityManager.instance.turnToActivity(LoginActivity::class.java)
        }
    }

}