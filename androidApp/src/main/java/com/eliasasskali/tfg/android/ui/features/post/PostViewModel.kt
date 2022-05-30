package com.eliasasskali.tfg.android.ui.features.post

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.eliasasskali.tfg.android.core.ui.RootViewModel
import com.eliasasskali.tfg.android.data.repository.authentication.AuthRepository
import com.eliasasskali.tfg.android.data.repository.clubAthlete.ClubAthleteRepositoryImpl
import com.eliasasskali.tfg.ui.error.ErrorHandler
import com.eliasasskali.tfg.ui.executor.Executor
import kotlinx.coroutines.launch

class PostViewModel(
    private val repository: ClubAthleteRepositoryImpl,
    private val authRepository: AuthRepository,
    executor: Executor,
    errorHandler: ErrorHandler
) : RootViewModel(executor, errorHandler) {
    val state: MutableState<PostState> = mutableStateOf(PostState())

    fun setTitle(title: String) {
        state.value = state.value.copy(title = title)
    }

    fun setContent(content: String) {
        state.value = state.value.copy(content = content)
    }

    fun setStep(step: PostSteps) {
        state.value = state.value.copy(step = step)
    }

    fun resetState() {
        state.value = PostState()
    }

    fun uploadPost() {
        setStep(PostSteps.IsLoading)
        viewModelScope.launch {
            execute {
                repository.uploadPost(state.value.title, state.value.content)
            }.fold(
                error = {
                    // TODO; Handle error
                },
                success = {
                    setStep(PostSteps.PostCreated)
                }
            )
        }
    }
}