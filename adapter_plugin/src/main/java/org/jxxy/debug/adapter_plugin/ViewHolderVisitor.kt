package org.jxxy.debug.adapter_plugin

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class ViewHolderVisitor(cv: ClassVisitor, val list: MutableList<ViewHolderInfo>) : ClassVisitor(Opcodes.ASM7, cv) {
    private var flag = false

    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?,
    ) {
        if (superName == "org/jxxy/debug/simpleRv/MultipleViewHolder") {
            flag = true
        }
        println("access:$access, name:$name, signature:$signature,superName:$superName")
        super.visit(version, access, name, signature, superName, interfaces)
    }

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?,
    ): MethodVisitor {
        if (flag && name == "<init>") {
            println(name + " " + descriptor)
        }
        return super.visitMethod(access, name, descriptor, signature, exceptions)
    }
}
