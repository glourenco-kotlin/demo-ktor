package com.ktor.io

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.client.HttpClient
import io.ktor.client.request.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async

fun Routing.root() {
    get("/") {
        call.respondText(text = "Hello ktor", ContentType.Text.Plain)
    }
}

fun Routing.rootPost(){


    post("/") {
        val routeRequest = call.receive<RouteRequest>()
        val client = HttpClient(CIO) {
            install(JsonFeature)
        }
        val dados = client.get<GitHubResponse>("https://api.github.com/users/${routeRequest.name}")


        call.respond(HttpStatusCode.Created, dados)
    }

    post("/async") {
        val routeRequest = call.receive<RouteRequest>()
        val client = HttpClient(CIO) {
            install(JsonFeature)
        }

        val firstRequest: Deferred<GitHubResponse> = async { client.get("https://api.github.com/users/${routeRequest.name}") }
        val secondRequest: Deferred<GitHubResponse> = async { client.get("https://api.github.com/users/${routeRequest.name}") }
        val firstRequestContent = firstRequest.await()
        val secondRequestContent = secondRequest.await()

        val list: List<GitHubResponse> = arrayListOf(firstRequestContent, secondRequestContent)


        call.respond(HttpStatusCode.Created, list)
    }
}