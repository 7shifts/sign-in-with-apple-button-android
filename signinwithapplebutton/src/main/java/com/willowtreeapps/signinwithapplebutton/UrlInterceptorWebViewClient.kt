package com.willowtreeapps.signinwithapplebutton

import android.graphics.Bitmap
import android.webkit.WebView
import android.webkit.WebViewClient

internal class UrlInterceptorWebViewClient(
    private val urlToIntercept: String,
    private val javascriptToInject: String
) : WebViewClient() {
    override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
        if (url.contains(urlToIntercept)) {
            /*
             * At this point, we already got a response from apple with the "id token" that we want to fetch to authenticate
             * the user and the "state" that we set in the initial request.
             * Still, that information is encoded as a "form_data" from the POST request that we sent.
             * As within the native code we can't access that POST's form_data, we inject a piece of Javascript code
             * that'll access the document's form_data, get the info and process it, so that it's available in "our"
             * code.
             */
            view.loadUrl("javascript: (function() { ${javascriptToInject} } ) ()")
        } else {
            super.onPageStarted(view, url, favicon)
        }
    }
}
