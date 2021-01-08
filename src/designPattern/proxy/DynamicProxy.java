package designPattern.proxy;

import net.sf.cglib.proxy.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DynamicProxy {

    public static void main(String[] args) {
        final IProductor productor = new Productor();
        productor.product(12f);

        /**
         * 动态代理
         *    特点: 字节码随用随创建,随用随加载(动态创建代理对象)
         *    作用: 不修改源码的基础上对方法增强
         *    分类:  1.基于接口的动态代理(JDK自带)
         *          @see java.lang.reflect.Proxy#newProxyInstance(ClassLoader, Class[], InvocationHandler)
         *          @param ClassLoader 用于创建代理对象字节码,一般使用和被代理接口相同的类加载器
         *          @param Class[] 用于创建代理对象方法列表,一般使用被代理接口的方法列表
         *          @param InvocationHandler 用户需要实现增强的接口方法,即如何代理
         *          注: 被代理的类至少是实现了一个接口的类,没有则不能使用
         *          Exception in thread "main" java.lang.ClassCastException:
         *                                   com.sun.proxy.$Proxy0 cannot be cast to designPattern.proxy.IProductor
         *          2.基于子类的动态代理(第三方cglib包提供)
         *          @see net.sf.cglib.proxy.Enhancer#create(Class, Class[], CallbackFilter, Callback[])
         *          @param Class 用于指定被代理对象的字节码
         *          @param Callback 用户需要实现增强的方法,即如何代理
         *              @see net.sf.cglib.proxy.MethodInterceptor extends Callback   一般使用这个作为Callback
         *          注: 被代理的类不能是final类
         */



        IProductor productor_dynamic_implement_proxyer = (IProductor)Proxy.newProxyInstance(
                productor.getClass().getClassLoader(),
                productor.getClass().getInterfaces(),
                new InvocationHandler() {
            /**
             * 作用: 执行被代理对象(proxyer)任何接口方法都会经过该方法
             * @param proxy 被代理对象的引用
             * @return 和被代理对象方法相同的返回值
             */
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                /*TODO Method Strengthen*/
                System.out.println("中间商通过接口赚差价!");
                Float originalMoney = (Float)args[0];

                return method.invoke(productor,originalMoney*0.8f);
            }
        });
        productor_dynamic_implement_proxyer.product(12f);



        IProductor productor_dynamic_subclass_proxyer = (IProductor)Enhancer.create(productor.getClass(), new MethodInterceptor() {
            /**
             * 作用: 执行被代理对象(proxyer)任何方法都会经过该方法
             * @param methodProxy 当前执行方法的代理对象
             * @return 和被代理对象方法相同的返回值
             */
            @Override
            public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                /*TODO Method Strengthen*/
                System.out.println("中间商通过子类赚差价!");
                Float originalMoney = (Float)args[0];

                return method.invoke(productor,originalMoney*0.8f);
            }
        });
        productor_dynamic_subclass_proxyer.product(12f);
    }

}
