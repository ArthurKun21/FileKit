package io.github.vinceglb.filekit.utils

import kotlinx.coroutines.CancellationException

internal suspend inline fun <T> runSuspendCatchingFileKit(block: suspend () -> T): Result<T> = try {
    Result.success(block())
} catch (error: CancellationException) {
    throw error
} catch (error: Throwable) {
    Result.failure(error)
}
