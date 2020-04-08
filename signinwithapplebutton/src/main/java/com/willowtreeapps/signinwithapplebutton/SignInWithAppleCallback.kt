package com.willowtreeapps.signinwithapplebutton

interface SignInWithAppleCallback {

    fun onSignInWithAppleSuccess(authorizationCode: String)

    fun onSignInWithAppleFailure(error: Throwable)
}

internal fun SignInWithAppleCallback.toFunction(): (SignInWithAppleResult) -> Unit =
    { result ->
        when (result) {
            is SignInWithAppleResult.Success -> onSignInWithAppleSuccess(result.idToken)
            is SignInWithAppleResult.Failure -> onSignInWithAppleFailure(result.error)
        }
    }
