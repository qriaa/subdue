package com.studx.subdue.logic

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class DateUpdateWorker(val ctx: Context, val params: WorkerParameters, val logicObj: SubLogic) : Worker(ctx, params){
    override fun doWork(): Result {
        logicObj.updateAllPayments()
        return Result.success()
    }
}