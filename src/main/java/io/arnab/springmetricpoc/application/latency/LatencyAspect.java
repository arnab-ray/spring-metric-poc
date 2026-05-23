package io.arnab.springmetricpoc.application.latency;

import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class LatencyAspect {

    private final MeterRegistry meterRegistry;

    @Around("@annotation(measureLatency)")
    public Object measure(
            ProceedingJoinPoint pjp,
            MeasureLatency measureLatency) throws Throwable {

        String name = measureLatency.value().isEmpty()
                ? pjp.getSignature().getName()
                : measureLatency.value();

        long start = System.currentTimeMillis();

        try {
            return pjp.proceed();  // actual method call
        } finally {
            long time = System.currentTimeMillis() - start;
            log.info("Latency for {} : {} ms", name, time);
            DistributionSummary
                    .builder(name)
                    .publishPercentiles(0.90, 0.95, 0.99, 0.999)
                    .register(meterRegistry)
                    .record(time);
        }
    }

}
