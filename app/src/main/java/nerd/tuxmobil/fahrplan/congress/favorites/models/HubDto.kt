package nerd.tuxmobil.fahrplan.congress.favorites.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

// Classes to deserialize JSON emitted by the Hub API v2 https://api.events.ccc.de/congress/2025/v2/docs

@JsonClass(generateAdapter = true)
data class HubOAuthTokenDto(
    @field:Json(name = "access_token")
    val accessToken: String,

    /**
     * In seconds, usually 3600 => 1 hour
     * */
    @field:Json(name = "expires_in")
    val expiresIn: Int,

    /**
     * The remote API does not set this value,
     * we set it internally upon receival, and store the serialized token on the device
     */
    @field:Json(name = "created_at")
    val createdAt: Long = 0,

    @field:Json(name = "refresh_token")
    val refreshToken: String,

    @field:Json(name = "token_type")
    val tokenType: String,
)


@JsonClass(generateAdapter = true)
data class HubEventsDto(
    @field:Json(name = "data")
    val data: List<HubEventDto>,

    @field:Json(name = "pagination")
    val pagination: HubPaginationDto
)

@JsonClass(generateAdapter = true)
data class HubEventDto(

    @field:Json(name = "room_id")
    val roomId: String,

    @field:Json(name = "id")
    val id: String,
)

@JsonClass(generateAdapter = true)
data class HubPaginationDto(
    @field:Json(name = "page_size")
    val pageSize: Int,

    @field:Json(name = "page")
    val page: Int,

    @field:Json(name = "total")
    val total: Int
)
