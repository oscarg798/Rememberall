package com.oscarg798.remembrall.common.network.restclient

import android.content.Context
import arrow.core.Either
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.RuntimeExecutionException
import com.oscarg798.remembrall.common.GoogleAuthOptionsBuilder
import com.oscarg798.remembrall.common.exception.AuthException
import com.oscarg798.remembrall.common.extensions.getUserName
import com.oscarg798.remembrall.common.model.AuthOptions
import com.oscarg798.remembrall.common.model.User
import com.oscarg798.remembrall.common.network.dto.SignInDto
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine

class GoogleSignInClient @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val authOptions: AuthOptions,
    private val googleAuthOptionsBuilder: GoogleAuthOptionsBuilder
) : ExternalSignInClient {

    override fun getSignedInUser(): Either<AuthException, SignInDto> {
        return GoogleSignIn.getLastSignedInAccount(context)?.let { account ->
            Either.Right(SignInDto(User(account.getUserName()), account.serverAuthCode))
        } ?: Either.Left(AuthException.AuthRequired())
    }

    override suspend fun silentSignIn(): Either<AuthException, SignInDto> =
        suspendCancellableCoroutine { continuation ->
            val signInTask = GoogleSignIn.getClient(
                context,
                googleAuthOptionsBuilder.buildFromAuthOptions(authOptions)
            ).silentSignIn()

            signInTask.addOnCompleteListener {
                runCatching {
                    val account = it.result
                    if (account != null) {
                        continuation.resume(
                            Either.Right(
                                SignInDto(
                                    User(account.getUserName()),
                                    account.serverAuthCode
                                )
                            )
                        )
                    } else {
                        continuation.resume(Either.Left(AuthException.AuthRequired()))
                    }
                }.onFailure { error ->
                    onSilentSignInFailure(continuation, error)
                }
            }

            signInTask.addOnFailureListener { error ->
                onSilentSignInFailure(continuation, error)
            }
        }

    override suspend fun logout() = suspendCancellableCoroutine<Unit> { continuation ->
        val task = GoogleSignIn.getClient(
            context,
            googleAuthOptionsBuilder.buildFromAuthOptions(authOptions)
        )
            .signOut()

        task.addOnCompleteListener {
            continuation.resume(Unit)
        }

        task.addOnFailureListener {
            continuation.resumeWithException(AuthException.LogOutError(it))
        }
    }

    private fun onSilentSignInFailure(
        continuation: CancellableContinuation<Either<AuthException, SignInDto>>,
        error: Throwable
    ) {

        if (!continuation.isActive) {
            return
        }

        continuation.resume(
            Either.Left(
                when (error) {
                    !is Exception -> throw error
                    is RuntimeExecutionException,
                    is ApiException -> AuthException.AuthRequired(error)
                    else -> throw error
                }
            )
        )
    }
}
