package com.ajudaqui.CalControl.repository

import com.ajudaqui.CalControl.entity.EventTemplate
import java.util.Optional
import org.springframework.data.jpa.repository.Query
import org.springframework.data.mongodb.repository.MongoRepository

interface TemplateRepository : MongoRepository<EventTemplate, String> {


  @Query("{ 'type': ?0, 'application': ?1 }") // ele pega por parametro... primerio ?0, segundo ?1, terceiro ?2...
  fun findByTypeAndApplication(type: String, application: String): Optional<EventTemplate>
}
