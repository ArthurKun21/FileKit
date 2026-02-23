package io.github.vinceglb.filekit

import io.github.vinceglb.filekit.exceptions.FileKitException
import kotlinx.cinterop.UnsafeNumber
import platform.Foundation.NSBundle
import platform.Foundation.NSData
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

@OptIn(UnsafeNumber::class)
public actual val FileKit.filesDir: PlatformFile
    get() = NSFileManager
        .defaultManager
        .URLsForDirectory(NSDocumentDirectory, NSUserDomainMask)
        .firstOrNull()
        ?.let { it as NSURL? }
        ?.let(::PlatformFile)
        ?: throw FileKitException("Could not find files directory")

public actual val FileKit.projectDir: PlatformFile
    get() = PlatformFile(nsUrl = NSBundle.mainBundle.bundleURL)
        .parent()
        ?.parent()
        ?.parent()
        ?.parent()
        ?: throw FileKitException("Unable to find project directory")

public actual suspend fun FileKit.saveImageToGallery(
    bytes: ByteArray,
    filename: String,
): Result<Unit> = Result.failure(FileKitException("saveImageToGallery is not supported on watchOS"))

public actual suspend fun FileKit.saveVideoToGallery(
    file: PlatformFile,
    filename: String,
): Result<Unit> = Result.failure(FileKitException("saveVideoToGallery is not supported on watchOS"))

internal actual fun compress(
    nsData: NSData,
    quality: Int,
    maxWidth: Int?,
    maxHeight: Int?,
    imageFormat: ImageFormat,
): NSData = throw FileKitException("compressImage is not supported on watchOS")
