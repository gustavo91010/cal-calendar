package com.ajudaqui.CalControl.repository

import com.ajudaqui.CalControl.entity.EventTemplate
import org.springframework.data.jpa.repository.Query
import org.springframework.data.mongodb.repository.MongoRepository

interface TemplateRepository : MongoRepository<EventTemplate, String> {

  // @Query(
  //         value = "{ 'type': ?0, 'application': ?1 }"
  // ) // ele pega por parametro... primerio ?0, segundo ?1, terceiro ?2...
  // fun findByTypeAndApplication(type: String, application: String): EventTemplate?
  fun findFirstByTypeAndApplication(type: String, application: String): EventTemplate?
}
