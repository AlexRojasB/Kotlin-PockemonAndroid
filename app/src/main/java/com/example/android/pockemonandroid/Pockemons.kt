package com.example.android.pockemonandroid

class Pockemon {
    var Name:String ?=null
    var Descr:String ?=null
    var Image:Int ?=null
    var Power:Double ?=null
    var Lat:Double ?=null
    var Long:Double ?=null
    var IsCatched = false
    constructor(name:String, image:Int, des:String, power:Double, lat:Double, log:Double){
        this.Name = name
        this.Image = image
        this.Descr = des
        this.Power = power
        this.Lat = lat
        this.Long = log
    }
}