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

import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.*

class EffectTest {
    private val outputBuffer = ByteArrayOutputStream()
    private lateinit var originOut: PrintStream

    @BeforeTest
    fun setUp() {
        // Mocks the standard output
        originOut = System.out
        System.setOut(
            PrintStream(outputBuffer, true, Charsets.UTF_8.displayName())
        )
    }

    @AfterTest
    fun tearDown() {
        System.setOut(originOut)
    }

    /**
     * This test case shows the usage [effect] function.
     */
    @Test
    fun testLogExample1() {
        // Write a `log` function to print the result of any given function
        fun <T> log(f: () -> T): T {
            return effect({ x -> print("$x") }, f)
        }
        // The simple `add` function (with log of the result)
        fun add(x: Int, y: Int): Int = log {
            x + y
        }
        // Assert the result of add function
        val result = add(1, 2)
        assertEquals(expected = 3, actual = result)
        // Assert the log
        val output = outputBuffer.toByteArray().toString(Charsets.UTF_8)
        assertEquals(expected = "3", actual = output)
    }

    /**
     * Another usage of [effect] function.
     */
    @Test
    fun testLogExample2() {
        // Use `effect` function directly
        val result = effect(::print) {
            1 + 2
        }
        // Assert the result of add function
        assertEquals(expected = 3, actual = result)
        // Assert the log
        val output = outputBuffer.toByteArray().toString(Charsets.UTF_8)
        assertEquals(expected = "3", actual = output)
    }
}
