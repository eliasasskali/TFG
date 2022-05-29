package com.eliasasskali.tfg.model

import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class ReviewDto(
    val userId: String = "",
    val clubId: String = "",
    val date: Long = 0,
    val comment: String = "",
    val rating: Int = 0
) {

    fun toModel(): Review {
        return Review(
            userId = this.userId,
            clubId = this.clubId,
            date = this.date,
            comment = this.comment,
            rating = this.rating,
            dateString = getDateTime(this.date)
        )
    }
}

data class Review(
    val userId: String = "",
    val clubId: String = "",
    val date: Long = 0,
    val comment: String = "",
    val rating: Int = 0,
    val dateString: String = ""
) {
    fun toModel(): ReviewDto {
        return ReviewDto(
            userId = this.userId,
            clubId = this.clubId,
            date = this.date,
            comment = this.comment,
            rating = this.rating
        )
    }
}

private fun getDateTime(timeStamp: Long): String {
    return java.time.Instant.ofEpochMilli(timeStamp)
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()
        .format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"))
        .toString()
}