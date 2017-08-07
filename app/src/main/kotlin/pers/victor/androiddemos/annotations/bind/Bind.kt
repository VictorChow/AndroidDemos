package pers.victor.androiddemos.annotations.bind

import android.app.Activity
import android.support.v4.app.Fragment
import android.view.View

/**
 * Created by victor on 16-7-23.
 */
object Bind {

    @JvmStatic fun bind(o: Any) {
        when (o) {
            is Activity -> bind(o, o.window.decorView)
            is Fragment -> bind(o, o.view!!)
        }
    }

    @JvmStatic fun bind(o: Any, view: View) {
        //bind view
        o.javaClass.declaredFields.forEach { field ->
            field.annotations.forEach { annotation ->
                if (annotation.annotationClass == BindView::class) {
                    val id = field.getAnnotation(BindView::class.java).value
                    field.isAccessible = true
                    field.set(o, view.findViewById(id))
                }
            }
        }

        //bind onClick
        o.javaClass.declaredMethods.forEach { method ->
            method.annotations.forEach annotationForeach@ { annotation ->
                if (method.parameterTypes.size != 1 || (method.parameterTypes.size == 1 && method.parameterTypes[0] != View::class.java))
                    return@annotationForeach
                if (annotation.annotationClass == BindClick::class) {
                    val ids = method.getAnnotation(BindClick::class.java).value
                    ids.forEach { id ->
                        method.isAccessible = true
                        val v = view.findViewById<View>(id)
                        v.setOnClickListener { method(o, v) }
                    }
                }
            }
        }
    }
}