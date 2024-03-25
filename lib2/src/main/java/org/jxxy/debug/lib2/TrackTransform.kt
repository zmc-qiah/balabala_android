package org.jxxy.debug.lib2

import com.android.build.api.transform.QualifiedContent
import com.android.build.gradle.internal.pipeline.TransformManager
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import java.io.InputStream
import java.io.OutputStream

class TrackTransform() : BaseCustomTransform(true) {
    override fun provideFunction(): ((InputStream, OutputStream) -> Unit)? =
        { fis, fos ->
            val classReader = ClassReader(fis)
            val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
            val list = ArrayList<Pair<String, String>>()
            val classVisitor = FindLambdaVisitor(classWriter, list)
            classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
            classReader.accept(TrackClassVisitor(classVisitor, list), ClassReader.EXPAND_FRAMES)
            fos.write(classWriter.toByteArray())
        }

    override fun classFilter(className: String): Boolean =
        !className.startsWith("androidx") &&
            className.endsWith(".class") && className != "R.class" &&
            !className.startsWith(
                "R\$",
            ) && className != "BuildConfig.class" &&
            !className.startsWith(
                "org.jxxy.debug.sensors",
            ) && !className.startsWith("android.support") && !className.startsWith("org.gradle.")

    override fun getName(): String = "TrackTransform"

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> = TransformManager.CONTENT_JARS

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> = TransformManager.SCOPE_FULL_PROJECT
}
