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

import kotlin.test.*

class EffectTest {

    /**
     * Test for using [effect] on function which executed successfully.
     */
    @Test
    fun testEffectWhenSucceed() {
        val logContainer: MutableList<Int> = mutableListOf()
        // Write a `log` function to print the result of any given function
        val appendLog: (Int) -> Unit = { logContainer.add(it) }
        // The simple `add` function (with log of the result)
        fun add(x: Int, y: Int) = effect(appendLog) { x + y }
        // Assert the result of add function
        val result = add(1, 2)
        assertEquals(expected = 3, actual = result)
        // Assert the log
        assertEquals(expected = listOf(3), actual = logContainer)
    }

    /**
     * Test for using [effect] on function which throws errors.
     */
    @Test
    fun testEffectWhenFailed() {
        val logContainer: MutableList<Int> = mutableListOf()
        // Write a `log` function to print the result of any given function
        val appendLog: (Int) -> Unit = { logContainer.add(it) }
        // The simple `add` function (with log of the result)
        @Suppress("UNUSED_PARAMETER")
        fun add(x: Int, y: Int): Int = effect(appendLog) {
            error("Throw exception anyway")
        }
        // Assert the result of add function
        assertFailsWith<IllegalStateException> { add(1, 2) }
        // Assert the log
        assertEquals(expected = emptyList<Int>(), actual = logContainer)
    }

    /**
     * Test for using [effectOnError] on function which executed successfully.
     */
    @Test
    fun testEffectOnErrorWhenSucceed() {
        val logContainer: MutableList<String> = mutableListOf()
        // Write a `log` function to print the result of any given function
        val appendLog: (Throwable) -> Unit = { e -> e.message?.also { logContainer.add(it) } }
        // The simple `add` function (with log of the result)
        @Suppress("UNUSED_PARAMETER")
        fun add(x: Int, y: Int): Int = effectOnError(appendLog) { x + y }
        // Assert the result of add function
        val result = add(1, 2)
        assertEquals(expected = 3, actual = result)
        // Assert the log
        assertEquals(expected = emptyList<String>(), actual = logContainer)
    }

    /**
     * Test for using [effectOnError] on function which throws errors.
     */
    @Test
    fun testEffectOnErrorWhenFailed() {
        val logContainer: MutableList<String> = mutableListOf()
        // Write a `log` function to print the result of any given function
        val appendLog: (Throwable) -> Unit = { e -> e.message?.also { logContainer.add(it) } }
        // The simple `add` function (with log of the result)
        @Suppress("UNUSED_PARAMETER")
        fun add(x: Int, y: Int): Int = effectOnError(appendLog) {
            error("Throw exception anyway")
        }
        // Assert the result of add function
        assertFailsWith<IllegalStateException> { add(1, 2) }
        // Assert the log
        assertEquals(expected = listOf("Throw exception anyway"), actual = logContainer)
    }

    /**
     * Test for using [effectOnResult] on function which executed successfully.
     */
    @Test
    fun testEffectOnResultWhenSucceed() {
        val logContainer: MutableList<Int> = mutableListOf()
        // Write a `log` function to print the result of any given function
        val appendLog: (Result<Int>) -> Unit = { result -> result.onSuccess { logContainer.add(it) } }
        // The simple `add` function (with log of the result)
        @Suppress("UNUSED_PARAMETER")
        fun add(x: Int, y: Int): Int = effectOnResult(appendLog) { x + y }
        // Assert the result of add function
        val result = add(1, 2)
        assertEquals(expected = 3, actual = result)
        // Assert the log
        assertEquals(expected = listOf(3), actual = logContainer)
    }

    /**
     * Test for using [effectOnResult] on function which throws errors.
     */
    @Test
    fun testEffectOnResultWhenFailed() {
        val logContainer: MutableList<String> = mutableListOf()
        // Write a `log` function to print the result of any given function
        val appendLog: (Result<Int>) -> Unit = { result -> result.onFailure { e -> e.message?.also { logContainer.add(it) } } }
        // The simple `add` function (with log of the result)
        @Suppress("UNUSED_PARAMETER")
        fun add(x: Int, y: Int): Int = effectOnResult(appendLog) {
            error("Throw exception anyway")
        }
        // Assert the result of add function
        assertFailsWith<IllegalStateException> { add(1, 2) }
        // Assert the log
        assertEquals(expected = listOf("Throw exception anyway"), actual = logContainer)
    }

    /**
     * Test for using [buildEffect] on function which executed successfully.
     */
    @Test
    fun testBuildEffectWhenSucceed() {
        val logContainer: MutableList<Int> = mutableListOf()
        val appendLog = buildEffect { x: Int -> logContainer.add(x) }
        fun add(x: Int, y: Int): Int = appendLog { x + y }
        // Assert the result of add function
        val result = add(1, 2)
        assertEquals(expected = 3, actual = result)
        // Assert the log
        assertEquals(expected = listOf(3), actual = logContainer)
    }

    /**
     * Test for using [buildEffect] on function which throws errors.
     */
    @Test
    fun testBuildEffectWhenFailed() {
        val logContainer: MutableList<Int> = mutableListOf()
        val appendLog = buildEffect { x: Int -> logContainer.add(x) }
        @Suppress("UNUSED_PARAMETER")
        fun add(x: Int, y: Int): Int = appendLog {
            error("Throw exception anyway")
        }
        // Assert the result of add function
        assertFailsWith<IllegalStateException> { add(1, 2) }
        // Assert the log
        assertEquals(expected = emptyList<Int>(), actual = logContainer)
    }

    /**
     * Test for using [buildEffectOnError] on function which executed successfully.
     */
    @Test
    fun testBuildEffectOnErrorWhenSucceed() {
        val logContainer: MutableList<String> = mutableListOf()
        val appendLog = buildEffectOnError<Int> { e -> e.message?.also { logContainer.add(it) } }
        @Suppress("UNUSED_PARAMETER")
        fun add(x: Int, y: Int): Int = appendLog { x + y }
        // Assert the result of add function
        val result = add(1, 2)
        assertEquals(expected = 3, actual = result)
        // Assert the log
        assertEquals(expected = emptyList<String>(), actual = logContainer)
    }

    /**
     * Test for using [buildEffectOnError] on function which throws errors.
     */
    @Test
    fun testBuildEffectOnErrorWhenFailed() {
        val logContainer: MutableList<String> = mutableListOf()
        val appendLog = buildEffectOnError<Int> { e -> e.message?.also { logContainer.add(it) } }
        @Suppress("UNUSED_PARAMETER")
        fun add(x: Int, y: Int): Int = appendLog {
            error("Throw exception anyway")
        }
        // Assert the result of add function
        assertFailsWith<IllegalStateException> { add(1, 2) }
        // Assert the log
        assertEquals(expected = listOf("Throw exception anyway"), actual = logContainer)
    }

    /**
     * Test for using [buildEffectOnResult] on function which executed successfully.
     */
    @Test
    fun testBuildEffectOnResultWhenSucceed() {
        val logContainer: MutableList<Int> = mutableListOf()
        val appendLog = buildEffectOnResult<Int> { result -> result.onSuccess { logContainer.add(it) } }
        @Suppress("UNUSED_PARAMETER")
        fun add(x: Int, y: Int): Int = appendLog { x + y }
        // Assert the result of add function
        val result = add(1, 2)
        assertEquals(expected = 3, actual = result)
        // Assert the log
        assertEquals(expected = listOf(3), actual = logContainer)
    }

    /**
     * Test for using [buildEffectOnResult] on function which throws errors.
     */
    @Test
    fun testBuildEffectOnResultWhenFailed() {
        val logContainer: MutableList<String> = mutableListOf()
        val appendLog = buildEffectOnResult<Int> { result -> result.onFailure { e -> e.message?.also { logContainer.add(it) } } }
        @Suppress("UNUSED_PARAMETER")
        fun add(x: Int, y: Int): Int = appendLog {
            error("Throw exception anyway")
        }
        // Assert the result of add function
        assertFailsWith<IllegalStateException> { add(1, 2) }
        // Assert the log
        assertEquals(expected = listOf("Throw exception anyway"), actual = logContainer)
    }

}
