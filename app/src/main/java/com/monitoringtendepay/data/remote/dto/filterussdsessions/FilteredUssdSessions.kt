package com.monitoringtendepay.data.remote.dto.filterussdsessions

data class FilteredUssdSessions(
    val `data`: List<FilterUssdSessionsDto>,
    val totalSessions: Int
)