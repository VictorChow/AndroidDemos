package pers.victor.androiddemos.util.mail

import android.os.Environment
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

/**
 * Created by victor on 16-7-25.
 */

object SendEmail : Runnable {
    override fun run() {
        val data = arrayListOf<String>()
        data.add("水电费讲课开始带来肌肤快乐圣诞节")
        data.add("多少放上大高发生的更好")
        data.add("电饭锅电饭锅电饭锅的收费规定法规的说法")
        data.add("水电费水电费收到玩儿刚")
        data.add("截图邮件用挺好飞天花雕")
        data.add("多少放假就开始点放开速度恢复尽快")
        data.add("事里的卡积分扣阿尔我家放可乐加空附近")
        data.add("说的罚款交苦涩几分考了几块了")
        data.add("额外入库囧反馈结果看来发给记录付款就过了看")
        data.add("冻死了快飞机类似看到就疯狂里的世界法律会计打死快")
        data.add("速度反馈链接阿卡丽时间付款了啊减肥快乐")
        data.add("温柔ieuwrioeuwoi")
        data.add("撒打开了放假都款式了放假快乐盛大")

        val rootDir = File(Environment.getExternalStorageDirectory().absolutePath + "/CML/")
        if (!rootDir.exists())
            rootDir.mkdir()
        val userDir = File(Environment.getExternalStorageDirectory().absolutePath + "/CML/userId/")
        if (!userDir.exists())
            userDir.mkdir()
        val file = File(Environment.getExternalStorageDirectory().absolutePath + "/CML/userId/mail.txt")
        if (!file.exists())
            file.createNewFile()

        val fw = FileWriter(file)
        val bw = BufferedWriter(fw)
        data.forEach {
            bw.write(it)
            bw.newLine()
        }
        bw.close()
        fw.close()

        val mailInfo = MultiMailsender.MultiMailSenderInfo()
        mailInfo.mailServerHost = "smtp.163.com"
        mailInfo.mailServerPort = "25"
        mailInfo.isValidate = true
        mailInfo.userName = "sanjinzhou@163.com"
        mailInfo.password = "stsjs1218!!"//您的邮箱密码
        mailInfo.fromAddress = "sanjinzhou@163.com"
        mailInfo.toAddress = "sanjinzhou@163.com"
        mailInfo.subject = System.currentTimeMillis().toString()
        mailInfo.content = "sjiodfjiosdjf精神科的肌肤的口水"
        mailInfo.attachFileNames = arrayOf("${Environment.getExternalStorageDirectory().absoluteFile}/CML/userId/mail.txt")
//        mailInfo.receivers = arrayOf("devilinmind@qq.com") //其他收件人
//        mailInfo.ccs = arrayOf("devilinmind@qq.com") // 抄送
        MultiMailsender().sendMail(mailInfo)//发送文体格式
    }

}
