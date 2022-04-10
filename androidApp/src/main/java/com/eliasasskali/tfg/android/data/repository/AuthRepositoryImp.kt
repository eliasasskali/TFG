package com.eliasasskali.tfg.android.data.repository

import com.eliasasskali.tfg.model.DomainError
import com.eliasasskali.tfg.model.Either
import com.eliasasskali.tfg.model.Success
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class AuthRepositoryImp constructor(
    private val auth: FirebaseAuth
) : AuthRepository {
    override fun isUserAuthenticated(): Either<DomainError, Boolean> {
        return try {
            Either.Right(auth.currentUser != null)
        } catch (e: Exception) {
            Either.Left(DomainError.ErrorNotHandled(e.toString()))
        }
    }

    override fun signOut(): Either<DomainError, Success> {
        return try {
            Firebase.auth.signOut()
            Either.Right(Success)
        } catch (e: Exception) {
            Either.Left(DomainError.SignOutError)
        }
    }

    override suspend fun createUserWithEmailAndPassword(
        userEmail: String,
        password: String
    ): Either<DomainError, Task<AuthResult>> {
        return try {
            Either.Right(Firebase.auth.createUserWithEmailAndPassword(userEmail, password))
        } catch (e: Exception) {
            Either.Left(DomainError.SignUpError)
        }
    }

    override suspend fun signInWithEmailAndPassword(
        userEmail: String,
        password: String,
        onSuccess: () -> Unit
    ): Either<DomainError, Task<AuthResult>> {
        return try {
            val task = Firebase.auth.signInWithEmailAndPassword(userEmail, password)
            Either.Right(task)
        } catch (e: Exception) {
            Either.Left(DomainError.SignInError)
        }
    }

    override suspend fun signInWithGoogleToken(token: String): Either<DomainError, Task<AuthResult>> {
        val credential = GoogleAuthProvider.getCredential(token, null)
        var result: Either<DomainError, Task<AuthResult>> =
            Either.Left(DomainError.SignInGoogleError)
        signWithCredential(credential).fold(
            error = {
                result = Either.Left(DomainError.SignInGoogleError)
            },
            success = {
                result = Either.Right(it)
            }
        )
        return result

    }

    override suspend fun signWithCredential(credential: AuthCredential): Either<DomainError, Task<AuthResult>> {
        return try {
            Either.Right(Firebase.auth.signInWithCredential(credential))
        } catch (e: Exception) {
            Either.Left(DomainError.SignInCredentialError)
        }
    }

    override suspend fun signInCompleteTask(task: Task<AuthResult>): Either<DomainError, Success> {
        return if (task.isSuccessful) {
            Either.Right(Success)
        } else {
            Either.Left(DomainError.SignInError)
        }
    }

    override fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    override fun isUserLoggedIn(): Boolean {
        return getCurrentUser() != null
    }

    override fun isProfileCompleted(onTrue: () -> Unit, onFalse: () -> Unit) {
        val uid = getCurrentUser()?.uid
        val db = Firebase.firestore
        uid?.let {
            var clubExists = false
            var userExists = false
            db.collection("Clubs").document(uid).get().addOnSuccessListener { club ->
                if (club.exists()) {
                    clubExists = true
                    onTrue()
                } else {
                    db.collection("Users").document(uid).get().addOnSuccessListener { user ->
                        if (user.exists()) {
                            userExists = true
                            onTrue()
                        }
                    }
                }
            }.addOnSuccessListener {
                if (!clubExists && !userExists) {
                    onFalse()
                }
            }
        }
    }
}