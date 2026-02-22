@file:Suppress("ktlint:standard:function-naming", "TestFunctionName")

package io.github.vinceglb.filekit.dialogs

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class FileSaverNameTest {
    @Test
    fun normalizeFileSaverExtension_whenNull_returnsNull() {
        assertNull(normalizeFileSaverExtension(null))
    }

    @Test
    fun normalizeFileSaverExtension_whenBlank_returnsNull() {
        assertNull(normalizeFileSaverExtension(""))
        assertNull(normalizeFileSaverExtension("   "))
        assertNull(normalizeFileSaverExtension("."))
        assertNull(normalizeFileSaverExtension("..."))
    }

    @Test
    fun normalizeFileSaverExtension_whenPrefixedWithDot_removesLeadingDotsAndWhitespace() {
        assertEquals("pdf", normalizeFileSaverExtension(".pdf"))
        assertEquals("pdf", normalizeFileSaverExtension(" .pdf "))
        assertEquals("pdf", normalizeFileSaverExtension("..pdf"))
    }

    @Test
    fun buildFileSaverSuggestedName_whenExtensionNullOrBlank_returnsSuggestedName() {
        assertEquals("document", buildFileSaverSuggestedName("document", null))
        assertEquals("document", buildFileSaverSuggestedName("document", ""))
        assertEquals("document", buildFileSaverSuggestedName("document", "   "))
        assertEquals("document", buildFileSaverSuggestedName("document", "."))
    }

    @Test
    fun buildFileSaverSuggestedName_whenExtensionProvided_appendsSingleDot() {
        assertEquals("document.pdf", buildFileSaverSuggestedName("document", "pdf"))
        assertEquals("document.pdf", buildFileSaverSuggestedName("document", ".pdf"))
        assertEquals("document.pdf", buildFileSaverSuggestedName("document", " .pdf "))
    }
}
