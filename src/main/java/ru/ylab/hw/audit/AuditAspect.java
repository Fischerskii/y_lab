package ru.ylab.hw.audit;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import ru.ylab.hw.enums.ActionType;

import java.util.Arrays;

/**
 * This aspect is designed to intercept methods annotated with {@link Audit} and those within classes annotated
 *  * with {@link Audit}, and it performs auditing actions such as logging execution time and recording user actions.
 */
@Aspect
@Slf4j
public class AuditAspect {

    private final LoggerService loggerService;

    public AuditAspect(LoggerService loggerService) {
        this.loggerService = loggerService;
    }

    /**
     * Pointcut that matches all methods within classes annotated with {@link Audit}
     * or methods annotated with {@link Audit}.
     */
    @Around("execution(* *(..)) && (@within(Audit) || @annotation(Audit))")
    public Object logAndAudit(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object result = joinPoint.proceed();  // Выполнение метода

        long executionTime = System.currentTimeMillis() - start;
        log.info("{} executed in {} ms", joinPoint.getSignature(), executionTime);

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Audit audit = signature.getMethod().getAnnotation(Audit.class);

        if (audit == null) {
            audit = joinPoint.getTarget().getClass().getAnnotation(Audit.class);
        }

        if (audit != null) {
            ActionType actionType = audit.actionType();
            Object[] args = joinPoint.getArgs();
            loggerService.logAction("system", actionType, "Action performed with args: " + Arrays.toString(args));
        }

        return result;
    }
}
