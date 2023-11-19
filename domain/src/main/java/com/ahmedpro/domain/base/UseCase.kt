package com.ahmedpro.domain.base

interface UseCase

interface AsyncUseCase : UseCase {

    interface RequestUseCaseParameters<P, R> : AsyncUseCase {
        suspend operator fun invoke(params: P): R = doWork(params)
        suspend fun doWork(params: P): R
    }

    interface RequestUseCase<R> : AsyncUseCase {
        suspend operator fun invoke(): R = doWork()
        suspend fun doWork(): R
    }

    interface PostUseCase<P> : AsyncUseCase {
        suspend operator fun invoke(parameter: P) = doWork(parameter)
        suspend fun doWork(parameter: P)
    }
}

interface SyncUseCase : UseCase {

    interface RequestUseCaseParameters<P, R> : SyncUseCase {
        operator fun invoke(params: P): Result<R> = doWork(params)
        fun doWork(params: P): Result<R>
    }

    interface RequestUseCase<R> : SyncUseCase {
        operator fun invoke(): Result<R> = doWork()
        fun doWork(): Result<R>
    }
}
