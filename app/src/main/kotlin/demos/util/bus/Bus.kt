package demos.util.bus


/**
 * Created by victor on 16-7-23.
 */
object Bus {
    private val subscribers = arrayListOf<Any>()

    fun register(o: Any) {
        subscribers.add(o)
    }

    fun unregister(o: Any) {
        subscribers.remove(o)
    }

    fun postAnnotation(o: Any) {
        subscribers.forEach { subscriber ->
            subscriber.javaClass.declaredMethods.forEach { method ->
                method.annotations.forEach { annotation ->
                    if (annotation.annotationClass == BusMethod::class) {
                        if (method.parameterTypes.size == 1) {
                            if (method.parameterTypes[0] == o.javaClass) {
                                method.isAccessible = true
                                method.invoke(subscriber, o)
                            }
                        }
                    }
                }
            }
        }
    }

    fun postReflect(o: Any) {
        subscribers.forEach { subscriber ->
            try {
                val method = subscriber.javaClass.getDeclaredMethod("onPostReflect", o.javaClass)
                method.isAccessible = true
                method.invoke(subscriber, o)
            } catch (e: Exception) {
            }
        }
    }
}