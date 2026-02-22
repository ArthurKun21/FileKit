package io.github.vinceglb.filekit

import io.github.vinceglb.filekit.exceptions.FileKitException

/**
 * Standard user-facing directories on desktop platforms.
 */
public enum class FileKitUserDirectory {
    Downloads,
    Pictures,
    Videos,
    Music,
    Documents,
}

/**
 * Returns a platform-resolved user directory for [type], or null if unavailable.
 */
public fun FileKit.userDirectoryOrNull(type: FileKitUserDirectory): PlatformFile? =
    platformUserDirectoryOrNull(type)

/**
 * Returns a platform-resolved user directory for [type].
 *
 * @throws FileKitException when the directory cannot be resolved.
 */
public fun FileKit.userDirectory(type: FileKitUserDirectory): PlatformFile =
    resolveUserDirectoryOrThrow(type = type, resolver = ::platformUserDirectoryOrNull)

/**
 * Returns the downloads directory for the current user.
 */
public val FileKit.downloadsDir: PlatformFile
    get() = userDirectory(type = FileKitUserDirectory.Downloads)

/**
 * Returns the pictures directory for the current user.
 */
public val FileKit.picturesDir: PlatformFile
    get() = userDirectory(type = FileKitUserDirectory.Pictures)

/**
 * Returns the videos directory for the current user.
 */
public val FileKit.videosDir: PlatformFile
    get() = userDirectory(type = FileKitUserDirectory.Videos)

/**
 * @deprecated Use [downloadsDir] instead.
 */
@Deprecated(
    message = "Use downloadsDir instead.",
    replaceWith = ReplaceWith("downloadsDir"),
)
public val FileKit.downloadDir: PlatformFile
    get() = downloadsDir

/**
 * @deprecated Use [picturesDir] instead.
 */
@Deprecated(
    message = "Use picturesDir instead.",
    replaceWith = ReplaceWith("picturesDir"),
)
public val FileKit.pictureDir: PlatformFile
    get() = picturesDir

/**
 * @deprecated Use [videosDir] instead.
 */
@Deprecated(
    message = "Use videosDir instead.",
    replaceWith = ReplaceWith("videosDir"),
)
public val FileKit.videoDir: PlatformFile
    get() = videosDir

/**
 * Returns the music directory for the current user.
 */
public val FileKit.musicDir: PlatformFile
    get() = userDirectory(type = FileKitUserDirectory.Music)

/**
 * Returns the documents directory for the current user.
 */
public val FileKit.documentsDir: PlatformFile
    get() = userDirectory(type = FileKitUserDirectory.Documents)

internal expect fun FileKit.platformUserDirectoryOrNull(type: FileKitUserDirectory): PlatformFile?

internal fun resolveUserDirectoryOrThrow(
    type: FileKitUserDirectory,
    resolver: (FileKitUserDirectory) -> PlatformFile?,
): PlatformFile = resolver(type)
    ?: throw FileKitException("Could not resolve the ${type.name.lowercase()} directory on this platform.")
