package org.d3ifcool.quiz.ViewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.DatabaseReference

class QuizViewModelFactory (
    private val dataSource: DatabaseReference,
    private val application: Application,
    private val id: String,
    private val idUser: String
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuizViewModel::class.java)) {
            return QuizViewModel(
                dataSource,
                application,
                id,
                idUser
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}