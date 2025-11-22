package com.ajudaqui.CalControl.service

import com.ajudaqui.CalControl.entity.Template
import com.ajudaqui.CalControl.repository.TemplateRepository
import org.springframework.stereotype.Service

@Service
class TemplateService(private val repository: TemplateRepository) {

  fun create(template: Template): Template {

    return save(template)
  }

  private fun save(template: Template): Template = repository.save(template)
}
