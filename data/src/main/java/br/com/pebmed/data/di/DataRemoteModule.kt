package br.com.pebmed.data.di

import br.com.pebmed.data.R
import br.com.pebmed.data.remote.api.RepoApi
import com.example.basearch.data.remote.source.RepoRemoteDataSouce
import com.example.basearch.data.remote.source.RepoRemoteDataSourceImpl
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val remoteDataSourceModule = module {
    factory { providesOkHttpClient() }
    single {
        createWebService<RepoApi>(
            okHttpClient = get(),
            url = androidContext().getString(R.string.base_url)
        )
    }

    factory { RepoRemoteDataSourceImpl(repoApi = get()) } bind RepoRemoteDataSouce::class
}

fun providesOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
}

inline fun <reified T> createWebService(okHttpClient: OkHttpClient, url: String): T {
    return Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(url)
        .client(okHttpClient)
        .build()
        .create(T::class.java)
}