@file:Suppress("MaxLineLength", "FunctionParameterNaming", "FunctionNaming", "LongParameterList")

package com.group.so.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.group.so.core.RemoteException
import com.group.so.core.Resource
import com.group.so.core.State
import com.group.so.data.CustomerCustomType
import com.group.so.data.entities.model.Customer
import com.group.so.data.repository.customer.CustomerRepository
import com.group.so.domain.customer.GetCustomersByCustomTypeUseCase
import com.group.so.domain.customer.GetCustomersByNameUseCase
import com.group.so.domain.customer.GetCustomersUseCase
import com.group.so.mock.CustomerMock
import com.group.so.presentation.ui.customer.CustomerViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CustomerViewModelTest {

    val customerRepository = mockk<CustomerRepository>(relaxed = true)

    val getCustomersUseCase = GetCustomersUseCase(customerRepository)

    val getCustomersByCustomTypeUseCase = GetCustomersByCustomTypeUseCase(customerRepository)

    val getCustomersByNameUseCase = GetCustomersByNameUseCase(customerRepository)

    private lateinit var viewModel: CustomerViewModel

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesTestRule = CoroutineTestRule()

    @Before
    fun setup() {

        // Dispatchers.setMain(Dispatchers.Unconfined)
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = CustomerViewModel(
            getCustomersUseCase,
            getCustomersByCustomTypeUseCase,
            getCustomersByNameUseCase,

        )
        coEvery { getCustomersUseCase.execute() } returns flow {
            emit(Resource.Success(data = CustomerMock.mockCustomerList()))
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun ` loading cutomers successfully `() = runTest {

        val customerListState = MutableStateFlow<State<List<Customer>>>(State.Idle)
        coEvery { getCustomersUseCase.execute() } returns flow {
            emit(Resource.Success(data = CustomerMock.mockCustomerList()))
        }
        viewModel.fetchLatestCustomers()
        runCurrent()
        customerListState.value = viewModel.customerListState.value

        assert(customerListState.value is State.Success)
    }

    @Test
    fun ` loading customers error `() = runTest {

        val customerListState = MutableStateFlow<State<List<Customer>>>(State.Idle)
        coEvery { getCustomersUseCase.execute() } returns flow {
            emit(Resource.Error(data = null, error = RemoteException("")))
        }
        viewModel.fetchLatestCustomers()
        runCurrent()
        customerListState.value = viewModel.customerListState.value

        assert(customerListState.value is State.Error)
    }

    @Test
    fun ` loading customers by name successfully `() = runTest {

        val customerListState = MutableStateFlow<State<List<Customer>>>(State.Idle)
        coEvery { getCustomersByNameUseCase.execute("teste") } returns flow {
            emit(Resource.Success(data = CustomerMock.mockCustomerList()))
        }
        viewModel.getCustomersByName("teste")
        runCurrent()
        customerListState.value = viewModel.customerListState.value

        assert(customerListState.value is State.Success)
    }

    @Test
    fun ` loading customers by custom type person successfully `() = runTest {

        val customerListState = MutableStateFlow<State<List<Customer>>>(State.Idle)
        coEvery { getCustomersByCustomTypeUseCase.execute("person") } returns flow {
            emit(Resource.Success(data = CustomerMock.mockCustomerList()))
        }
        viewModel.getAllCustomersByCustomType(CustomerCustomType.PERSON)
        runCurrent()
        customerListState.value = viewModel.customerListState.value

        assert(customerListState.value is State.Success)
    }

    @Test
    fun ` loading customers by custom type business successfully `() = runTest {

        val customerListState = MutableStateFlow<State<List<Customer>>>(State.Idle)
        coEvery { getCustomersByCustomTypeUseCase.execute("business") } returns flow {
            emit(Resource.Success(data = CustomerMock.mockCustomerList()))
        }
        viewModel.getAllCustomersByCustomType(CustomerCustomType.BUSINESS)
        runCurrent()
        customerListState.value = viewModel.customerListState.value

        assert(customerListState.value is State.Success)
    }
}
