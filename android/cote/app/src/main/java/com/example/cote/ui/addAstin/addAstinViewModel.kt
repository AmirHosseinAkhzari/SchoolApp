package com.example.cote.ui.addAstin

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.cote.domain.repo.CoteRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject


@HiltViewModel
class AddAstinViewModel @Inject constructor(
    private val repo: CoteRepo
) : ViewModel() {


    fun GetNFCStatus(context: Context) : String{
        return repo.checkNFCStatus(context)
    }
}
