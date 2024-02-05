package com.usfzy.restaurantsapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.usfzy.restaurantsapp.model.DummyContent
import com.usfzy.restaurantsapp.model.Restaurant
import com.usfzy.restaurantsapp.state.RestaurantsScreenState
import com.usfzy.restaurantsapp.ui.Description
import com.usfzy.restaurantsapp.ui.RestaurantScreen
import org.junit.Rule
import org.junit.Test

class RestaurantScreenTest {

    @get:Rule
    val testRule: ComposeContentTestRule = createComposeRule()

    @Test
    fun initialState_isRendered() {
        testRule.setContent {
            RestaurantScreen(
                state = RestaurantsScreenState(
                    isLoading = true,
                    restaurants = emptyList(),
                    error = null,
                ),
                onItemClick = {},
                onFavoriteClick = { _: Int, _: Boolean -> }
            )
        }

        testRule.onNodeWithContentDescription(Description.RESTAURANTS_LOADING).assertIsDisplayed()
    }

    @Test
    fun stateWithContent_isRendered() {
        val dummyRestaurants: List<Restaurant> = DummyContent.getDomainRestaurants()
        testRule.setContent {
            RestaurantScreen(
                state = RestaurantsScreenState(
                    restaurants = dummyRestaurants,
                    isLoading = false,
                    error = null,
                ),
                onItemClick = {},
                onFavoriteClick = { _: Int, _: Boolean -> }
            )
        }

        testRule.onNodeWithText(dummyRestaurants[0].title).assertIsDisplayed()
        testRule.onNodeWithText(dummyRestaurants[0].description).assertIsDisplayed()

        testRule.onNodeWithContentDescription(Description.RESTAURANTS_LOADING).assertDoesNotExist()
    }

    @Test
    fun stateWithContent_ClickOnItem_IsRegistered() {
        val restaurants = DummyContent.getDomainRestaurants()
        val targetRestaurant = restaurants[0]

        testRule.setContent {
            RestaurantScreen(
                state = RestaurantsScreenState(
                    restaurants = restaurants,
                    isLoading = false
                ),
                onItemClick = { id ->
                    assert(id == targetRestaurant.id)
                },
                onFavoriteClick = { _: Int, _: Boolean -> }
            )
        }

        testRule.onNodeWithText(targetRestaurant.title).performClick()
    }
}