package com.starbugs.wasalni_core.util.annotation


@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class ErrorStringId(val stringIdName: String)