package com.workonenight.winteambe.entity.annotations.anonymous;

import com.workonenight.winteambe.entity.interfaces.Anonymizable;
import com.workonenight.winteambe.utils.Utils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;


@Component
public class InterceptingMethods {

    public static Anonymizable validate(Anonymizable input) {
        try {
            for (Field field : input.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Anonymous.class)) {
                    if (input.shouldBeAnonymized()) {
                        field.setAccessible(true);
                        field.set(input, Utils.ANONYMOUS);
                    }
                }
            }
            return input;
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Could not validate input object", e);
        }
    }


    /*public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("Intercepting method: " + method.getName());
        System.out.println("AASDFJADHFSDBUKFGWCKBUHWGEBKUCWEGBKUHFCWEGBKUYCEWBGGWEKFEGWKU " + method.getName());
        if (objects[0] instanceof Anonymizable) {
            Anonymizable anonymizable = (Anonymizable) o;
            for (Annotation a : method.getAnnotations()) {
                if (a instanceof AnonymizedObject) {
                    return methodProxy.invoke(validate(anonymizable), objects);
                }
            }
        }
        return methodProxy.invoke(o, objects);
    }*/

    /*@Pointcut("@annotation(com.workonenight.winteambe.entity.annotations.anonymous.AnonymizedObject)")
    public void interceptAnonymizedObject() {
    }*/


    @Around("execution(* com.workonenight.winteambe.utils.Utils.toDto(..)) || @annotation(AnonymizedObject)")
    public Object invoke(final ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();

        ArrayList<Object> arrayResult = new ArrayList<>();
        for (Object curr : args) {
            if (curr instanceof Anonymizable) {
                Anonymizable anonymizable = (Anonymizable) curr;
                Anonymizable result = validate(anonymizable);
                arrayResult.add(result);
            }
        }
        return pjp.proceed(args);
    }


}
