package com.example.crud

import java.util.*

class StudentModel (
    var id:Int = getAutoId(),
    var name:String = "",
    var email:String = "",
    var contact:String = "",
    var address:String = "",
){
    companion object{
        fun getAutoId():Int{
            var random = Random()
            return random.nextInt(100)
        }
    }

}