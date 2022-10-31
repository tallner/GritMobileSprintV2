package com.tallner.gritmobilesprintv2.models


class User(
    val uid: String,
    val username: String,
    val profile: String,
    val cover: String,
    val status: String,
    val search: String,
    val facebook: String,
    val instagram: String,
    val website: String)
{
    constructor(): this("","", "","","","","",
        "", "")
}