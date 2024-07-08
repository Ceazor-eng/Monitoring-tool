package com.monitoringtendepay.data.remote.apiservice

import com.monitoringtendepay.data.remote.dto.payments.Paymentss
import com.monitoringtendepay.data.remote.dto.ussd.FetchUssdSessions
import retrofit2.http.GET
import retrofit2.http.Query

interface AllPaymentsApi {

    @GET("Tende_monitoring_tool-main/fetch_payments_monitoring_tool.php")
    suspend fun getAllPayments(@Query("action")action: String): Paymentss
    @GET("Tende_monitoring_tool-main/fetch_payments_monitoring_tool.php")
    suspend fun getAllUssdSessions(@Query("action")action: String): FetchUssdSessions
}