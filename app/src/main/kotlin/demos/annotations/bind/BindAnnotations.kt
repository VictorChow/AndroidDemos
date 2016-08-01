package demos.annotations.bind

/**
 * Created by victor on 16-7-23.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class BindView(val value: Int = 0)

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class BindClick(val value: IntArray = intArrayOf())

