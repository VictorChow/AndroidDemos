package demos.util.annotations.bus

/**
 * Created by victor on 16-7-23.
 */

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class BusMethod()