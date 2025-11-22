package com.ajudaqui.CalControl.repository

import com.ajudaqui.CalControl.entity.Template
import org.springframework.data.mongodb.repository.MongoRepository

interface TemplateRepository : MongoRepository<Template, String> {

  fun findByEmail(email: String): Template
}
