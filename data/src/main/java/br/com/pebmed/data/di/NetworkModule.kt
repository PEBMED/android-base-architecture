package br.com.pebmed.data.di

import br.com.pebmed.data.R
import br.com.pebmed.data.remote.api.PullRequestApi
import br.com.pebmed.data.remote.api.RepoApi
import br.com.pebmed.data.remote.source.PullRequestRemoteDataSource
import br.com.pebmed.data.remote.source.PullRequestRemoteDataSourceImpl
import br.com.pebmed.data.remote.source.RepoRemoteDataSouce
import br.com.pebmed.data.remote.source.RepoRemoteDataSourceImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single<OkHttpClient> {
        providesOkHttpClient(
            get() as HttpLoggingInterceptor
        )
    }

    single<HttpLoggingInterceptor> {
        val interceptor = HttpLoggingInterceptor()

        interceptor.level = HttpLoggingInterceptor.Level.BODY

        interceptor
    }

    single {
        createWebService<RepoApi>(
            okHttpClient = get(),
            url = androidContext().getString(R.string.base_url)
        )
    }

    single {
        createWebService<PullRequestApi>(
            okHttpClient = get(),
            url = androidContext().getString(R.string.base_url)
        )
    }
}

fun providesOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
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