package ru.netology.nmedia.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import retrofit2.HttpException
import ru.netology.nmedia.api.ApiService
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dao.PostRemoteKeyDao
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.entity.PostEntity
import ru.netology.nmedia.entity.PostRemoteKeyEntity
import ru.netology.nmedia.entity.toEntity
import ru.netology.nmedia.exception.ApiException

@OptIn(ExperimentalPagingApi::class)
class PostRemoteMediator(
    private val service: ApiService,
    private val db: AppDb,
    private val postDao: PostDao,
    private val postRemoteKeyDao: PostRemoteKeyDao,
) : RemoteMediator<Int, PostEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PostEntity>
    ): MediatorResult {
        try {
            val response = when (loadType) {
//                LoadType.REFRESH -> service.getLatest(state.config.initialLoadSize)

//    REFRESH не затирал предыдущий кеш, а добавлял данные сверху, учитывая id последнего поста сверху (соответственно, swipe to refresh должен "добавлять" данные, а не затирать их).
                LoadType.REFRESH -> postRemoteKeyDao.max()?.let {
                    service.getAfter(it, state.config.pageSize)
                } ?: service.getLatest(state.config.initialLoadSize)

                LoadType.PREPEND -> {
//Автоматический PREPEND был отключен
                    return MediatorResult.Success(true)
//                    val id = postRemoteKeyDao.max() ?: return MediatorResult.Success(
//                        endOfPaginationReached = false
//                    )
//                    service.getAfter(id, state.config.pageSize)
                }
                LoadType.APPEND -> {
                    val id = postRemoteKeyDao.min() ?: return MediatorResult.Success(
                        endOfPaginationReached = false
                    )
                    service.getBefore(id, state.config.pageSize)
                }
            }

            if (!response.isSuccessful) {
                throw ApiException(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiException(
                response.code(),
                response.message(),
            )

            val data = response.body() ?: throw HttpException(response)

            db.withTransaction {
                when (loadType) {
                    LoadType.REFRESH -> {
                        postRemoteKeyDao.insert(
                            PostRemoteKeyEntity(
                                PostRemoteKeyEntity.KeyType.AFTER,
                                data.first().id
                            )
                        )
                        if (postRemoteKeyDao.isEmpty()) {
                            postRemoteKeyDao.insert(
                                PostRemoteKeyEntity(
                                    PostRemoteKeyEntity.KeyType.BEFORE,
                                    data.last().id
                                )
                            )
                        }
                    }
//                        postRemoteKeyDao.removeAll()
//                        postRemoteKeyDao.insert(
//                            listOf(
//                                PostRemoteKeyEntity(
//                                    type = PostRemoteKeyEntity.KeyType.AFTER,
//                                    id = body.first().id,
//                                ),
//                                PostRemoteKeyEntity(
//                                    type = PostRemoteKeyEntity.KeyType.BEFORE,
//                                    id = body.last().id,
//                                ),
//                            )
//                        )
//                        postDao.removeAll()

                    LoadType.PREPEND -> {
//                        postRemoteKeyDao.insert(
//                            PostRemoteKeyEntity(
//                                type = PostRemoteKeyEntity.KeyType.AFTER,
//                                id = body.first().id,
//                            )
//                        )
                    }
                    LoadType.APPEND -> {
                        postRemoteKeyDao.insert(
                            PostRemoteKeyEntity(
                                type = PostRemoteKeyEntity.KeyType.BEFORE,
                                id = body.last().id,
                            )
                        )
                    }
                }
                postDao.insert(body.toEntity())
            }
            return MediatorResult.Success(endOfPaginationReached = body.isEmpty())
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }
}