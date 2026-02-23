package io.github.vinceglb.filekit.dialogs

/**
 * Marks internal FileKit Dialogs APIs that may change without notice.
 *
 * This is intended for internal module coordination (for example, filekit-dialogs-compose).
 */
@RequiresOptIn(
    message = "This API is internal to FileKit modules and may change without notice.",
    level = RequiresOptIn.Level.WARNING,
)
public annotation class FileKitDialogsInternalApi
