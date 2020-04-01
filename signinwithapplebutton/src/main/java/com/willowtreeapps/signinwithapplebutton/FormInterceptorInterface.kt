package com.willowtreeapps.signinwithapplebutton

import android.webkit.JavascriptInterface

internal class FormInterceptorInterface(
    private val expectedState: String,
    private val callback: (SignInWithAppleResult) -> Unit
) {
    @JavascriptInterface
    fun processFormData(formData: String) {
        val values = formData.split(FORM_DATA_SEPARATOR)
        val idTokenEncoded = values.find { it.startsWith(ID_TOKEN) }
        val stateEncoded = values.find { it.startsWith(STATE) }

        if (idTokenEncoded != null && stateEncoded != null) {
            val stateValue = stateEncoded.substringAfter(KEY_VALUE_SEPARATOR)
            val tokenValue = idTokenEncoded.substringAfter(KEY_VALUE_SEPARATOR)

            if (stateValue == expectedState) {
                callback(SignInWithAppleResult.Success(tokenValue))
            } else {
                callback(SignInWithAppleResult.Failure(IllegalArgumentException("state does not match")))
            }
        } else {
            callback(SignInWithAppleResult.Failure(IllegalArgumentException("id token not returned")))
        }
    }

    companion object {
        const val NAME = "FormInterceptorInterface"
        private const val STATE = "state"
        private const val ID_TOKEN = "id_token"
        private const val FORM_DATA_SEPARATOR = "|"
        private const val KEY_VALUE_SEPARATOR = "="

        /**
         * This piece of Javascript code fetches all (key, value) attributes from the site's form data and concatenates
         * them in the form: "key [KEY_VALUE_SEPARATOR] value [FORM_DATA_SEPARATOR]".
         * Then, invokes the method [processFormData] on the app's side (that's exposed to Javascript) so that the form
         * data can be analyzed in the app's context.
         */
        val JS_TO_INJECT = """
        function parseForm(form){
            var values = '';
            for(var i=0 ; i< form.elements.length; i++){
                values += 
                    form.elements[i].name + 
                    '${KEY_VALUE_SEPARATOR}' + 
                    form.elements[i].value + 
                    '${FORM_DATA_SEPARATOR}'
            }
            ${NAME}.processFormData(values);
        }
        
        for(var i=0 ; i< document.forms.length ; i++){
            parseForm(document.forms[i]);
        }
        """.trimIndent()
    }
}
