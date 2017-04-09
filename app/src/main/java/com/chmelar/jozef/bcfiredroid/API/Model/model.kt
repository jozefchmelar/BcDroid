package com.chmelar.jozef.bcfiredroid.API.Model

data class Comment(
    val text: String,
    val createdAt: String,
    val author: User
)

data class CommentResponse(
    val text: String,
    val createdAt: String,
    val author: Int
)
data class SubmitCommentResponse(
    val comment: CommentResponse,
    val success:Boolean
)

data class Trip(
    val _id:String,
    val car:String,
    val reason:String,
    val employees:ArrayList<User>,
    val date:String

)
data class SubmitComment(val text:String, val author:Int)


data class ProjectRequest(
    val _id: String,
    val name: String,
    val costumer: String,
    val employees: List<Int>
)