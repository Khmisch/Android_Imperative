package com.example.android_imperative.network

object Server {
    val IS_TESTER = true

    init {
        System.loadLibrary("keys")
    }

    external fun getDevelopment(): String
    external fun getProduction(): String
}