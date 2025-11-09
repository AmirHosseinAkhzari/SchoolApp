package com.example.school

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.school.domain.repository.AstinRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch


@HiltViewModel
class AstinViewModel @Inject constructor(
    private val repo : AstinRepo
) : ViewModel() {


}