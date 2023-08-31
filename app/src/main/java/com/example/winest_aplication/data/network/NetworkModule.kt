package com.example.winest_aplication

import com.example.winest_aplication.data.network.*
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit




val networkModule = module {
    single { TokenManager(get()) }
    single { provideLoginService(get()) }
    single { AuthInterceptor(get()) }
    single {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(get<AuthInterceptor>())
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

        Retrofit.Builder()
            .baseUrl("http://192.168.15.4:3000") // if youre using a physical device, put here your ipv4 instead 10.0.2.2
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single { get<Retrofit>().create(PostService::class.java) }
    single { get<Retrofit>().create(WineService::class.java) }
}


private fun provideLoginService(retrofit: Retrofit): AuthService {
    return retrofit.create(AuthService::class.java)
}
