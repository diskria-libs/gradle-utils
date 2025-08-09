package io.github.diskria.utils.gradle.extensions

import io.github.diskria.utils.kotlin.extensions.common.KotlinClass
import io.github.diskria.utils.kotlin.extensions.common.failWithInvalidValue
import io.github.diskria.utils.kotlin.extensions.common.tryCatchOrNull
import io.github.diskria.utils.kotlin.extensions.toTypedOrNull
import org.gradle.api.Project
import org.gradle.api.Task
import java.io.File
import java.util.*

private val androidPluginIds: List<String> =
    listOf(
        "com.android.application",
        "com.android.library",
        "com.android.test",
        "com.android.dynamic-feature",
    )

fun Project.ifAndroid(block: () -> Unit) {
    androidPluginIds.forEach { pluginId ->
        plugins.withId(pluginId) { block() }
    }
}

inline fun <reified T> Project.getProperty(propertiesFile: File, key: String): T =
    getPropertyOrNull(propertiesFile, key) ?: failWithInvalidValue(key)

inline fun <reified T> Project.getPropertyOrNull(propertiesFile: File, key: String): T? =
    Properties().apply {
        propertiesFile.inputStream().use { load(it) }
    }.getProperty(key, null)?.toTypedOrNull<T>()

fun Project.getBuildDirectory(): File =
    layout.buildDirectory.asFile.get()

inline fun <reified T : Any> Project.getExtension(type: Class<T>): T? =
    tryCatchOrNull {
        extensions.findByType(type)
    }

fun <T : Task> Project.registerTasks(sealedClass: KotlinClass<T>) {
    sealedClass.sealedSubclasses.forEach { taskClass ->
        tasks.register(taskClass.getDisplayName(), taskClass.java)
    }
}
