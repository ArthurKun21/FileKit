package io.github.vinceglb.filekit.dialogs

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.utils.toBitsArray
import org.w3c.files.File
import org.w3c.files.FilePropertyBag

internal actual fun createTestPlatformFile(name: String): PlatformFile {
    val bytes = name.encodeToByteArray()
    val file = File(
        fileBits = bytes.toBitsArray(),
        fileName = name,
        options = FilePropertyBag(type = "text/plain"),
    )
    return PlatformFile(file)
}
