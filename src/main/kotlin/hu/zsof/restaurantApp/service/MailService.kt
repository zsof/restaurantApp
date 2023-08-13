package hu.zsof.restaurantApp.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import java.nio.file.Files
import javax.mail.Message

@Service
class MailService constructor(private val javaMailSender: JavaMailSender) {
    companion object {
        const val EMAIL_FROM = "taprakihh@gmail.com"
    }
    @Value("classpath:/html/register.email.html")
    var resource: Resource? = null

    fun sendVerifyRegisterEmail(emailTo: String, userName: String, verificationToken: String) {
        val mimeMessage = javaMailSender.createMimeMessage()
        mimeMessage.setFrom(EMAIL_FROM)
        mimeMessage.subject = "Verify your email"
        mimeMessage.addRecipients(Message.RecipientType.TO, emailTo)

        val messageTemplate = resource?.file
        if(messageTemplate != null) {
            var content = String((Files.readAllBytes(messageTemplate.toPath())))
            content = content.replace("[USERNAME]", userName)
            content = content.replace("[TOKEN]", verificationToken)

            var helper = MimeMessageHelper(mimeMessage, true)
            helper.setText(content, true)

            javaMailSender.send(mimeMessage)
        }
    }

}