package com.junmeng.retrofitdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.junmeng.retrofitdemo.app.Config
import com.junmeng.retrofitdemo.retrofit.BusinessCodeIntercepet
import com.junmeng.retrofitdemo.retrofit.GithubApi
import com.junmeng.retrofitdemo.retrofit.RetrofitManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    val TAG = this.javaClass.name
    var githubApi: GithubApi? = null

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //测试动态更新baseurl
        RetrofitManager.instance.updateBaseUrl(Config.BASE_URL)
        //测试动态添加拦截器
        RetrofitManager.instance.addInterceptor(BusinessCodeIntercepet())

        githubApi = RetrofitManager.instance.createService(GithubApi::class.java)

        var res = githubApi?.listReposObservable("huweijian5")
            ?.subscribe({
                Log.i(TAG, "res=$it")
            }, {
                Log.i(TAG, "error=${it.message}")
            })

        githubApi?.listReposSingle("huweijian5")
            ?.subscribe({
                Log.i(TAG, "res2=$it")
            }, {
                Log.i(TAG, "error2=${it.message}")
            })


    }

    fun onClickTokenInvalid(view: View) {
        //测试token失效跳转到登录页
        githubApi?.testTokenInvalid()
            ?.subscribe({
                Log.i(TAG, "res4=$it")
            }, {
                Log.i(TAG, "error4=${it.message}")
            })
    }

    fun onClickCoroutines(view: View) {
        GlobalScope.launch(Dispatchers.Main) {//Dispatchers.Main表示切回主线程
            try { //在协程里要用try-catch捕获异常
                var str = githubApi?.listReposKT("huweijian5")
                Log.i(TAG, "res3=$str")
            } catch (e: Exception) {
                Log.i(TAG, "error3=${e.message}")
            }

        }
    }
}