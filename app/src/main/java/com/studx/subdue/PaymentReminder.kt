package com.studx.subdue

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.studx.subdue.logic.SubLogic
import com.studx.subdue.logic.SubdueSettings
import java.time.LocalDateTime

/**
 * Worker class that checks if notification is necessary
 * @param[ctx] current context
 * @param[params] @see[WorkerParameters]
 */

class PaymentReminder(val ctx: Context, val params: WorkerParameters) : Worker(ctx, params) {

    private val title = "Subdue - upcoming payment!"

    /**
     * Overriden method from [Worker] interface that checks if notification is required and
     * sends notification to user if it's needed
     * @return @see[Result]
     */
    override fun doWork(): Result {
        SubLogic.loadSubs(ctx);
        val subscriptions = SubLogic.getSubList()
        if(subscriptions.isNotEmpty()) {
            subscriptions.sortBy { subscription -> subscription.dateAnchor }
            val latestSub = subscriptions[0]
            val daysBeforePayment = SubdueSettings().daysBeforePaymentAlert
            if (SubLogic.isNearPayment(latestSub, daysBeforePayment)) {
                val message = "Your ${latestSub.name} subscription is due in ${daysBeforePayment} days!"
                PaymentNotification(ctx).createNotification(title, message)
            }
        }
        return Result.success()
    }
}