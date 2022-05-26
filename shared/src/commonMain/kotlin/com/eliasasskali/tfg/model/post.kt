package com.eliasasskali.tfg.model

import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class PostDto(
    val title: String = "",
    val content: String = "",
    val date: Long = 0,
    val clubId: String = "",
    val clubName: String = ""
) {

    fun toModel(postId: String): Post {
        return Post(
            postId = postId,
            title = this.title,
            content = this.content,
            date = this.date,
            clubName = this.clubName,
            clubId = this.clubId,
            dateString = getDateTime(this.date)
        )
    }
}

data class Post(
    val postId: String = "",
    val title: String = "",
    val content: String = "",
    val date: Long = 0,
    val clubName: String = "",
    val clubId: String = "",
    val dateString: String = ""
) {
    fun toModel(clubId: String): PostDto {
        return PostDto(
            title = this.title,
            content = this.content,
            date = this.date,
            clubId = clubId,
            clubName = this.clubName
        )
    }
}

private fun getDateTime(timeStamp: Long): String {
    return java.time.Instant.ofEpochMilli(timeStamp)
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()
        .format(DateTimeFormatter.ofPattern("dd-MMM-yyyy, HH:mm"))
        .toString()
}