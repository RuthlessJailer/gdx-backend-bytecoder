package com.squins.gdx.backends.bytecoder.preloader

import org.apache.tika.Tika
import java.io.File


class PreloaderBundleGenerator(private val assetSourceDirectory: File, private val outputDirectory:File) {

    private val assetTypeLookup: AssetTypeLookup = DefaultAssetTypeLookup()
    private val tika = Tika()

    fun generate() {
        outputDirectory.mkdirs()
        File(outputDirectory, "assets.txt")
                .writeText(
                        generateAssets(assetSourceDirectory).joinToString("\n") {
                            convertAssetToLine(it)
                        }
                )

        assetSourceDirectory.copyRecursively(outputDirectory, true)
    }

    private fun convertAssetToLine(it: Asset) = it.type.code + ":" + it.file + ":" + it.sizeInBytes + ":" + it.mimeType + ":" + if(it.shouldPreload) { "1" } else {"0"}

    fun generateAssets(directory: File): List<Asset>
            = directory.walk()
            .filter { it.isFile }
            .map {
                Asset(
                        file=it.canonicalPath.let { f ->
                            val dir = directory.canonicalPath
                            if (f.startsWith(dir))
                                f.substring(dir.length + 1).replace('\\', '/')
                            else f
                        },
                        type= getAssetType(it),
                        sizeInBytes = it.length(),
                        mimeType = tika.detect(it),
                        preloadEnabled = true
                )
            }
            .toList()

    fun getAssetType(file: File) : AssetType {
        return assetTypeLookup.getType(file)
    }

}