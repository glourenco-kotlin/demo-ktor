package com.ktor.io

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

@JsonDeserialize
@JsonIgnoreProperties(ignoreUnknown = true)
data class RouteRequest(val name: String)
