package com.studx.subdue.logic

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class DateUpdateWorker(val ctx: Context, val params: WorkerParameters) : Worker(ctx, params){
    override fun doWork(): Result {
        SubLogic.updateAllPayments()
        return Result.success()
    }
}