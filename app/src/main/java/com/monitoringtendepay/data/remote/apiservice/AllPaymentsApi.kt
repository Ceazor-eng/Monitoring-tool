package com.monitoringtendepay.data.remote.apiservice

import com.monitoringtendepay.data.remote.dto.FetchAllPaymentsDto
import retrofit2.http.GET
import retrofit2.http.Query

interface AllPaymentsApi {

    @GET("Tende_monitoring_tool-main/fetch_payments_monitoring_tool.php")
    suspend fun getAllPayments(@Query("action")action: String): List<FetchAllPaymentsDto>
}