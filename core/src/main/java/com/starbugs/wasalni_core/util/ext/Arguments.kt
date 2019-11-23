@file:Suppress("UNCHECKED_CAST", "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
package com.starbugs.wasalni_core.util.ext

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import java.io.Serializable

/**
 * Start an Activity for given class T and allow to work on intent with "run" lambda function
 */
fun Fragment.withArguments(vararg arguments: Pair<String, Serializable>): Fragment {
    val bundle = android.os.Bundle()
    arguments.forEach { bundle.putSerializable(it.first, it.second) }
    this.arguments = bundle
    return this
}

/**
 * Retrieve property from intent
 */
fun <T : Any> FragmentActivity.argument(key: String) = kotlin.lazy { intent.extras[key] as T }

/**
 * Retrieve property with default value from intent
 */
fun <T : Any> FragmentActivity.argument(key: String, defaultValue: T? = null) = kotlin.lazy {
    intent.extras[key] as? T ?: defaultValue
}

/**
 * Retrieve property from intent
 */
fun <T : Any> Fragment.argument(key: String) = kotlin.lazy { arguments?.get(key) as T }

/**
 * Retrieve property with default value from intent
 */
fun <T : Any> Fragment.argument(key: String, defaultValue: T? = null) = kotlin.lazy {
    arguments?.get(key)  as? T ?: defaultValue
}
