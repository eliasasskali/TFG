package com.eliasasskali.tfg.model

data class PostDto(
    val title: String = "",
    val content: String = "",
    val date: Long = 0,
    val clubId: String = ""
) {
    fun toModel(clubName: String): Post {
        return Post(
            title = this.title,
            content = this.content,
            date = this.date,
            clubName = clubName
        )
    }
}

data class Post(
    val title: String = "",
    val content: String = "",
    val date: Long = 0,
    val clubName: String = ""
) {
    fun toModel(clubId: String): PostDto {
        return PostDto(
            title = this.title,
            content = this.content,
            date = this.date,
            clubId = clubId
        )
    }
}