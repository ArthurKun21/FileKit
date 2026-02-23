package io.github.vinceglb.filekit

import io.github.vinceglb.filekit.exceptions.FileKitException
import io.github.vinceglb.filekit.utils.calculateNewDimensions
import io.github.vinceglb.filekit.utils.runSuspendCatchingFileKit
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.AppKit.NSBitmapImageFileType
import platform.AppKit.NSBitmapImageRep
import platform.AppKit.NSImage
import platform.AppKit.NSImageCompressionFactor
import platform.AppKit.representationUsingType
import platform.Foundation.NSApplicationSupportDirectory
import platform.Foundation.NSBundle
import platform.Foundation.NSData
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSDownloadsDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSMakeRect
import platform.Foundation.NSMakeSize
import platform.Foundation.NSMoviesDirectory
import platform.Foundation.NSMusicDirectory
import platform.Foundation.NSPicturesDirectory
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

public actual val FileKit.filesDir: PlatformFile
    get() {
        val appSupportDir = NSFileManager
            .defaultManager
            .URLsForDirectory(NSApplicationSupportDirectory, NSUserDomainMask)
            .firstOrNull()
            ?.let { it as NSURL? }
            ?: throw FileKitException("Could not find Application Support directory")

        val bundleId = NSBundle.mainBundle.bundleIdentifier
            ?: throw FileKitException("Could not find bundle identifier")

        val appDir = appSupportDir.URLByAppendingPathComponent(bundleId, true)
            ?: throw FileKitException("Could not create app directory path")

        val filesDir = PlatformFile(nsUrl = appDir)

        if (!filesDir.exists()) {
            filesDir.createDirectories()
        }

        return filesDir
    }

public actual val FileKit.projectDir: PlatformFile
    get() = PlatformFile(".")

internal actual fun FileKit.platformUserDirectoryOrNull(type: FileKitUserDirectory): PlatformFile? =
    NSFileManager
        .defaultManager
        .URLsForDirectory(type.macosDirectoryType, NSUserDomainMask)
        .firstOrNull()
        ?.let { it as NSURL? }
        ?.let(::PlatformFile)

public actual suspend fun FileKit.saveImageToGallery(
    bytes: ByteArray,
    filename: String,
): Result<Unit> = runSuspendCatchingFileKit {
    FileKit.picturesDir / filename write bytes
}

public actual suspend fun FileKit.saveVideoToGallery(
    file: PlatformFile,
    filename: String,
): Result<Unit> = runSuspendCatchingFileKit {
    FileKit.videosDir / filename write file
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun compress(
    nsData: NSData,
    quality: Int,
    maxWidth: Int?,
    maxHeight: Int?,
    imageFormat: ImageFormat,
): NSData {
    val originalImage = NSImage(data = nsData)
    val (newWidth, newHeight) = calculateNewDimensions(
        originalImage.size.useContents { width }.toInt(),
        originalImage.size.useContents { height }.toInt(),
        maxWidth,
        maxHeight,
    )

    val resizedImage = originalImage.resizeTo(newWidth / 2, newHeight / 2)

    val imageRep = NSBitmapImageRep.imageRepWithData(resizedImage.TIFFRepresentation!!)
        ?: throw FileKitException("Failed to compress image")

    val storageType = when (imageFormat) {
        ImageFormat.JPEG -> NSBitmapImageFileType.NSBitmapImageFileTypeJPEG
        ImageFormat.PNG -> NSBitmapImageFileType.NSBitmapImageFileTypePNG
    }

    return imageRep.representationUsingType(
        storageType = storageType,
        properties = mapOf(NSImageCompressionFactor to (quality / 100.0)),
    ) ?: throw FileKitException("Failed to compress image")
}

@OptIn(ExperimentalForeignApi::class)
private fun NSImage.resizeTo(newWidth: Int, newHeight: Int): NSImage {
    val newSize = NSMakeSize(newWidth.toDouble(), newHeight.toDouble())

    val newImage = NSImage(newSize)
    newImage.lockFocus()
    this.drawInRect(
        NSMakeRect(
            x = 0.0,
            y = 0.0,
            w = newSize.useContents { width },
            h = newSize.useContents { height },
        ),
    )
    newImage.unlockFocus()

    return newImage
}

private val FileKitUserDirectory.macosDirectoryType: ULong
    get() = when (this) {
        FileKitUserDirectory.Downloads -> NSDownloadsDirectory
        FileKitUserDirectory.Pictures -> NSPicturesDirectory
        FileKitUserDirectory.Videos -> NSMoviesDirectory
        FileKitUserDirectory.Music -> NSMusicDirectory
        FileKitUserDirectory.Documents -> NSDocumentDirectory
    }
