package com.br.vobis.model

data class Location(
        var lat: Double = 0.0,
        var long: Double = 0.0,
        var description: InfoLocation = InfoLocation()
)