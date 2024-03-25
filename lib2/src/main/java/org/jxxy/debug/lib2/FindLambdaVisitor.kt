package org.jxxy.debug.lib2

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Handle
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter

class FindLambdaVisitor(classVisitor: ClassVisitor, val list: MutableList<Pair<String, String>>) : ClassVisitor(
    Opcodes.ASM7,
    classVisitor,
) {
    private var interfaces: Array<out String>? = null
    private var name: String? = null
    private var superName: String? = null
    val interfaceName = "android/view/View\$OnClickListener"
    val methodName = "onClick"
    val interfaceSignSuffix = "L$interfaceName;"

    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?,
    ) {
        this.interfaces = interfaces
        this.name = name
        this.superName = superName
        super.visit(version, access, name, signature, superName, interfaces)
    }

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?,
    ): MethodVisitor {
        var mv = super.visitMethod(access, name, descriptor, signature, exceptions)
        mv =
            object : AdviceAdapter(Opcodes.ASM7, mv, access, name, descriptor) {
                override fun visitInvokeDynamicInsn(
                    name: String?,
                    descriptor: String?,
                    bootstrapMethodHandle: Handle?,
                    vararg bootstrapMethodArguments: Any?,
                ) {
                    super.visitInvokeDynamicInsn(
                        name,
                        descriptor,
                        bootstrapMethodHandle,
                        *bootstrapMethodArguments,
                    )
                    if (name == methodName && descriptor?.endsWith(interfaceSignSuffix) == true && bootstrapMethodArguments.size > 2) {
                        val any = bootstrapMethodArguments[1].toString()
                        val indexOf = any.lastIndexOf(".")
                        val substring = any.substring(indexOf + 1)
                        val indexOf1 = substring.indexOf('(')
                        val v1 = substring.substring(0, indexOf1)
                        val v2 = substring.substring(indexOf1).split(' ')[0]
                        val p = Pair<String, String>(v1, v2)
                        list.add(p)
                    }
                }
            }
        return mv
    }
}
