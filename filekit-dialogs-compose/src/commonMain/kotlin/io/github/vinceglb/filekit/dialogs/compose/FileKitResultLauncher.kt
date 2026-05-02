package io.github.vinceglb.filekit.dialogs.compose

import io.github.vinceglb.filekit.PlatformFile

/**
 * Launcher for the file picker.
 */
public class PickerResultLauncher(
    private val onLaunch: () -> Unit,
) {
    /**
     * Launches the file picker.
     */
    public fun launch() {
        onLaunch()
    }
}

/**
 * Launcher for the file saver.
 */
public class SaverResultLauncher(
    private val onLaunch: (
        suggestedName: String,
        defaultExtension: String?,
        allowedExtensions: Set<String>?,
        directory: PlatformFile?,
    ) -> Unit,
) {
    /**
     * Launches the file saver dialog.
     *
     * [defaultExtension] controls the suggested/default extension for the
     * generated file name. [allowedExtensions] controls native save dialog
     * filters where the platform supports them; apps that require a specific
     * output format should still validate the returned file extension.
     *
     * @param suggestedName The suggested name for the file.
     * @param defaultExtension The default file extension without the dot.
     * @param allowedExtensions Allowed file extensions for the native save dialog.
     * @param directory The initial directory (optional, supported on desktop).
     */
    public fun launch(
        suggestedName: String,
        defaultExtension: String? = null,
        allowedExtensions: Set<String>? = null,
        directory: PlatformFile? = null,
    ) {
        onLaunch(suggestedName, defaultExtension, allowedExtensions, directory)
    }

    /**
     * Launches the file saver dialog.
     *
     * @param suggestedName The suggested name for the file.
     * @param extension The default file extension without the dot.
     * @param directory The initial directory (optional, supported on desktop).
     */
    @Deprecated(
        message = "Use defaultExtension. The extension parameter is a default extension hint, not a filter.",
        replaceWith = ReplaceWith(
            "launch(" +
                "suggestedName = suggestedName, " +
                "defaultExtension = extension, " +
                "directory = directory" +
                ")",
        ),
    )
    public fun launch(
        suggestedName: String,
        extension: String? = null,
        directory: PlatformFile? = null,
    ) {
        launch(
            suggestedName = suggestedName,
            defaultExtension = extension,
            directory = directory,
        )
    }
}
