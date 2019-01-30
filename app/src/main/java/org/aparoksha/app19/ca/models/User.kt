package org.aparoksha.app19.ca.models

/**
 * Created by akshat on 2/12/17.
 */
data class User(
        var totalPoints: Long = 0,
        var identifier: String = "",
        var collegeName: String = "",
        var userName: String = "",
        var fullName: String = "",
        var phoneNumber: String = "",
        var gender: String = "",
        var images: Map<String, Image>? = null,
        var cards: Map<String, Card>? = null,
        var accountVerified: Boolean = false,
        var tokenFCM: String? = "",
        var refer: String = ""
)
