package com.codesroots.satavendor.models.auth

data class Group(
    var created: String,
    var description: String,
    var id: Int,
    var modified: String,
    var name: String,
    var parent_id: Int,
    var registration_allowed: Int
)