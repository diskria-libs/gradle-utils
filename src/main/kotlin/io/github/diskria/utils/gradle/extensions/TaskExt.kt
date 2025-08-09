package io.github.diskria.utils.gradle.extensions

import io.github.diskria.utils.kotlin.extensions.common.KotlinClass
import io.github.diskria.utils.kotlin.extensions.common.failWithInvalidValue
import org.gradle.api.Action
import org.gradle.api.Task
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.TaskProvider

inline fun <reified T : Task> TaskContainer.registerTask(
    noinline configuration: T.() -> Unit = {}
): TaskProvider<T> =
    register(T::class.getDisplayName(), T::class.java, Action(configuration))

fun <T : Task> KotlinClass<T>.getDisplayName(): String =
    simpleName
        ?.removeSuffix("Task")
        ?.replaceFirstChar { it.lowercaseChar() }
        ?: failWithInvalidValue(simpleName)
