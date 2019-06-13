package com.br.vobis.model

class LocationVobis {
    var latitude: Double? = null
    var longitude: Double? = null
    var infors: InfoLocation? = null

    fun getLocation(): String {
        return "${infors?.adress} - ${infors?.city} / ${infors?.uf}"
    }
}

class InfoLocation {
    var adress: String? = null
    var cep: String? = null
    var city: String? = null
    var uf: String? = null
}