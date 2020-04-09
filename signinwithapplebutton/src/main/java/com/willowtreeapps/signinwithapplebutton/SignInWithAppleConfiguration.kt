package com.willowtreeapps.signinwithapplebutton

data class SignInWithAppleConfiguration(
    val clientId: String,
    val redirectUri: String
) {

    class Builder {
        private lateinit var clientId: String
        private lateinit var redirectUri: String

        fun clientId(clientId: String) = apply {
            this.clientId = clientId
        }

        fun redirectUri(redirectUri: String) = apply {
            this.redirectUri = redirectUri
        }

        fun build() = SignInWithAppleConfiguration(clientId, redirectUri)
    }
}
