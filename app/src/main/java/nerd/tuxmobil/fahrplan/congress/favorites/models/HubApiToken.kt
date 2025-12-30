package nerd.tuxmobil.fahrplan.congress.favorites.models

import info.metadude.android.eventfahrplan.commons.temporal.Moment

data class HubApiToken(
    private val dto: HubOAuthTokenDto
) {
    val expiresAt = Moment.ofEpochMilli(dto.createdAt).plusSeconds(dto.expiresIn.toLong())
    val accessToken = dto.accessToken
    val refreshToken = dto.refreshToken

    fun isValidNow() = accessToken.isNotEmpty() && Moment.now().isBefore(expiresAt.minusSeconds(5))
    fun canRefresh() = accessToken.isNotEmpty() && dto.refreshToken.isNotEmpty()
}

val UNAUTHENTICATED_TOKEN = HubOAuthTokenDto(
    accessToken = "",
    expiresIn = 0,
    refreshToken = "",
    tokenType = "",
    createdAt = 0,
)

const val HUB_ACTION_FAV_SYNC = "FAV_SYNC"

sealed interface HubApiTokenState {
    val action: String
}

class HubApiTokenValidState(val token: HubApiToken, override val action: String): HubApiTokenState
class HubApiTokenRequiresAuthState(override val action: String, val codeChallenge: String): HubApiTokenState