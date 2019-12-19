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
 *
 * In general, [action] should not contain any business logic and should not throw exceptions.
 *
 * @param action any side-effect, e.g. loggers and builders
 * @param f the function you actually want to call
 * @return which is same to the result of [f]
 */
inline fun <T> effect(action: (T) -> Unit, f: () -> T): T =
    runCatching(f).onSuccess(action).getOrThrow()

/**
 * TBD.
 * @param action TBD
 * @param f TBD
 * @return TBD
 */
inline fun <T> effectOnError(action: (Throwable) -> Unit, f: () -> T): T =
    runCatching(f).onFailure(action).getOrThrow()

/**
 * TBD.
 * @param action TBD
 * @param f TBD
 * @return TBD
 */
inline fun <T> effectOnResult(action: (Result<T>) -> Unit, f: () -> T): T =
    runCatching(f).also(action).getOrThrow()

/**
 * TBD.
 * @param action TBD.
 * @return higher-order function
 */
inline fun <T> buildEffect(crossinline action: (T) -> Unit): (() -> T) -> T =
    { f -> effect(action, f) }

/**
 * TBD.
 * @param action TBD
 * @return TBD
 */
inline fun <T> buildEffectOnError(crossinline action: (Throwable) -> Unit): (() -> T) -> T =
    { f -> effectOnError(action, f) }

/**
 * TBD.
 * @param action TBD
 * @return TBD
 */
inline fun <T> buildEffectOnResult(crossinline action: (Result<T>) -> Unit): (() -> T) -> T =
    { f -> effectOnResult(action, f) }
