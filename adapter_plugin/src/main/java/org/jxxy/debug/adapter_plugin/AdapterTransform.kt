package org.jxxy.debug.adapter_plugin

import com.android.build.api.transform.QualifiedContent
import com.android.build.gradle.internal.pipeline.TransformManager
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import java.io.InputStream
import java.io.OutputStream

class AdapterTransform : BaseCustomTransform(false) {
    private val list = ArrayList<ViewHolderInfo>()

    override fun provideFunction(): (InputStream, OutputStream) -> Unit =
        { fis, fos ->
            val classReader = ClassReader(fis)
            val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_FRAMES)
            val classVisitor = ViewHolderVisitor(classWriter, list)
            classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
            fos.write(classWriter.toByteArray())
        }

    override fun getName(): String = "AdapterTransform"

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> = TransformManager.CONTENT_CLASS

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> = TransformManager.SCOPE_FULL_PROJECT

    override fun classFilter(className: String): Boolean {
        val a =
            !className.contains(
                "/",
            ) &&
                !className.contains(
                    "$",
                ) && className.endsWith(".class") && className != "BuildConfig.class" && !className.startsWith("R.") &&
                className != "module-info.class"
        if (a) println(className)
        return a
    }
}
