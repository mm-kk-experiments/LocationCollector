package net.olewinski.locationcollector.data.models

data class LocationData(
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long,
    val id: Long = 0L
)
