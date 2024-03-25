package org.jxxy.debug.adapter_plugin

import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class AdapterPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.getByType(BaseExtension::class.java)
        extension.registerTransform(AdapterTransform())
    }
}
