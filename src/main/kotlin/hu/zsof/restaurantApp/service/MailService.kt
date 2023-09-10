package hu.zsof.restaurantApp.service

import hu.zsof.restaurantApp.model.MyUser
import hu.zsof.restaurantApp.util.RestaurantConfigurations
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import javax.mail.Message

@Service
class MailService constructor(
        private val javaMailSender: JavaMailSender,
        private val restaurantConfigurations: RestaurantConfigurations
) {
    companion object {
        const val EMAIL_FROM = "taprakihh@gmail.com"
    }


    fun sendVerifyRegisterEmail(user: MyUser) {
        val mimeMessage = javaMailSender.createMimeMessage()
        mimeMessage.setFrom(EMAIL_FROM)
        mimeMessage.subject = "Verify your email"
        mimeMessage.addRecipients(Message.RecipientType.TO, user.email)

        val messageTemplate = restaurantConfigurations.verifyTemplate?.inputStream
        if (messageTemplate != null) {
            var content = String(messageTemplate.readAllBytes())
            content = content.replace("[USERNAME]", user.name)
            content = content.replace("[ID]", user.id.toString())
            content = content.replace("[SECRET]", user.verificationSecret)

            val helper = MimeMessageHelper(mimeMessage, true)
            helper.setText(content, true)

            javaMailSender.send(mimeMessage)
        } else {
            throw Exception("message template null")
        }
    }

}