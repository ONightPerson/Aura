package com.lbz.aura

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.time.Duration.Companion.milliseconds

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class CoroutinesUnitTest {

    @Test
    fun coroutinesTest() {
        runBlocking {
            val job = launch {
                try {
                    println("开始延迟...")
                    delay(10000.milliseconds) // 等待 10 秒
                    println("延迟结束") // 这行不会打印
                } catch (e: CancellationException) {
                    println("检测到取消，主动退出: $e")
                    // 注意：这里捕获后必须重新抛出，或者让协程自然结束，否则取消信号会被“吞掉”
                    throw e
                }
            }

            delay(1000.milliseconds) // 让协程先运行
            println("1秒后取消任务")
            job.cancel() // 触发取消
            job.join() // 等待协程结束
        }
    }

    @Test
    fun simpleCoroutines() {
        runBlocking {
            try {
                loadAll()
            } catch (e: Exception) {
                println("捕获: $e")
            }
        }
    }

    suspend fun cancelChild() = coroutineScope {
        launch {
            val job = launch {
                println("child 1")
                delay(1000.milliseconds)
            }
            launch {
                delay(1000.milliseconds)
                println("child 2")
            }
            launch {
                delay(1000.milliseconds)
                println("child 3")
            }
            delay(500.milliseconds)
            job.cancelAndJoin()
        }
    }
}

suspend fun test() = coroutineScope {
    launch {
        delay(2000.milliseconds)
        println("delay end")
        launch {
            delay(3000.milliseconds)
            println("sub delay end")
        }
    }
    println("start")
}

suspend fun loadAll(): Nothing = coroutineScope {
    val a = async {  }
    val b = async { throw RuntimeException("B失败") }
    a.await()
    b.await()
}