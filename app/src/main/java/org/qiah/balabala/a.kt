package org.qiah.balabala

import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter

fun main() {
    val classReader = ClassReader("org.qiah.balabala.activity.TestActivity2")
    val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
//    val classVisitor = LambdaNodeAdapter(Opcodes.ASM7, classWriter)
    val classVisitor = TrackClassVisitor(classWriter)
    classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
}
