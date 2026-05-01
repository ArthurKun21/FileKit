package io.github.vinceglb.filekit.dialogs

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.fromWebDirectoryFiles
import kotlinx.coroutines.flow.Flow

internal actual suspend fun FileKit.platformOpenFilePicker(
    type: FileKitType,
    mode: PickerMode,
    directory: PlatformFile?,
    dialogSettings: FileKitDialogSettings,
): Flow<FileKitPickerState<List<PlatformFile>>> =
    openBrowserFileInput(
        type = type,
        multipleMode = mode is PickerMode.Multiple,
        directoryMode = false,
    )?.map { PlatformFile(it) }.toPickerStateFlow()

public actual suspend fun FileKit.openDirectoryPicker(
    directory: PlatformFile?,
    dialogSettings: FileKitDialogSettings,
): PlatformFile? {
    val fileList = openBrowserFileInput(
        type = FileKitType.File(),
        multipleMode = false,
        directoryMode = true,
    )
    return PlatformFile.fromWebDirectoryFiles(fileList.orEmpty())
}
