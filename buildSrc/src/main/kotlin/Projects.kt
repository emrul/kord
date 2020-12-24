import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.project

val DependencyHandlerScope.common get() = project(":common")
val DependencyHandlerScope.rest get() = project(":rest")
val DependencyHandlerScope.gateway get() = project(":gateway")
val DependencyHandlerScope.core get() = project(":core")

object Library {
    private const val releaseVersion = "0.7.0"

    const val group = "dev.kord"
    val version = System.getenv("RELEASE_TAG") ?: releaseVersion
    const val description = "Idiomatic Kotlin Wrapper for The Discord API"

    /**
     * Whether the current API is considered stable, and should be compared to the 'golden' API dump.
     *
     * Settings this flag to `false` disables the `apiCheck` tasks that compares binary compatibility.
     * Whenever a new development cycle starts, this flag should be set to `false`, and set to
     * `true` together with a new API dump whenever that cycle ends.
     */
    const val stableApi: Boolean = false
}