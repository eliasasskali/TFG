package com.eliasasskali.tfg.ui.error

import com.eliasasskali.tfg.model.DomainError

expect class ErrorHandler {
    fun convert(error: DomainError): String
}