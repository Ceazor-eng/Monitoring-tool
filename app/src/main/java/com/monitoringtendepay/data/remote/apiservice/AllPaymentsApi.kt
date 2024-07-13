package com.monitoringtendepay.data.remote.apiservice

import com.monitoringtendepay.data.remote.dto.filterpayments.FilteredPayments
import com.monitoringtendepay.data.remote.dto.filterussdsessions.FilteredUssdSessions
import com.monitoringtendepay.data.remote.dto.monthlytransactions.FailedMonthlyTransactionsDto
import com.monitoringtendepay.data.remote.dto.monthlytransactions.MissingPaymentsDto
import com.monitoringtendepay.data.remote.dto.monthlytransactions.PendingMonthlyTransactionsDto
import com.monitoringtendepay.data.remote.dto.monthlytransactions.TotalMonthlyTransactionsDto
import com.monitoringtendepay.data.remote.dto.payments.Paymentss
import com.monitoringtendepay.data.remote.dto.ussd.FetchUssdSessions
import retrofit2.http.GET
import retrofit2.http.Query

interface AllPaymentsApi {

    @GET("Tende_monitoring_tool-main/fetch_payments_monitoring_tool.php")
    suspend fun getAllPayments(@Query("action") action: String): Paymentss

    @GET("Tende_monitoring_tool-main/fetch_payments_monitoring_tool.php")
    suspend fun filterPayments(
        @Query("action") action: String,
        @Query("serviceCode") serviceCode: String?,
        @Query("paymentStatus") paymentStatus: String?,
        @Query("startDate") startDate: String?,
        @Query("endDate") endDate: String?
    ): FilteredPayments

    @GET("Tende_monitoring_tool-main/fetch_sessions_monitoring_tool.php")
    suspend fun filterUssdSessions(
        @Query("action") action: String,
        @Query("msisdn") phoneNumber: String?,
        @Query("sessionId") sessionId: String?,
        @Query("startDate") startDate: String?,
        @Query("endDate") endDate: String?
    ): FilteredUssdSessions

    @GET("Tende_monitoring_tool-main/fetch_payments_monitoring_tool.php")
    suspend fun getTotalMonthlyTransactions(@Query("action") action: String): TotalMonthlyTransactionsDto

    @GET("Tende_monitoring_tool-main/fetch_payments_monitoring_tool.php")
    suspend fun getPendingMonthlyTransactions(@Query("action") action: String): PendingMonthlyTransactionsDto

    @GET("Tende_monitoring_tool-main/fetch_payments_monitoring_tool.php")
    suspend fun getFailedMonthlyTransactions(@Query("action") action: String): FailedMonthlyTransactionsDto

    @GET("Tende_monitoring_tool-main/fetch_payments_monitoring_tool.php")
    suspend fun getMissingPayments(@Query("action") action: String): MissingPaymentsDto

    @GET("Tende_monitoring_tool-main/fetch_sessions_monitoring_tool.php")
    suspend fun getAllUssdSessions(@Query("action") action: String): FetchUssdSessions
}