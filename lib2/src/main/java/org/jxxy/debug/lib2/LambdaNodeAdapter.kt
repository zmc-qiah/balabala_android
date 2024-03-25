@file:Suppress("ktlint:standard:no-wildcard-imports")

package org.jxxy.debug.lib2

import org.objectweb.asm.*
import org.objectweb.asm.tree.*

class LambdaNodeAdapter(api: Int, val classVisitor: ClassVisitor) : ClassNode(api) {
    init {
        this.cv = classVisitor
    }

    fun excludeList() = listOf("me/wsj/apm", "me/wsj/core")

    override fun visitEnd() {
        super.visitEnd()
        //            log("lambda: " + this.name, true)

        val shouldHookMethodList = mutableSetOf<String>()
        for (methodNode in this.methods) {
            if (methodNode.isStatic) {
                continue
            }

            // 判断方法内部是否有需要处理的 lambda 表达式
            val invokeDynamicInsnNodes = methodNode.findHookPointLambda()
            invokeDynamicInsnNodes.forEach {
                val handle = it.bsmArgs[1] as? Handle
                if (handle != null) {
                    shouldHookMethodList.add(handle.name + handle.desc)
                }
            }
        }
        if (shouldHookMethodList.isNotEmpty()) {
            for (methodNode in methods) {
                val methodNameWithDesc = methodNode.nameWithDesc
                if (shouldHookMethodList.contains(methodNameWithDesc)) {
                    /*val argumentTypes = Type.getArgumentTypes(methodNode.desc)
                    log("++++++++++++++: ${argumentTypes.size} + "+ methodNameWithDesc, true)
                    argumentTypes.forEach {
                        log(it.toString(), true)
                    }
                    log("-------------------"+ methodNameWithDesc, true)
                    val viewArgumentIndex = argumentTypes?.indexOfFirst {
                        it.descriptor == "Ljava/lang/Runnable;"
                    } ?: -1*/

                    val instructions = methodNode.instructions
                    if (instructions != null && instructions.size() > 0) {
                        val list = InsnList()
                        val type = Type.getMethodType(methodNode.desc)
                        list.add(VarInsnNode(Opcodes.ALOAD, type.argumentTypes.size - 1))
                        list.add(
                            MethodInsnNode(
                                Opcodes.INVOKESTATIC,
                                "org/jxxy/debug/sensors/SensorsBind",
                                "onTrackView",
                                "(Landroid/view/View;)V",
                                false,
                            ),
                        )
                        instructions.insert(list)
                    }
                }
            }
        }
        accept(cv)
    }

    val interfaceName = "android/view/View\$OnClickListener"
    val methodName = "onClick"
    val interfaceSignSuffix = "L$interfaceName;"

    private fun MethodNode.findHookPointLambda(): List<InvokeDynamicInsnNode> {
        val onClickListenerLambda =
            findLambda {
                val nodeName = it.name
                val nodeDesc = it.desc
                val find = nodeName == methodName && nodeDesc.endsWith(interfaceSignSuffix)
                return@findLambda find
            }
        return onClickListenerLambda
    }

    val MethodNode.nameWithDesc: String
        get() = name + desc

    val MethodNode.isStatic: Boolean
        get() = access and Opcodes.ACC_STATIC != 0

    fun MethodNode.findLambda(filter: (InvokeDynamicInsnNode) -> Boolean): List<InvokeDynamicInsnNode> {
        val handleList = mutableListOf<InvokeDynamicInsnNode>()
        val instructions = instructions?.iterator() ?: return handleList
        while (instructions.hasNext()) {
            val nextInstruction = instructions.next()
            if (nextInstruction is InvokeDynamicInsnNode) {
                if (filter(nextInstruction)) {
                    handleList.add(nextInstruction)
                }
            }
        }
        return handleList
    }
}
