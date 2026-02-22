package io.github.vinceglb.filekit

import io.github.vinceglb.filekit.exceptions.FileKitException
import io.github.vinceglb.filekit.utils.Platform
import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class FileKitUserDirectoriesJvmTest {
    @Test
    fun defaultLinuxUserDirsConfig_returnsNullWhenConfigIsUnreadable() {
        val tempDir = Files.createTempDirectory("filekit-userdirs-test").toFile()
        val configDir = Files.createDirectory(tempDir.toPath().resolve("config")).toFile()
        Files.createDirectory(configDir.toPath().resolve("user-dirs.dirs"))

        val config = defaultLinuxUserDirsConfig { key ->
            when (key) {
                "HOME" -> tempDir.path
                "XDG_CONFIG_HOME" -> configDir.path
                else -> null
            }
        }

        assertNull(config)
    }

    @Test
    fun resolveJvmUserDirectoryPath_linuxPrefersEnvOverConfigAndFallback() {
        val env = mapOf(
            "HOME" to "/home/alice",
            "XDG_MUSIC_DIR" to "/mnt/media/music",
        )

        val resolved = resolveJvmUserDirectoryPath(
            type = FileKitUserDirectory.Music,
            platform = Platform.Linux,
            envProvider = env::get,
            linuxUserDirsConfigProvider = { "XDG_MUSIC_DIR=\"\$HOME/ConfigMusic\"" },
            windowsKnownFolderResolver = { null },
        )

        assertEquals("/mnt/media/music", resolved.normalizedPathString())
    }

    @Test
    fun resolveJvmUserDirectoryPath_linuxUsesConfigWhenEnvIsMissing() {
        val env = mapOf("HOME" to "/home/alice")

        val resolved = resolveJvmUserDirectoryPath(
            type = FileKitUserDirectory.Downloads,
            platform = Platform.Linux,
            envProvider = env::get,
            linuxUserDirsConfigProvider = { "XDG_DOWNLOAD_DIR=\"\${HOME}/dl\"" },
            windowsKnownFolderResolver = { null },
        )

        assertEquals("/home/alice/dl", resolved.normalizedPathString())
    }

    @Test
    fun resolveJvmUserDirectoryPath_linuxFallsBackToConventionalFolder() {
        val env = mapOf("HOME" to "/home/alice")

        val resolved = resolveJvmUserDirectoryPath(
            type = FileKitUserDirectory.Documents,
            platform = Platform.Linux,
            envProvider = env::get,
            linuxUserDirsConfigProvider = { null },
            windowsKnownFolderResolver = { null },
        )

        assertEquals("/home/alice/Documents", resolved.normalizedPathString())
    }

    @Test
    fun resolveJvmUserDirectoryPath_windowsUsesKnownFoldersBeforeFallback() {
        val env = mapOf("USERPROFILE" to "/users/alice")

        val resolved = resolveJvmUserDirectoryPath(
            type = FileKitUserDirectory.Documents,
            platform = Platform.Windows,
            envProvider = env::get,
            linuxUserDirsConfigProvider = { null },
            windowsKnownFolderResolver = { "/resolved/documents" },
        )

        assertEquals("/resolved/documents", resolved.normalizedPathString())
    }

    @Test
    fun resolveJvmUserDirectoryPath_windowsFallsBackToUserProfileWhenKnownFolderFails() {
        val env = mapOf("USERPROFILE" to "/users/alice")

        val resolved = resolveJvmUserDirectoryPath(
            type = FileKitUserDirectory.Videos,
            platform = Platform.Windows,
            envProvider = env::get,
            linuxUserDirsConfigProvider = { null },
            windowsKnownFolderResolver = { null },
        )

        assertEquals("/users/alice/Videos", resolved.normalizedPathString())
    }

    @Test
    fun resolveJvmUserDirectoryPath_returnsNullWhenHomeOrUserProfileMissing() {
        val linuxResolved = resolveJvmUserDirectoryPath(
            type = FileKitUserDirectory.Downloads,
            platform = Platform.Linux,
            envProvider = { null },
            linuxUserDirsConfigProvider = { null },
            windowsKnownFolderResolver = { null },
        )
        val windowsResolved = resolveJvmUserDirectoryPath(
            type = FileKitUserDirectory.Downloads,
            platform = Platform.Windows,
            envProvider = { null },
            linuxUserDirsConfigProvider = { null },
            windowsKnownFolderResolver = { null },
        )

        assertNull(linuxResolved)
        assertNull(windowsResolved)
    }

    @Test
    fun parseXdgUserDirsConfig_parsesValidLinesAndIgnoresInvalidOnes() {
        val parsed = parseXdgUserDirsConfig(
            """
            # This is a comment
            XDG_DOWNLOAD_DIR="${'$'}HOME/Downloads"
            XDG_MUSIC_DIR="${'$'}{HOME}/Music"
            XDG_DOCUMENTS_DIR=/tmp/docs
            MALFORMED
            XDG_EMPTY_DIR=
            """.trimIndent(),
        )

        assertEquals("\$HOME/Downloads", parsed["XDG_DOWNLOAD_DIR"])
        assertEquals("\${HOME}/Music", parsed["XDG_MUSIC_DIR"])
        assertEquals("/tmp/docs", parsed["XDG_DOCUMENTS_DIR"])
        assertEquals(null, parsed["MALFORMED"])
        assertEquals(null, parsed["XDG_EMPTY_DIR"])
    }

    @Test
    fun resolveUserDirectoryOrThrow_throwsWhenResolverReturnsNull() {
        assertFailsWith<FileKitException> {
            resolveUserDirectoryOrThrow(
                type = FileKitUserDirectory.Music,
                resolver = { null },
            )
        }
    }

    @Test
    fun fileKitUserDirectoryWrappers_matchTypedResolver() {
        assertEquals(
            FileKit.userDirectory(FileKitUserDirectory.Downloads).path,
            FileKit.downloadsDir.path,
        )
        assertEquals(
            FileKit.userDirectory(FileKitUserDirectory.Pictures).path,
            FileKit.picturesDir.path,
        )
        assertEquals(
            FileKit.userDirectory(FileKitUserDirectory.Videos).path,
            FileKit.videosDir.path,
        )
        assertEquals(
            FileKit.userDirectory(FileKitUserDirectory.Music).path,
            FileKit.musicDir.path,
        )
        assertEquals(
            FileKit.userDirectory(FileKitUserDirectory.Documents).path,
            FileKit.documentsDir.path,
        )
    }

    @Suppress("DEPRECATION")
    @Test
    fun fileKitDeprecatedSingularWrappers_remainCompatible() {
        assertEquals(
            FileKit.downloadsDir.path,
            FileKit.downloadDir.path,
        )
        assertEquals(
            FileKit.picturesDir.path,
            FileKit.pictureDir.path,
        )
        assertEquals(
            FileKit.videosDir.path,
            FileKit.videoDir.path,
        )
    }

    @Test
    fun fileKitUserDirectoryOrNull_returnsDirectoryForAllTypesOnJvm() {
        FileKitUserDirectory.entries.forEach { type ->
            val dirOrNull = FileKit.userDirectoryOrNull(type)
            assertNotNull(dirOrNull)
            assertEquals(dirOrNull.path, FileKit.userDirectory(type).path)
        }
    }
}

private fun Any?.normalizedPathString(): String? = this
    ?.toString()
    ?.replace('\\', '/')
