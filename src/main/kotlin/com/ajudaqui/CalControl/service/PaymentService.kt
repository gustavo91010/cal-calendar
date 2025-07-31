package com.ajudaqui.CalControl.service

import com.ajudaqui.CalControl.dto.EventCreateRequest
import com.ajudaqui.CalControl.dto.PaymentDto
import com.ajudaqui.CalControl.entity.Events
import java.io.File
import org.springframework.stereotype.Service

@Service
class PaymentService(private val eventService: EventsService) {

  fun create( paymentDto: PaymentDto): Events {
    val eventCreate =
            EventCreateRequest(
                    summary = "Contas a pagar",
                    description = factorDescription(paymentDto),
                    day = paymentDto.day
            )
    return eventService.create(paymentDto.email, eventCreate)
  }

  private fun factorDescription(paymentDto: PaymentDto): String {
    val template = File("payment.html").readText()

    val blocos =
            paymentDto.paymentData.joinToString("\n") { (id, description, value, status, link) ->
              """<li>
  <strong>Descrição:</strong> $description 
  <strong>Valor:</strong> R$ $value
  <strong>Status:</strong> $status 
  <a href="$link/$id" target="_blank"
     style="
       margin-left: 10px;
       padding: 4px 12px;
       background-color: #4CAF50;
       color: white;
       text-decoration: none;
       border-radius: 5px;
       font-weight: bold;
       display: inline-block;
     ">Confirmar pagamento</a></li>""".trimIndent()
            }

    return template.replace(
            "<!-- Lista de contas -->",
            "<ul style=\"list-style:none; padding:0; margin:0;\">\n$blocos\n</ul>"
    )
  }
}
