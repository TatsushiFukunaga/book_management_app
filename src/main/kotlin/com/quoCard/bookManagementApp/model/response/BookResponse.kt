package com.quoCard.bookManagementApp.model.response

import com.quoCard.bookManagementApp.model.dto.AuthorDto
import com.quoCard.bookManagementApp.model.entity.PublicationStatus
import java.math.BigDecimal

data class BookResponse(
    val id: Long,
    val title: String,
    val price: BigDecimal,
    val status: PublicationStatus,
    val authorIds: List<Long>
)