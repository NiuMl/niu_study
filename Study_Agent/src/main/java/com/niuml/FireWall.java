package com.niuml;

import org.objectweb.asm.*;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.function.Function;

/***
 * @author niumengliang
 * Date:2024/9/30
 * Time:10:50
 */
public class FireWall implements ClassFileTransformer {

    private final Function<MethodVisitor, MethodVisitor> mvFun = mv -> new MethodVisitor(Opcodes.ASM7, mv) {
        @Override
        public void visitCode() {
            try {
                // 加载doDispatch方法的第一个参数
                // 如果varIndex是0，代表org/springframework/web/servlet/DispatcherServlet那个类的this
                mv.visitVarInsn(Opcodes.ALOAD, 1);
                mv.visitVarInsn(Opcodes.ALOAD, 2);
                // 执行MyInterceptor.beforeRequest 方法
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getInternalName(FireWallInterceptor.class), "beforeRequest",
                        Type.getMethodDescriptor(FireWallInterceptor.class.getMethod("beforeRequest", Object.class, Object.class)), false);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            super.visitCode();
        }
    };

    private final Function<ClassWriter, ClassVisitor> ccFun = cw -> new ClassVisitor(Opcodes.ASM7, cw) {
        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
            return name.equals("doDispatch") ? mvFun.apply(mv) : mv;
        }
    };

    @Override
    public byte[] transform(ClassLoader loader, String className,
                            Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) throws IllegalClassFormatException {
        if (className.equals("org/springframework/web/servlet/DispatcherServlet")) {
            System.out.println("拦截到DispatcherServlet");
            ClassReader cr = new ClassReader(classfileBuffer);
            ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
            ClassVisitor cv = ccFun.apply(cw);
            cr.accept(cv, 0);
            return cw.toByteArray();
        }
        return ClassFileTransformer.super.transform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
    }
}
