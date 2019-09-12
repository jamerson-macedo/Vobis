package com.br.vobis.model

data class LocationVobis(
    var lat: Double = 0.0,
    var long: Double = 0.0,
    var description: InfoLocation = InfoLocation()
)