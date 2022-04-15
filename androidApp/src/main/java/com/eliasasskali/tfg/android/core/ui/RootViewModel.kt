package com.eliasasskali.tfg.android.core.ui

import androidx.lifecycle.ViewModel
import com.eliasasskali.tfg.model.DomainError
import com.eliasasskali.tfg.model.Either
import com.eliasasskali.tfg.ui.error.ErrorHandler
import com.eliasasskali.tfg.ui.executor.Executor

import kotlinx.coroutines.withContext

open class RootViewModel(private val executor: Executor, val errorHandler: ErrorHandler) :
    ViewModel() {

    protected suspend fun <T> execute(f: suspend () -> Either<DomainError, T>): Either<DomainError, T> =
        withContext(executor.bg) { f() }

    protected fun getErrorMessage(domainError: DomainError): String {
        return errorHandler.convert(domainError)
    }
}