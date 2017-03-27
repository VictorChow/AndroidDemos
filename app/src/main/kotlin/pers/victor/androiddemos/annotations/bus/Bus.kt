package pers.victor.androiddemos.annotations.bus

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
                method.isAccessible = true
                if (method.getAnnotation(BusMethod::class.java) != null && method.parameterTypes.size == 1 && method.parameterTypes[0] == o.javaClass) {
                    try {
                        handler.post { method.invoke(subscriber, o) }
                    } catch (e: Exception) {
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