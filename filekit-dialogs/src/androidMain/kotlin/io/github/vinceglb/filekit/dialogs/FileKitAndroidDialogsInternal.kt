package io.github.vinceglb.filekit.dialogs

import android.webkit.MimeTypeMap

@FileKitDialogsInternalApi
public object FileKitAndroidDialogsInternal {
    public fun getMimeTypes(fileExtensions: Set<String>?): Array<String> {
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return fileExtensions
            ?.map {
                when (it) {
                    "csv" -> listOf(
                        "text/csv",
                        "application/csv",
                        "application/x-csv",
                        "text/comma-separated-values",
                        "text/x-comma-separated-values",
                        "text/x-csv",
                    )

                    else -> listOf(mimeTypeMap.getMimeTypeFromExtension(it))
                }
            }?.let { res -> res.flatten().mapNotNull { it } }
            ?.takeIf { it.isNotEmpty() }
            ?.toTypedArray()
            ?: arrayOf("*/*")
    }

    public fun getMimeType(fileExtension: String?): String {
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return fileExtension
            ?.let { mimeTypeMap.getMimeTypeFromExtension(it) }
            ?: "*/*"
    }

    public fun normalizeFileSaverExtension(extension: String?): String? =
        io.github.vinceglb.filekit.dialogs
            .normalizeFileSaverExtension(extension)

    public fun buildFileSaverSuggestedName(
        suggestedName: String,
        extension: String?,
    ): String = io.github.vinceglb.filekit.dialogs.buildFileSaverSuggestedName(
        suggestedName = suggestedName,
        extension = extension,
    )
}
