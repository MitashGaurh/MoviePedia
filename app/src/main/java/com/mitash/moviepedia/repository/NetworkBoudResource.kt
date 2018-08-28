package com.mitash.moviepedia.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.support.annotation.MainThread
import android.support.annotation.WorkerThread
import com.mitash.moviepedia.AppExecutors
import com.mitash.moviepedia.api.ApiEmptyResponse
import com.mitash.moviepedia.api.ApiErrorResponse
import com.mitash.moviepedia.api.ApiResponse
import com.mitash.moviepedia.api.ApiSuccessResponse
import com.mitash.moviepedia.vo.Resource

/**
 * A generic class that can provide a resource backed by both the sqlite database and the network.
 *
 *
 * You can read more about it in the [Architecture
 * Guide](https://developer.android.com/arch).
 * @param <ResultType>
 * @param <RequestType>
</RequestType></ResultType> */
abstract class NetworkBoundResource<ResultType, RequestType>
@MainThread constructor(private val mAppExecutors: AppExecutors) {

    private val mResult = MediatorLiveData<Resource<ResultType>>()

    init {
        mResult.value = Resource.loading(null)
        @Suppress("LeakingThis")
        val dbSource = loadFromDb()
        mResult.addSource(dbSource) { data ->
            mResult.removeSource(dbSource)
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource)
            } else {
                mResult.addSource(dbSource) { newData ->
                    setValue(Resource.success(newData))
                }
            }
        }
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        if (mResult.value != newValue) {
            mResult.value = newValue
        }
    }

    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        val apiResponse = createCall()
        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
        mResult.addSource(dbSource) { newData ->
            setValue(Resource.loading(newData))
        }
        mResult.addSource(apiResponse) { response ->
            mResult.removeSource(apiResponse)
            mResult.removeSource(dbSource)
            when (response) {
                is ApiSuccessResponse -> {
                    mAppExecutors.diskIO().execute {
                        saveCallResult(processResponse(response))
                        mAppExecutors.mainThread().execute {
                            // we specially request a new live data,
                            // otherwise we will get immediately last cached value,
                            // which may not be updated with latest casts received from network.
                            mResult.addSource(loadFromDb()) { newData ->
                                setValue(Resource.success(newData))
                            }
                        }
                    }
                }
                is ApiEmptyResponse -> {
                    mAppExecutors.mainThread().execute {
                        // reload from disk whatever we had
                        mResult.addSource(loadFromDb()) { newData ->
                            setValue(Resource.success(newData))
                        }
                    }
                }
                is ApiErrorResponse -> {
                    onFetchFailed()
                    mResult.addSource(dbSource) { newData ->
                        setValue(Resource.error(response.errorMessage, newData))
                    }
                }
            }
        }
    }

    protected open fun onFetchFailed() {}

    fun asLiveData() = mResult as LiveData<Resource<ResultType>>

    @WorkerThread
    protected open fun processResponse(response: ApiSuccessResponse<RequestType>) = response.body

    @WorkerThread
    protected abstract fun saveCallResult(item: RequestType)

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>
}