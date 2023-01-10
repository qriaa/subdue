package com.studx.subdue

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.studx.subdue.logic.SubLogic
import java.time.LocalDateTime


class PaymentReminder(val ctx: Context, val params: WorkerParameters) : Worker(ctx, params) {
    val title = "Tytul powiadomienia"
    val message = "Fajna tresc"
    override fun doWork(): Result {
        SubLogic.loadSubs(ctx);
        val subscriptions = SubLogic.getSubList()
        subscriptions.sortBy { subscription -> subscription.dateAnchor}
        val latestSub = subscriptions[0]
        if(latestSub.dateAnchor < LocalDateTime.now().plusDays())
        PaymentNotification(ctx).createNotification(title, message)
        System.out.println("poszla notyfikacja")
        return Result.success()
    }
}