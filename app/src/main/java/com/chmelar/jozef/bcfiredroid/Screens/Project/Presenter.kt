package com.chmelar.jozef.bcfiredroid.Screens.Project

import android.view.View
import com.chmelar.jozef.bcfiredroid.API.IRoutes
import com.chmelar.jozef.bcfiredroid.API.Model.Project
import com.chmelar.jozef.bcfiredroid.API.Model.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers



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
    val comment:CommentResponse,
    val success:Boolean
)

data class SubmitComment(val text:String, val author:Int)