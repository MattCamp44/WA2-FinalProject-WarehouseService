package com.example.polito.demo.Services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender

@Service
class MailServiceImpl : MailService {

    @Autowired lateinit var mailSender : JavaMailSender

    @Autowired lateinit var message: SimpleMailMessage


    override fun sendMessage( productId: Long , warehouseId : Long) {

        //TODO make a coroutine to retrieve and send emails?

        var adminEmails : Vector<String> = retrieveAdminEmails()

        for(email in adminEmails)
        {
            message.setSubject("Warehouse service: Alarm level reached for product with Id $productId in warehouse $warehouseId")
            message.setText(
                " Dear admin," +
                        "" +
                        "Product $productId in warehouse $warehouseId has reached the alarm level you set " +
                        "" +
                        "Best regards" +
                        "Warehouse service"
            )
        message.setTo(email)
        }

        try{

            mailSender.send(message)

        } catch ( ex: Exception ){
            throw ex
        }

    }


    fun retrieveAdminEmails() : Vector<String> {

        //TODO make http request
        var mailvector : Vector<String> = Vector<String>()
        mailvector.add("webapp2confirmation@gmail.com")
        return mailvector


    }


}