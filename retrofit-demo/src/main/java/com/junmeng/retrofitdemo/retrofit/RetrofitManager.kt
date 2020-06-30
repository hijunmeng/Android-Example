package com.junmeng.retrofitdemo.retrofit


import com.junmeng.retrofitdemo.BuildConfig
import com.junmeng.retrofitdemo.app.Config
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

/**
 * 管理Retrofit(单例)
 */
class RetrofitManager private constructor() {
    init {
        initRetrofit()
    }

    private lateinit var mRetrofit: Retrofit
    private lateinit var mClient: OkHttpClient

    companion object {
        private  var mRetrofitManager: RetrofitManager?=null

        @JvmStatic
        @get:Synchronized
        open val instance: RetrofitManager
            get() {
                if (mRetrofitManager == null) {
                    mRetrofitManager =
                        RetrofitManager()
                }
                return mRetrofitManager!!
            }

    }

    private fun initRetrofit() {
        //普通写法
//        val clientBuilder=OkHttpClient.Builder()
//        if(BuildConfig.DEBUG){
//            val logging = HttpLoggingInterceptor()
//            logging.level = HttpLoggingInterceptor.Level.BODY //设置日志级别，body是最详细的
//            clientBuilder.addInterceptor(logging)
//        }
//
//        val client = clientBuilder.build()

        //使用apply的写法
        mClient=OkHttpClient.Builder().apply {
            if(BuildConfig.DEBUG){
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BODY //设置日志级别，body是最详细的
                addInterceptor(logging)
            }
            addInterceptor(TokenIntercepet())
        }.build()


        mRetrofit = Retrofit.Builder()
            .baseUrl(Config.ERROR_BASE_URL)
            .client(mClient)
            .addConverterFactory(ScalarsConverterFactory.create())//需要写在GsonConverterFactory之前，否则则会抛异常：Expected BEGIN_OBJECT but was BEGIN_ARRAY at line 1 column 2 path $
            .addConverterFactory(GsonConverterFactory.create())
            //.addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.createWithScheduler(Schedulers.io()))//统一指定请求放在io线程，这样我们就可以不用每次在调用api时指定了
            .build()



    }

    /**
     * 更新BaseUrl
     */
    open fun updateBaseUrl(url:String){
        mRetrofit= mRetrofit.newBuilder()
            .baseUrl(url)
            .build()
    }

    /**
     * 添加拦截器
     */
    open fun addInterceptor(interceptor: Interceptor){
        mClient= mClient.newBuilder()
            .addInterceptor(interceptor)
            .build()
        mRetrofit= mRetrofit.newBuilder()
            .client(mClient)
            .build()
    }



    fun <T> createService(reqServer: Class<T>?): T {
        return mRetrofit.create(reqServer)
    }

}