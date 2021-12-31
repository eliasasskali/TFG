package com.eliasasskali.tfg

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}