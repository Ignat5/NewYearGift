package org.example.project

class WasmPlatform: Platform {
    override val name: String = "Happy New Year!"
}

actual fun getPlatform(): Platform = WasmPlatform()