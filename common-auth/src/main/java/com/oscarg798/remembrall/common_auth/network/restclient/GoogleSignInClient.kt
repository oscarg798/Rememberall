package com.oscarg798.remembrall.common_auth.network.restclient

import android.content.Context
import arrow.core.Either
import arrow.core.getOrHandle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.RuntimeExecutionException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.oscarg798.remembrall.common.model.User
import com.oscarg798.remembrall.common_auth.exception.AuthException
import com.oscarg798.remembrall.common_auth.extensions.getUserName
import com.oscarg798.remembrall.common_auth.model.AuthOptions
import com.oscarg798.remembrall.common_auth.model.GoogleAuthOptionsBuilder
import com.oscarg798.remembrall.common_auth.network.model.SignInDto
import com.oscarg798.remembrall.common.toSuspend
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GoogleSignInClient @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val authOptions: AuthOptions,
    private val googleAuthOptionsBuilder: GoogleAuthOptionsBuilder,
    private val firebaseAuth: FirebaseAuth
) : ExternalSignInClient {

    override fun getSignedInUser(): Either<AuthException, SignInDto> {
        return GoogleSignIn.getLastSignedInAccount(context)?.let { account ->
            Either.Right(
                SignInDto(
                    User(
                        account.getUserName(),
                        account.email ?: throw AuthException.DataIntegrityError("email")
                    ),
                    account.serverAuthCode,
                    account.idToken ?: throw AuthException.DataIntegrityError("idToken")
                )
            )
        } ?: Either.Left(AuthException.AuthRequired())
    }

    override suspend fun silentSignIn(): Either<AuthException, SignInDto> {
        return runCatching {
            GoogleSignIn.getClient(
                context,
                googleAuthOptionsBuilder.buildFromAuthOptions(authOptions)
            ).silentSignIn().toSuspend { error ->
                val errorWrapped = error ?: throw RuntimeException()

                when (errorWrapped) {
                    is RuntimeExecutionException,
                    is ApiException -> AuthException.AuthRequired(error)
                    else -> throw error
                }

            }.result ?: throw AuthException.AuthRequired()
        }.map { account ->
            Either.Right(
                SignInDto(
                    User(
                        account.getUserName(),
                        account.email
                            ?: throw AuthException.DataIntegrityError("email")
                    ),
                    account.serverAuthCode,
                    account.idToken
                        ?: throw AuthException.DataIntegrityError("idToken")
                )
            )

        }.getOrElse {
            if (it !is AuthException) throw  it
            Either.Left(it)
        }
    }

    override suspend fun logout() {
        GoogleSignIn.getClient(
            context,
            googleAuthOptionsBuilder.buildFromAuthOptions(authOptions)
        ).signOut().toSuspend {
            AuthException.LogOutError(it)
        }

        firebaseAuth.signOut()
    }

    override fun isUserLoggedIn(): Either<AuthException, Unit> {
        return GoogleSignIn.getLastSignedInAccount(context)?.let {
            Either.Right(Unit)
        } ?: Either.Left(AuthException.LoggedUserNotFound())
    }

    override suspend fun finishLogIn() {
        val user = getSignedInUser().getOrHandle { authException ->
            throw authException
        }

        val credential = GoogleAuthProvider.getCredential(user.token, null)

        firebaseAuth.signInWithCredential(credential).toSuspend {
            AuthException.ErrorFinishingLogOutProcessWithExternalAuthenticators(it)
        }
    }
}
