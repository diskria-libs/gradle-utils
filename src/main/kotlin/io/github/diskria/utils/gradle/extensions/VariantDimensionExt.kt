package io.github.diskria.utils.gradle.extensions

import com.android.build.api.dsl.VariantDimension
import io.github.diskria.utils.kotlin.delegates.toAutoNamedPair
import io.github.diskria.utils.kotlin.extensions.common.className
import io.github.diskria.utils.kotlin.extensions.common.failWithDetails
import io.github.diskria.utils.kotlin.extensions.common.modifyIf
import io.github.diskria.utils.kotlin.extensions.common.primitiveTypeNameOrNull
import io.github.diskria.utils.kotlin.extensions.wrapWithDoubleQuote

fun VariantDimension.putBuildConfigs(configs: Map<String, Any>) {
    configs.forEach { (name, value) ->
        putBuildConfig(name, value)
    }
}

fun <T> VariantDimension.putBuildConfig(name: String, value: T) {
    val isString = value is String
    val type = value?.primitiveTypeNameOrNull() ?: run {
        if (isString) String::class.className()
        else failWithDetails {
            val name by name.toAutoNamedPair()
            val value by value?.javaClass?.name.toAutoNamedPair()
            listOf(name, value)
        }
    }
    buildConfigField(type, name, value.toString().modifyIf(isString) { it.wrapWithDoubleQuote() })
}
