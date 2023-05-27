package dev.zwander.common.util

/**
 * Delegate URL launching and email sending to the platform.
 */
expect object UrlHandler {
    /**
     * Launch a given URL.
     * @param url the URL to open.
     */
    fun launchUrl(url: String)

    /**
     * Send an email.
     * @param address the address to send to.
     * @param subject the subject line (optional).
     * @param content the email body (optional).
     */
    fun sendEmail(address: String, subject: String? = null, content: String? = null)
}