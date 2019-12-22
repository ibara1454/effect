/**
 * MIT License
 *
 * Copyright (c) 2019 Chiajun, Wang (ibara1454)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.ibara1454.effect

/**
 * [effect] allows you to make side-effects [action] with the result of given function [f].
 * If any exception is thrown by [f], then it will propagate immediately outside of [effect]
 * and the side-effect [action] will not be triggered.
 *
 * @param action any side-effect, e.g. loggers and builders.
 *  In general, [action] should not contain any business logic and should not throw exceptions
 * @param f the function you actually want to call
 * @return same as the return value of [f]
 */
inline fun <T> effect(action: (T) -> Unit, f: () -> T): T =
    runCatching(f).onSuccess(action).getOrThrow()

/**
 * Make side-effects [action] when the given function [f] throws exceptions.
 * The side-effect function [action] will only triggered if any exception is thrown by [f].
 * After that, the exception will propagate outside of [effectOnError].
 *
 * @param action any side-effect, e.g. loggers and builders.
 *  In general, [action] should not contain any business logic and should not throw exceptions
 * @param f the function you actually want to call
 * @return same as the return value of [f]
 */
inline fun <T> effectOnError(action: (Throwable) -> Unit, f: () -> T): T =
    runCatching(f).onFailure(action).getOrThrow()

/**
 * Make side-effects [action] after each invocation of given function [f].
 *
 * @param action any side-effect, e.g. loggers and builders.
 *  In general, [action] should not contain any business logic and should not throw exceptions
 * @param f the function you actually want to call
 * @return same as the return value of [f]
 */
@SinceKotlin("1.3")
inline fun <T> effectOnResult(action: (Result<T>) -> Unit, f: () -> T): T =
    runCatching(f).also(action).getOrThrow()

/**
 * Receive side-effect function [action] to build a high-order function which receives a function `f`
 * that we want to call actually. If any exception is thrown by `f`, then it will propagate immediately
 * to the caller and the side-effect [action] will not be triggered.
 * Note that this function is just returned the partial application on [action] of the two-arity
 * function [effect].
 *
 * @see [effect]
 * @param action any side-effect, e.g. loggers and builders
 * @return a higher-order function receives a function `f` and makes side-effect by using the return
 *  value of `f`
 */
inline fun <T> buildEffect(crossinline action: (T) -> Unit): (() -> T) -> T =
    { f -> effect(action, f) }

/**
 * Receive side-effect function [action] to build a high-order function which receives a function `f`
 * that we want to call actually. The side-effect function [action] will only triggered if any exception
 * is thrown by `f`. After that, the exception will propagate immediately to the caller.
 * Note that this function is just returned the partial application on [action] of the two-arity
 * function [effectOnError].
 *
 * @param action any side-effect, e.g. loggers and builders
 * @return a higher-order function receives a function `f` and makes side-effect if any exception is
 *  thrown by `f`
 */
inline fun <T> buildEffectOnError(crossinline action: (Throwable) -> Unit): (() -> T) -> T =
    { f -> effectOnError(action, f) }

/**
 * Receive side-effect function [action] to build a high-order function which receives a function `f`
 * that we want to call actually. The side-effect function [action] will triggered by either any exception
 * is thrown by `f` or the return value of `f`. If any exception is thrown by `f`, if will first trigger the
 * side-effect function [action], and after that, it will propagate to the caller.
 * Note that this function is just returned the partial application on [action] of the two-arity
 * function [effectOnResult].
 *
 * @param action any side-effect, e.g. loggers and builders
 * @return a higher-order function receives a function `f` and makes side-effect either any exception is
 *  thrown by `f` or `f` finished with success.
 */
@SinceKotlin("1.3")
inline fun <T> buildEffectOnResult(crossinline action: (Result<T>) -> Unit): (() -> T) -> T =
    { f -> effectOnResult(action, f) }
