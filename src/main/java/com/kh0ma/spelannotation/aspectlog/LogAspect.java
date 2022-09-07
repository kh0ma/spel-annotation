package com.kh0ma.spelannotation.aspectlog;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LogAspect {

    @Around("@annotation(com.kh0ma.spelannotation.aspectlog.Log)")
    public Object processMethodsAnnotatedWithProjectSecuredAnnotation(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Log projectSecuredAnnotation = method.getAnnotation(Log.class);
        Map<String, String> additionalParams = new HashMap<>();
        for (LogParam param : projectSecuredAnnotation.params()) {
            additionalParams.put(param.name(), (String) getDynamicValue(signature.getParameterNames(), joinPoint.getArgs(), param.spel()));
        }
        log.info(projectSecuredAnnotation.label() + " " + additionalParams);
        return joinPoint.proceed();
    }

    public static Object getDynamicValue(String[] parameterNames, Object[] args, String key) {
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();

        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }
        return parser.parseExpression(key).getValue(context, Object.class);
    }
}
