package com.example.data.di

import android.content.Context
import com.example.data.api.ApiConfig
import com.example.data.Repository
import com.example.storyapp.data.pref.UserPreference
import com.example.storyapp.data.pref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): Repository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)

        return Repository.getInstance(apiService, pref)
    }
}