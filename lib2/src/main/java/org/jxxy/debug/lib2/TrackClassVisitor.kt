package org.jxxy.debug.lib2

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter

class TrackClassVisitor(classVisitor: ClassVisitor, val list: List<Pair<String, String>>) : ClassVisitor(Opcodes.ASM7, classVisitor) {
    private var interfaces: Array<out String>? = null
    private var name: String? = null
    private var superName: String? = null

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
        var visitMethod = super.visitMethod(access, name, descriptor, signature, exceptions)
        if ((name == "onClick" && interfaces?.contains("android/view/View\$OnClickListener") == true) || (isLambda(name, descriptor))) {
            visitMethod =
                object : AdviceAdapter(
                    Opcodes.ASM7,
                    visitMethod,
                    access,
                    name,
                    descriptor,
                ) {
                    override fun onMethodExit(opcode: Int) {
                        super.onMethodExit(opcode)
                        mv.visitVarInsn(ALOAD, 1)
                        mv.visitMethodInsn(
                            INVOKESTATIC,
                            "org/jxxy/debug/sensors/SensorsBind",
                            "onTrackView",
                            "(Landroid/view/View;)V",
                            false,
                        )
                    }
                }
        } else if (name == "onResume" && superName == "androidx/fragment/app/Fragment" && descriptor == "()V") {
            visitMethod =
                object : AdviceAdapter(
                    Opcodes.ASM7,
                    visitMethod,
                    access,
                    name,
                    descriptor,
                ) {
                    override fun onMethodExit(opcode: Int) {
                        super.onMethodExit(opcode)
                        mv.visitVarInsn(ALOAD, 0)
                        mv.visitTypeInsn(CHECKCAST, "androidx/fragment/app/Fragment")
                        mv.visitMethodInsn(
                            INVOKESTATIC,
                            "org/jxxy/debug/sensors/SensorsBind",
                            "onFragmentResume",
                            "(Landroidx/fragment/app/Fragment;)V",
                            false,
                        )
                    }
                }
        } else if (name == "onPause" && superName == "androidx/fragment/app/Fragment" && descriptor == "()V") {
            visitMethod =
                object : AdviceAdapter(
                    Opcodes.ASM7,
                    visitMethod,
                    access,
                    name,
                    descriptor,
                ) {
                    override fun onMethodExit(opcode: Int) {
                        super.onMethodExit(opcode)
                        mv.visitVarInsn(ALOAD, 0)
                        mv.visitTypeInsn(CHECKCAST, "androidx/fragment/app/Fragment")
                        mv.visitMethodInsn(
                            INVOKESTATIC,
                            "org/jxxy/debug/sensors/SensorsBind",
                            "onFragmentPause",
                            "(Landroidx/fragment/app/Fragment;)V",
                            false,
                        )
                    }
                }
        } else if (name == "dismiss" && superName == "androidx/fragment/app/DialogFragment" && descriptor == "()V") {
            visitMethod =
                object : AdviceAdapter(
                    Opcodes.ASM7,
                    visitMethod,
                    access,
                    name,
                    descriptor,
                ) {
                    override fun onMethodExit(opcode: Int) {
                        super.onMethodExit(opcode)
                        mv.visitVarInsn(ALOAD, 0)
                        mv.visitTypeInsn(CHECKCAST, "androidx/fragment/app/DialogFragment")
                        mv.visitVarInsn(ALOAD, 0)
                        mv.visitMethodInsn(INVOKEVIRTUAL, "org/qiah/balabala/dialog/BaseDialog", "getTag", "()Ljava/lang/String;", false)
                        mv.visitMethodInsn(
                            INVOKESTATIC,
                            "org/jxxy/debug/sensors/SensorsBind",
                            "onDialogDismiss",
                            "(Landroidx/fragment/app/DialogFragment;Ljava/lang/String;)V",
                            false,
                        )
                    }
                }
        } else if (name == "show" && superName == "androidx/fragment/app/DialogFragment" && (descriptor == "(Landroidx/fragment/app/FragmentManager;Ljava/lang/String;)V" || descriptor == "(Landroidx/fragment/app/FragmentTransaction;Ljava/lang/String;)I")) {
            visitMethod =
                object : AdviceAdapter(
                    Opcodes.ASM7,
                    visitMethod,
                    access,
                    name,
                    descriptor,
                ) {
                    override fun onMethodExit(opcode: Int) {
                        super.onMethodExit(opcode)
                        mv.visitVarInsn(ALOAD, 0)
                        mv.visitTypeInsn(CHECKCAST, "androidx/fragment/app/DialogFragment")
                        mv.visitVarInsn(ALOAD, 0)
                        mv.visitMethodInsn(INVOKEVIRTUAL, "org/qiah/balabala/dialog/BaseDialog", "getTag", "()Ljava/lang/String;", false)
                        mv.visitMethodInsn(
                            INVOKESTATIC,
                            "org/jxxy/debug/sensors/SensorsBind",
                            "onDialogShow",
                            "(Landroidx/fragment/app/DialogFragment;Ljava/lang/String;)V",
                            false,
                        )
                    }
                }
        }
        return visitMethod
    }

    private fun isLambda(
        name: String?,
        descriptor: String?,
    ): Boolean {
        var res = false
        list.forEach {
            if (name == it.first && descriptor == it.second) res = true
        }
        return res
    }
}
