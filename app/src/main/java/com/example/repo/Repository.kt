package com.example.repo

import androidx.datastore.core.IOException
import androidx.lifecycle.liveData
import com.example.api.ApiService
import com.example.response.AddCartResponse
import com.example.response.AddWishlistResponse
import com.example.response.Brand
import com.example.response.CartResponseItem
import com.example.response.CategoryResponseItem
import com.example.response.LoginResponse
import com.example.response.ProductsItem
import com.example.response.RegisterResponse
import com.example.response.UserProfileResponseItem
import com.example.response.WishlistResponseItem
import com.example.storyapp.data.pref.UserModel
import com.example.storyapp.data.pref.UserPreference
import com.example.utils.ResultState
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class Repository private constructor(
    private var apiService: ApiService, private val userPreference: UserPreference
) {
    suspend fun getProducts(): List<ProductsItem> {
        val products = apiService.getAllProducts()
        return products ?: emptyList()
    }

    fun updateApiService(apiService: ApiService) {
        this.apiService = apiService
    }

    fun registerData(
        name: String, address: String, phoneNumber: String, email: String, password: String
    ) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.register(
                name, address, phoneNumber, email, picture = "", password = password
            )
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
            val errorMessage = errorResponse.message ?: e.message()
            emit(ResultState.Error(errorMessage))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "An error occurred"))
        }
    }


    suspend fun logout() {
        userPreference.logout()
    }

    fun login(email: String, password: String) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.login(email, password)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
            emit(ResultState.Error("An error occurred"))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "An error occurred"))
        }
    }

    suspend fun getProfile(token: String): List<UserProfileResponseItem> {
        val products = apiService.getProfile(token)
        return products ?: emptyList()
    }


    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun getBrands(): List<Brand> {
        val brand = apiService.getBrands()
        return brand ?: emptyList()
    }

    suspend fun getProductDetail(productId: String): ProductsItem {
        val response = apiService.getProductDetail(productId)
        if (response.isSuccessful) {
            val products = response.body() ?: throw IOException("Product not found")
            return products
        } else {
            throw IOException("Error fetching product detail")
        }
    }

    suspend fun getCategories(): List<CategoryResponseItem> {
        return apiService.getCategories()
    }

    suspend fun getWishlist(token: String): List<WishlistResponseItem> {
        val wishlist = apiService.getWishlist(token)
        return wishlist ?: emptyList()
    }

    suspend fun addWishlist(token: String, productId: String): AddWishlistResponse {

        val successResponse = apiService.addWishlist(
            token, productId
        )
        return successResponse
    }

    suspend fun deleteWishlist(token: String, wishlistId: String): List<WishlistResponseItem> {
        return apiService.deleteWishlist(token, wishlistId)
    }


    suspend fun getMyCart(token: String): List<CartResponseItem> {
        val cart = apiService.getMyCart(token)
        return cart ?: emptyList()
    }

    suspend fun addCart(token: String, productId: String, count: Int): AddCartResponse {

        val successResponse = apiService.addCart(
            token, productId, count
        )
        return successResponse
    }

    fun updateCart(token: String, cartId: String, count: Int) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.updateCart(
                token, cartId, count
            )
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            emit(ResultState.Error("An error occurred"))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "An error occurred"))
        }
    }

    suspend fun deleteCart(token: String, cartId: String): AddCartResponse {
        return apiService.deleteCart(token, cartId)
    }


    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(apiService: ApiService, userPreference: UserPreference) =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService, userPreference)
            }.also { instance = it }
    }
}
