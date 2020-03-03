package br.com.pebmed.data.di

import br.com.pebmed.data.R
import br.com.pebmed.data.pullRequest.PullRequestApi
import br.com.pebmed.data.repo.remote.RepoApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val githubApiScopeName = "GITHUB_API"
const val exampleApiScopeName = "EXAMPLE_API"

val networkModule = module {
    factory {
        providesOkHttpClient(
            get() as HttpLoggingInterceptor
        )
    }

    factory {
        val interceptor = HttpLoggingInterceptor()

        interceptor.level = HttpLoggingInterceptor.Level.BODY

        interceptor
    }

    single(named(githubApiScopeName)) {
        provideRetrofit(
            okHttpClient = get(),
            url = androidContext().getString(R.string.base_url)
        )
    }

    single(named(exampleApiScopeName)) {
        provideRetrofit(
            okHttpClient = get(),
            url = "https://example.com"
        )
    }

    factory<RepoApi> {
        provideApi(retrofit = get(named(githubApiScopeName)))
    }

    factory<PullRequestApi> {
        provideApi(retrofit = get(named(githubApiScopeName)))
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

fun provideRetrofit(okHttpClient: OkHttpClient, url: String): Retrofit {
    return Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(url)
        .client(okHttpClient)
        .build()
}

inline fun <reified T> provideApi(retrofit: Retrofit): T = retrofit.create(T::class.java)
