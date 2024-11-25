package com.quoCard.bookManagementApp.model.entity

import java.math.BigDecimal
import java.time.LocalDateTime

data class Book(
    val id: Long = 0,
    val title: String,
    val price: BigDecimal,
    var status: PublicationStatus = PublicationStatus.UNPUBLISHED,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
