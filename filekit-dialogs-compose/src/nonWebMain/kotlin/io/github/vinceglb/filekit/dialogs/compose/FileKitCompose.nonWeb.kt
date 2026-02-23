package io.github.vinceglb.filekit.dialogs.compose

import androidx.compose.runtime.Composable
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.FileKitDialogSettings

/**
 * Creates and remembers a [PickerResultLauncher] for picking a directory.
 *
 * @param directory The initial directory. Supported on desktop platforms.
 * @param dialogSettings Platform-specific settings for the dialog.
 * @param onResult Callback invoked with the picked directory, or null if cancelled.
 * @return A [PickerResultLauncher] that can be used to launch the picker.
 */
@Composable
public expect fun rememberDirectoryPickerLauncher(
    directory: PlatformFile? = null,
    dialogSettings: FileKitDialogSettings = FileKitDialogSettings.createDefault(),
    onResult: (PlatformFile?) -> Unit,
): PickerResultLauncher

/**
 * Creates and remembers a [SaverResultLauncher] for saving a file.
 *
 * @param dialogSettings Platform-specific settings for the dialog.
 * @param onResult Callback invoked with the saved file path, or null if cancelled.
 * @return A [SaverResultLauncher] that can be used to launch the saver.
 */
@Composable
public fun rememberFileSaverLauncher(
    dialogSettings: FileKitDialogSettings,
    onResult: (PlatformFile?) -> Unit,
): SaverResultLauncher {
    // Init FileKit
    InitFileKit()
    return rememberPlatformFileSaverLauncher(
        dialogSettings = dialogSettings,
        onResult = onResult,
    )
}

@Composable
internal expect fun rememberPlatformFileSaverLauncher(
    dialogSettings: FileKitDialogSettings,
    onResult: (PlatformFile?) -> Unit,
): SaverResultLauncher
