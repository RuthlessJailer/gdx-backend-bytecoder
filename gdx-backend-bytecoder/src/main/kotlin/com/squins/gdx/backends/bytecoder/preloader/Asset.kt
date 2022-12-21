package com.squins.gdx.backends.bytecoder.preloader

data class Asset(val file: String,
                 val type: AssetType,
                 val sizeInBytes: Long,
                 val mimeType: String,
                 val preloadEnabled:Boolean) {

    val shouldPreload = preloadEnabled || file.startsWith("com/badlogic/")
    var succeed = false
        set(value) {
            if(value) {
                field = true
                bytesLoaded = sizeInBytes
            }
        }
    var failed = false
    var downloadStarted = false
    var bytesLoaded: Long = 0
}