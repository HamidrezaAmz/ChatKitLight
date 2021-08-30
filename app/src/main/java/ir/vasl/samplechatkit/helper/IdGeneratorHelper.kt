package ir.vasl.samplechatkit.helper

import kotlin.random.Random

class IdGeneratorHelper {

    companion object {

        private var lastNumber = 1000

        fun getRandomStringId(): String {
            val size = 10
            val source = "A1BCDEF4G0H8IJKLM7NOPQ3RST9UVWX52YZab1cd60ef2ghij3klmn49opq5rst6uvw7xyz8"
            return (source).map { it }.shuffled().subList(0, size).joinToString("")
        }

        fun getRandomIntId(): Int {
            val min = 10000
            val max = 99999
            return Random.nextInt(max - min + 1) + min
        }

        fun getIncreasingId(): Int {
            return lastNumber++
        }
    }

}