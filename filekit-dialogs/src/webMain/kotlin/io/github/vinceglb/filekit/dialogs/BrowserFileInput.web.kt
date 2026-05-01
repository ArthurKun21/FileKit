package io.github.vinceglb.filekit.dialogs

import io.github.vinceglb.filekit.BrowserFile
import io.github.vinceglb.filekit.WebFile
import kotlinx.browser.document
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.w3c.dom.HTMLElement
import org.w3c.dom.asList
import org.w3c.files.FileList
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@OptIn(ExperimentalWasmJsInterop::class)
internal suspend fun openBrowserFileInput(
    type: FileKitType,
    multipleMode: Boolean,
    directoryMode: Boolean,
): List<WebFile.FileWrapper>? = withContext(Dispatchers.Default) {
    suspendCancellableCoroutine { continuation ->
        val inputElement = document.createElement("input")
        val input = inputElement as BrowserFileInputElement
        (inputElement as HTMLElement).style.display = "none"
        document.body?.appendChild(inputElement)

        input.apply {
            this.type = "file"
            accept = type.acceptAttribute
            multiple = multipleMode
            webkitdirectory = directoryMode
        }

        input.onchange = {
            try {
                val result = input.files
                    ?.asList()
                    ?.map { WebFile.FileWrapper(it.unsafeCast<BrowserFile>()) }

                continuation.resume(result)
            } catch (e: Throwable) {
                continuation.resumeWithException(e)
            } finally {
                document.body?.removeChild(inputElement)
            }
        }

        input.oncancel = {
            continuation.resume(null)
            document.body?.removeChild(inputElement)
        }

        input.click()
    }
}

private val FileKitType.acceptAttribute: String
    get() = when (this) {
        is FileKitType.Image -> {
            "image/*"
        }

        is FileKitType.Video -> {
            "video/*"
        }

        is FileKitType.ImageAndVideo -> {
            "image/*,video/*"
        }

        is FileKitType.File -> {
            extensions
                ?.joinToString(",") { ".$it" }
                .orEmpty()
        }
    }

@JsName("HTMLInputElement")
internal external interface BrowserFileInputElement {
    var accept: String
    val files: FileList?
    var multiple: Boolean
    var webkitdirectory: Boolean
    var type: String
    var value: String
    var onchange: (() -> Unit)?
    var oncancel: (() -> Unit)?

    fun click()
}
