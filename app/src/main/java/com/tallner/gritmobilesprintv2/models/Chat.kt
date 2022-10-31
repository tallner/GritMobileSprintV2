package com.tallner.gritmobilesprintv2.models


class Chat(
    val senderID: String,
    val receiverID: String,
    val message: String)
{
    constructor(): this("","", "")
}