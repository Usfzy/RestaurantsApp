package com.usfzy.restaurantsapp

import com.usfzy.restaurantsapp.model.DummyContent
import com.usfzy.restaurantsapp.repository.RestaurantsRepository
import com.usfzy.restaurantsapp.state.RestaurantsScreenState
import com.usfzy.restaurantsapp.usecases.GetInitialRestaurantsUseCase
import com.usfzy.restaurantsapp.usecases.GetSortedRestaurantsUseCase
import com.usfzy.restaurantsapp.usecases.ToggleRestaurantUseCase
import com.usfzy.restaurantsapp.view_model.RestaurantViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test

@ExperimentalCoroutinesApi
class RestaurantsViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private val scope = TestScope(dispatcher)

    @Test
    fun initialState_isProduced() = scope.runTest {
        val viewModel = getViewModel()
        val initialState = viewModel.state.value

        assert(
            initialState ==
                    RestaurantsScreenState(
                        restaurants = emptyList(),
                        isLoading = true,
                        error = null,
                    )
        )

    }

    @Test
    fun stateWithContent_isProduced() = scope.runTest {
        val testVm = getViewModel()
        advanceUntilIdle()
        val state = testVm.state.value

        assert(
            state == RestaurantsScreenState(
                restaurants = DummyContent.getDomainRestaurants(),
                isLoading = false,
                error = null,
            )
        )
    }

    private fun getViewModel(): RestaurantViewModel {
        val restaurantsRepository = RestaurantsRepository(
            FakeApiService(),
            FakeRoomDao(),
            dispatcher,
        )
        val getSortedRestaurantsUseCase = GetSortedRestaurantsUseCase(restaurantsRepository)

        val getInitialRestaurantsUseCase =
            GetInitialRestaurantsUseCase(
                restaurantsRepository,
                getSortedRestaurantsUseCase
            )

        val toggleRestaurantUseCase =
            ToggleRestaurantUseCase(
                restaurantsRepository,
                getSortedRestaurantsUseCase
            )

        return RestaurantViewModel(
            getInitialRestaurantsUseCase,
            toggleRestaurantUseCase,
            dispatcher,
        )
    }
}