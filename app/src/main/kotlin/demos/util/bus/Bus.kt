package demos.util.bus

import android.os.Handler
import android.os.Looper


/**
 * Created by victor on 16-7-23.
 */
object Bus {
    private val subscribers = arrayListOf<Any>()
    private val handler = Handler(Looper.getMainLooper())

    @JvmStatic fun register(o: Any) {
        subscribers.add(o)
    }

    @JvmStatic fun unregister(o: Any) {
        subscribers.remove(o)
    }

    @JvmStatic fun postAnnotation(o: Any) {
        subscribers.forEach { subscriber ->
            subscriber.javaClass.declaredMethods.forEach { method ->
                method.annotations.forEach { annotation ->
                    if (annotation.annotationClass == BusMethod::class) {
                        if (method.parameterTypes.size == 1) {
                            if (method.parameterTypes[0] == o.javaClass) {
                                method.isAccessible = true
//                                if (Looper.myLooper() == Looper.getMainLooper())
//                                    method.invoke(subscriber, o)
//                                else
                                handler.post { method.invoke(subscriber, o) }
                            }
                        }
                    }
                }
            }
        }
    }

    @JvmStatic fun postReflect(o: Any) {
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