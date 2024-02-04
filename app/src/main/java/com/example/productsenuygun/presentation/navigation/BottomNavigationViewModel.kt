package com.example.productsenuygun.presentation.navigation

import androidx.lifecycle.ViewModel
import com.example.productsenuygun.domain.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BottomNavigationViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {

    val cartItems = cartRepository.cartTotalItems
}