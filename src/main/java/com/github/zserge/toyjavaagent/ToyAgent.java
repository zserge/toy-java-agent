package com.github.zserge.toyjavaagent;

import java.lang.instrument.*;
import java.security.ProtectionDomain;
import org.objectweb.asm.*;

public class ToyAgent {
    public static void premain(String args, Instrumentation inst) throws Exception {
        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String name, Class<?> c,
                ProtectionDomain pd, byte[] bytecode) throws IllegalClassFormatException {
                if (!name.equals("Example")) {
                    return bytecode;
                }
                ClassReader cr = new ClassReader(bytecode);
                ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
                cr.accept(new ClassVisitor(Opcodes.ASM5, cw) {
                    @Override
                    public MethodVisitor visitMethod(int access, String method,
                            String desc, String signature, String[] exceptions) {
                        MethodVisitor v = cv.visitMethod(access, method, desc, signature, exceptions);
                        v.visitCode();
                        v.visitFieldInsn(
                            Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                        v.visitLdcInsn("Agent intercepted:" + name + "." + method);
                        v.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println",
                            "(Ljava/lang/String;)V", false);
                        if ("getAgentName".equals(method)) {
                            v.visitLdcInsn("Smith");
                            v.visitInsn(Opcodes.ARETURN);
                        }
                        v.visitEnd();
                        return v;
                    }

                }, 0);
                return cw.toByteArray();
            }
        });
    }
}

