package com.abduqodirov.guitaronlineshop.viewmodel

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class SubmitProductViewModelTest {

    lateinit var viewModel: SubmitProductViewModel

    @Before
    fun setUp() {
        viewModel = SubmitProductViewModel()
    }

    @Test
    fun nameValidatorWithRightNamePasses() {
        val result = viewModel.isValidName("John")

        assertThat(result, `is`(true))
    }

    @Test
    fun nameValidatorWithEmptyStringFails() {
        val result = viewModel.isValidName("")

        assertThat(result, `is`(false))
    }

    @Test
    fun priceValidatorWithRightPrice() {
        val result = viewModel.isValidPrice("560")

        assertThat(result, `is`(true))
    }

    @Test
    fun priceValidatorWithNegativeNumberFails() {
        val result = viewModel.isValidPrice("-234")

        assertThat(result, `is`(false))
    }

    @Test
    fun priceValidatorWithEmptyStringFails() {
        val result = viewModel.isValidPrice("")

        assertThat(result, `is`(false))
    }

    @Test
    fun descValidatorWithEmptyStringFails() {
        val result = viewModel.isValidDesc("")

        assertThat(result, `is`(false))
    }

    @Test
    fun descValidatorWithNineCharacterStringFails() {
        val result = viewModel.isValidDesc("123456789")

        assertThat(result, `is`(false))
    }
}
