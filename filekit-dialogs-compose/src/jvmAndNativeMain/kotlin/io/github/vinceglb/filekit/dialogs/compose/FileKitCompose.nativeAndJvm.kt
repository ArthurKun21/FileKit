package io.github.vinceglb.filekit.dialogs.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.FileKitDialogSettings
import io.github.vinceglb.filekit.dialogs.openFileSaver
import kotlinx.coroutines.launch

/**
 * Creates a launcher for the file saver dialog.
 *
 * @param dialogSettings Platform-specific settings for the dialog.
 * @param onResult Callback to receive the result of the file saver dialog.
 * @return A [SaverResultLauncher] instance.
 */
@Composable
internal actual fun rememberPlatformFileSaverLauncher(
    dialogSettings: FileKitDialogSettings,
    onResult: (PlatformFile?) -> Unit,
): SaverResultLauncher {
    val coroutineScope = rememberCoroutineScope()
    val currentDialogSettings by rememberUpdatedState(dialogSettings)
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
