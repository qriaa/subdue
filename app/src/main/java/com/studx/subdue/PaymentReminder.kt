package com.studx.subdue

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

/**
 * A class implementing worker for managing notifications
 * @property[ctx] current Context
 * @property[params] @see[WorkerParameters]
 */

class PaymentReminder(val ctx: Context, val params: WorkerParameters) : Worker(ctx, params) {
    val title = "Tytul powiadomienia"
    val message = "Fajna tresc"
    override fun doWork(): Result {
        // TODO("Dodać czytanie z JSONA")
        PaymentNotification(ctx).createNotification(title, message)
        System.out.println("poszla notyfikacja")
        return Result.success()
    }
}