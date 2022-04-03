package com.eliasasskali.tfg.android.data.repository

import com.eliasasskali.tfg.model.DomainError
import com.eliasasskali.tfg.model.Either
import com.eliasasskali.tfg.model.Success
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    fun isUserAuthenticated(): Either<DomainError, Boolean>

    suspend fun signOut(): Either<DomainError, Success>

    suspend fun createUserWithEmailAndPassword(userEmail: String, password: String): Either<DomainError, Task<AuthResult>>

    suspend fun signInWithEmailAndPassword(userEmail: String, password: String, onSuccess: () -> Unit = {}): Either<DomainError, Task<AuthResult>>

    suspend fun signInWithGoogleToken(token: String): Either<DomainError, Task<AuthResult>>

    suspend fun signWithCredential(credential: AuthCredential): Either<DomainError, Task<AuthResult>>

    suspend fun signInCompleteTask(task: Task<AuthResult>): Either<DomainError, Success>

    fun getCurrentUser(): FirebaseUser?

    fun isUserLoggedIn(): Boolean

    fun isProfileCompleted(onTrue: () -> Unit = {}, onFalse: () -> Unit = {})
}