package at.sunilson.core

/*
Allows forcing when to be exhaustive.

Do exhaustive when (sealedClass) {
    is SealedClass.First -> doSomething()
    is SealedClass.Second -> doSomethingElse()
}

https://youtrack.jetbrains.com/issue/KT-12380#focus=streamItem-27-2909338.0-0
 */
object Do {
    inline infix fun <reified T> exhaustive(any: T) = any
}
