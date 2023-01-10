package com.studx.subdue

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.studx.subdue.logic.SubLogic
import com.studx.subdue.logic.SubdueSettings
import java.time.LocalDateTime


class PaymentReminder(val ctx: Context, val params: WorkerParameters) : Worker(ctx, params) {
    private val title = "Subdue - upcoming payment!"
    override fun doWork(): Result {
        SubLogic.loadSubs(ctx);
        val subscriptions = SubLogic.getSubList()
        subscriptions.sortBy { subscription -> subscription.dateAnchor}
        val latestSub = subscriptions[0]
        val daysBeforePayment = SubdueSettings().daysBeforePaymentAlert
        if(SubLogic.isNearPayment(latestSub, daysBeforePayment)) {
            val message = "Your ${latestSub.name} subscription is due in ${daysBeforePayment}!"
            PaymentNotification(ctx).createNotification(title, message)
        }
        return Result.success()
    }
}