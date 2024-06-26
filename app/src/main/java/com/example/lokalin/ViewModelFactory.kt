package com.example.lokalin

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.data.di.Injection
import com.example.lokalin.ui.Order.history.HistoryViewModel
import com.example.lokalin.ui.Order.order.OrderViewModel
import com.example.lokalin.ui.addproduct.AddProductViewModel
import com.example.lokalin.ui.cart.CartViewModel
import com.example.lokalin.ui.categories.CategoriesViewModel
import com.example.lokalin.ui.checkout.CheckoutViewModel
import com.example.lokalin.ui.detailproduct.DetailViewModel
import com.example.lokalin.ui.home.HomeViewModel
import com.example.lokalin.ui.login.LoginViewModel
import com.example.lokalin.ui.myproducts.MyProductViewModel
import com.example.lokalin.ui.profile.ProfileViewModel
import com.example.lokalin.ui.search.SearchViewModel
import com.example.lokalin.ui.shoporders.ShopOrdersViewModel
import com.example.lokalin.ui.signup.SignUpViewModel
import com.example.lokalin.ui.wishlist.WishlistViewModel
import com.example.data.Repository

class ViewModelFactory(private val repository: Repository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }

            modelClass.isAssignableFrom(SearchViewModel::class.java) -> {
                SearchViewModel(repository) as T
            }

            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(repository) as T
            }

            modelClass.isAssignableFrom(CategoriesViewModel::class.java) -> {
                CategoriesViewModel(repository) as T
            }

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }

            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                SignUpViewModel(repository) as T
            }

            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(repository) as T
            }

            modelClass.isAssignableFrom(WishlistViewModel::class.java) -> {
                WishlistViewModel(repository) as T
            }

            modelClass.isAssignableFrom(CartViewModel::class.java) -> {
                CartViewModel(repository) as T
            }

            modelClass.isAssignableFrom(AddProductViewModel::class.java) -> {
                AddProductViewModel(repository) as T
            }

            modelClass.isAssignableFrom(MyProductViewModel::class.java) -> {
                MyProductViewModel(repository) as T
            }

            modelClass.isAssignableFrom(CheckoutViewModel::class.java) -> {
                CheckoutViewModel(repository) as T
            }

            modelClass.isAssignableFrom(OrderViewModel::class.java) -> {
                OrderViewModel(repository) as T
            }

            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> {
                HistoryViewModel(repository) as T
            }

            modelClass.isAssignableFrom(ShopOrdersViewModel::class.java) -> {
                ShopOrdersViewModel(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}