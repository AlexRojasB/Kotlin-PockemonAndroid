package com.example.android.pockemonandroid

import android.location.Location

class Pockemon {
    var Name:String ?=null
    var Descr:String ?=null
    var Image:Int ?=null
    var Power:Double ?=null
    var Lat:Double ?=null
    var Long:Double ?=null
    var IsCatched = false
    var Locationn:Location?=null

    constructor(name:String, image:Int, des:String, power:Double, lat:Double, log:Double){
        this.Name = name
        this.Image = image
        this.Descr = des
        this.Power = power
        this.Locationn = Location(name)
        this.Locationn!!.latitude = lat
        this.Locationn!!.longitude = log
    }
}