package io.github.vinceglb.filekit.dialogs.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.FileKitDialogSettings
import io.github.vinceglb.filekit.dialogs.openDirectoryPicker
import io.github.vinceglb.filekit.dialogs.openFileSaver
import kotlinx.coroutines.launch

/**
 * Creates and remembers a [PickerResultLauncher] for picking a directory.
 *
 * @param directory The initial directory. Supported on desktop platforms.
 * @param dialogSettings Platform-specific settings for the dialog.
 * @param onResult Callback invoked with the picked directory, or null if cancelled.
 * @return A [PickerResultLauncher] that can be used to launch the picker.
 */
@Composable
public actual fun rememberDirectoryPickerLauncher(
    directory: PlatformFile?,
    dialogSettings: FileKitDialogSettings,
    onResult: (PlatformFile?) -> Unit,
): PickerResultLauncher {
    // Init FileKit
    InitFileKit()

    // Coroutine
    val coroutineScope = rememberCoroutineScope()
    val stableDialogSettings = rememberStableDialogSettings(dialogSettings)

    // Updated state
    val currentDirectory by rememberUpdatedState(directory)
    val currentDialogSettings by rememberUpdatedState(stableDialogSettings)
    val currentOnResult by rememberUpdatedState(onResult)

    // FileKit launcher
    val returnedLauncher = remember {
        PickerResultLauncher {
            coroutineScope.launch {
                val result = FileKit.openDirectoryPicker(
                    directory = currentDirectory,
                    dialogSettings = currentDialogSettings,
                )
                currentOnResult(result)
            }
        }
    }

    return returnedLauncher
}

@Composable
internal actual fun rememberPlatformFileSaverLauncher(
    dialogSettings: FileKitDialogSettings,
    onResult: (PlatformFile?) -> Unit,
): SaverResultLauncher {
    val coroutineScope = rememberCoroutineScope()
    val stableDialogSettings = rememberStableDialogSettings(dialogSettings)
    val currentDialogSettings by rememberUpdatedState(stableDialogSettings)
    val currentOnResult by rememberUpdatedState(onResult)

    return remember {
        SaverResultLauncher { suggestedName, extension, directory ->
            coroutineScope.launch {
                val result = FileKit.openFileSaver(
                    suggestedName = suggestedName,
                    extension = extension,
                    directory = directory,
                    dialogSettings = currentDialogSettings,
                )
                currentOnResult(result)
            }
        }
    }
}
