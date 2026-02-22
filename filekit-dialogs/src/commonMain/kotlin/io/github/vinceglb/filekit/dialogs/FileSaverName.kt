package io.github.vinceglb.filekit.dialogs

internal fun normalizeFileSaverExtension(extension: String?): String? = extension
    ?.trim()
    ?.trimStart('.')
    ?.takeIf { it.isNotBlank() }

internal fun buildFileSaverSuggestedName(
    suggestedName: String,
    extension: String?,
): String {
    val normalizedExtension = normalizeFileSaverExtension(extension)
    return when (normalizedExtension) {
        null -> suggestedName
        else -> "$suggestedName.$normalizedExtension"
    }
}
