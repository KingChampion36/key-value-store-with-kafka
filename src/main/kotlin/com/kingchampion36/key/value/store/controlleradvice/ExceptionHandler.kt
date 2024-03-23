package com.kingchampion36.key.value.store.controlleradvice

import com.kingchampion36.key.value.store.exception.NotFoundException
import com.kingchampion36.key.value.store.model.GenericResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {

  @ExceptionHandler(IllegalArgumentException::class)
  fun illegalArgumentException(e: IllegalArgumentException) = ResponseEntity
    .status(HttpStatus.BAD_REQUEST)
    .body(GenericResponse(e.localizedMessage))

  @ExceptionHandler(NotFoundException::class)
  fun notFoundException(e: NotFoundException) = ResponseEntity
    .status(HttpStatus.BAD_REQUEST)
    .body(GenericResponse(e.localizedMessage))
}
