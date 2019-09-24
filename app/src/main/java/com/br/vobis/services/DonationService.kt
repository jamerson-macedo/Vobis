package com.br.vobis.services

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.br.vobis.model.Donation

class DonationService : EntityService<Donation>("donations") {

    fun getByPhone(phone: String): Query {
        return getByParam("phone", phone).orderBy("createdOn", Query.Direction.DESCENDING)
    }

    fun getByMember(memberReference: DocumentReference, limit: Long = 30): Query {
        return getByParam("memberResponsible", memberReference)
            .limit(limit)
    }

    fun getByInstitute(instituteReference: DocumentReference, limit: Long = 30): Query {
        return getByParam("instituteResponsible", instituteReference)
            .limit(limit)
    }

    fun getByStatus(status: Donation.STATUS, limit: Long = 50): Query {
        return getByParam("status", status.toString())
            .limit(limit)
    }

    /**
     * @param distance default 10.0
     * @reference https://stackoverflow.com/questions/46630507/how-to-run-a-geo-nearby-query-with-firestore
     */
    fun getByBestLocation(latitude: Double, longitude: Double, distance: Double = 10.0): Task<QuerySnapshot> {

        // ~1 mile of lat and lon in degrees
        val lat = 0.0144927536231884
        val lon = 0.0181818181818182

        val lowerLat = latitude - (lat * distance)
        val lowerLon = longitude - (lon * distance)

        val greaterLat = latitude + (lat * distance)
        val greaterLon = longitude + (lon * distance)

        val lesserGeopoint = GeoPoint(lowerLat, lowerLon)
        val greaterGeopoint = GeoPoint(greaterLat, greaterLon)

        return collectionReference
            .whereGreaterThanOrEqualTo("location.latitude", lesserGeopoint)
            .whereLessThanOrEqualTo("location.latitude", greaterGeopoint)
            .whereEqualTo("status", Donation.STATUS.PENDENT)
            .orderBy("createdOn")
            .limit(15)
            .get()
    }
}
