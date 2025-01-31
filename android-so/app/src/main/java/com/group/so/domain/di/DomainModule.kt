package com.group.so.domain.di

import com.group.so.domain.LoginUseCase
import com.group.so.domain.category.DeleteCategoryUseCase
import com.group.so.domain.category.EditCategoryUseCase
import com.group.so.domain.category.GetCategoriesUseCase
import com.group.so.domain.category.RegisterCategoryUseCase
import com.group.so.domain.category.SearchCategoriesUseCase
import com.group.so.domain.customer.DeleteCustomerUseCase
import com.group.so.domain.customer.EditCustomerUseCase
import com.group.so.domain.customer.GetCustomersByCustomTypeUseCase
import com.group.so.domain.customer.GetCustomersByNameUseCase
import com.group.so.domain.customer.GetCustomersUseCase
import com.group.so.domain.customer.RegisterCustomerUseCase
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

object DomainModule {

    fun load() {
        loadKoinModules(useCaseModule())
    }

    private fun useCaseModule(): Module {
        return module {

            // Auth
            factory { LoginUseCase(get()) }

            // Category
            factory { GetCategoriesUseCase(get()) }
            factory { RegisterCategoryUseCase(get()) }
            factory { EditCategoryUseCase(get()) }
            factory { DeleteCategoryUseCase(get()) }
            factory { SearchCategoriesUseCase(get()) }

            // Customer
            factory { GetCustomersUseCase(get()) }
            factory { GetCustomersByNameUseCase(get()) }
            factory { GetCustomersByCustomTypeUseCase(get()) }
            factory { DeleteCustomerUseCase(get()) }
            factory { EditCustomerUseCase(get()) }
            factory { RegisterCustomerUseCase(get()) }
        }
    }
}
