package io.github.vinceglb.filekit.utils

import io.github.vinceglb.filekit.BrowserFile
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.WebFile
import org.w3c.files.File
import org.w3c.files.FilePropertyBag
import kotlin.js.unsafeCast

@OptIn(ExperimentalWasmJsInterop::class)
actual fun createTestFile(
    name: String,
    content: String,
    relativePath: String?,
): PlatformFile {
    val jsArray = content.encodeToByteArray().toJsArray()
    val file = File(jsArray, name, FilePropertyBag(type = "text/plain"))
    return PlatformFile(
        WebFile.FileWrapper(
            file = file.unsafeCast<BrowserFile>(),
            path = relativePath.orEmpty(),
        ),
    )
}
