package com.group.so.data.di

import android.util.Log
import com.group.so.core.data.NullToEmptyStringAdapter
import com.group.so.data.database.ServiceOrderDatabase
import com.group.so.data.di.interceptor.AuthInterceptor
import com.group.so.data.repository.LoginRepository
import com.group.so.data.repository.LoginRepositoryImpl
import com.group.so.data.repository.category.CategoryRepository
import com.group.so.data.repository.category.CategoryRepositoryImpl
import com.group.so.data.repository.customer.CustomerRepository
import com.group.so.data.repository.customer.CustomerRepositoryImpl
import com.group.so.data.services.CategoryService
import com.group.so.data.services.CustomerService
import com.group.so.data.services.SessionManager
import com.group.so.data.services.UserService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object DataModule {

    private const val BASE_URL =
        "https://group-habits.herokuapp.com/api/v1/"
    private const val OK_HTTP = "Ok Http"

    fun load() {
        loadKoinModules(postsModule() + networkModule() + daoModule())
    }

    private fun daoModule(): Module {
        return module {
            single { ServiceOrderDatabase.getInstance(androidContext()).dao }
            single { ServiceOrderDatabase.getInstance(androidContext()).daoCustomer }
        }
    }

    private fun postsModule(): Module {
        return module {
            single<LoginRepository> { LoginRepositoryImpl(service = get(), get()) }
            single<CategoryRepository> {
                CategoryRepositoryImpl(
                    categoryService = get(),
                    categoryDao = get()
                )
            }
            single<CustomerRepository> {
                CustomerRepositoryImpl(
                    customerService = get(),
                    customerDao = get()
                )
            }
        }
    }

    private fun networkModule(): Module {
        return module {

            single {
                SessionManager(androidContext())
            }

            single {
                createOkHttpClient(get())
            }

            single {
                Moshi.Builder().add(NullToEmptyStringAdapter()).add(KotlinJsonAdapterFactory())
                    .build()
            }

            single {
                createService<UserService>(get(), get())
            }
            single {
                createService<CategoryService>(get(), get())
            }
            single {
                createService<CustomerService>(get(), get())
            }
        }
    }

    private fun createOkHttpClient(sessionManager: SessionManager): OkHttpClient {

        val interceptor = HttpLoggingInterceptor {
            Log.e(OK_HTTP, it)
        }
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(sessionManager))
            .addInterceptor(interceptor)
            .build()
    }

    private inline fun <reified T> createService(
        client: OkHttpClient,
        factory: Moshi,
    ): T {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(factory))
            .build()
            .create(T::class.java)
    }
}
