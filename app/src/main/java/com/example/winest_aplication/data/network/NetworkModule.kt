package com.example.winest_aplication

import com.example.winest_aplication.data.network.AuthInterceptor
import com.example.winest_aplication.data.network.AuthService
import com.example.winest_aplication.data.network.PostService
import com.example.winest_aplication.data.network.TokenManager
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val networkModule = module {
    single { TokenManager(get()) }
    single { provideLoginService(get()) }
    single { AuthInterceptor(get()) }
    single {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(get<AuthInterceptor>())
            .build()

        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single { get<Retrofit>().create(PostService::class.java) }
}


private fun provideLoginService(retrofit: Retrofit): AuthService {
    return retrofit.create(AuthService::class.java)
}
