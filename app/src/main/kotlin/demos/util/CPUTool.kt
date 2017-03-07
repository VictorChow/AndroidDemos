package demos.util

import java.io.*
import java.util.regex.Pattern

/**
 * Created by Victor on 16/3/26.
 */
object CPUTool {
    private val kCpuInfoMaxFreqFilePath = "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"

    val maxCpuFreq: Int
        get() {
            var result = 0
            var fr: FileReader? = null
            var br: BufferedReader? = null
            try {
                fr = FileReader(kCpuInfoMaxFreqFilePath)
                br = BufferedReader(fr)
                val text = br.readLine()
                result = Integer.parseInt(text.trim { it <= ' ' })
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                if (fr != null)
                    try {
                        fr.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                if (br != null)
                    try {
                        br.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

            }

            return result
        }

    private val kCpuInfoMinFreqFilePath = "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq"

    /* 获取CPU最小频率（单位KHZ） */
    val minCpuFreq: Int
        get() {
            var result = 0
            var fr: FileReader? = null
            var br: BufferedReader? = null
            try {
                fr = FileReader(kCpuInfoMinFreqFilePath)
                br = BufferedReader(fr)
                val text = br.readLine()
                result = Integer.parseInt(text.trim { it <= ' ' })
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                if (fr != null)
                    try {
                        fr.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                if (br != null)
                    try {
                        br.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

            }
            return result
        }

    private val kCpuInfoCurFreqFilePath = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq"

    /* 实时获取CPU当前频率（单位KHZ） */
    val curCpuFreq: Int
        get() {
            var result = 0
            var fr: FileReader? = null
            var br: BufferedReader? = null
            try {
                fr = FileReader(kCpuInfoCurFreqFilePath)
                br = BufferedReader(fr)
                val text = br.readLine()
                result = Integer.parseInt(text.trim { it <= ' ' })
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                if (fr != null)
                    try {
                        fr.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                if (br != null)
                    try {
                        br.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

            }
            return result
        }

    /* 获取CPU名字 */
    val cpuName: String?
        get() {
            var fr: FileReader? = null
            var br: BufferedReader? = null
            try {
                fr = FileReader("/proc/cpuinfo")
                br = BufferedReader(fr)
                val text = br.readLine()
                val array = text.split(":\\s+".toRegex(), 2).toTypedArray()
                for (i in array.indices) {
                }
                return array[1]
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                if (fr != null)
                    try {
                        fr.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                if (br != null)
                    try {
                        br.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

            }
            return null
        }

    //Private Class to display only CPU devices in the directory listing
    //Check if filename is "cpu", followed by a single digit number
    val numCores: Int
        get() {
            class CpuFilter : FileFilter {
                override fun accept(pathname: File): Boolean {
                    if (Pattern.matches("cpu[0-9]", pathname.name)) {
                        return true
                    }
                    return false
                }
            }

            try {
                val dir = File("/sys/devices/system/cpu/")
                val files = dir.listFiles(CpuFilter())
                return files.size
            } catch (e: Exception) {
                e.printStackTrace()
                return 1
            }

        }
}
