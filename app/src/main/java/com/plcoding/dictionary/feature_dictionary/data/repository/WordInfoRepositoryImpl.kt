package com.plcoding.dictionary.feature_dictionary.data.repository

import com.plcoding.dictionary.core.util.Resource
import com.plcoding.dictionary.feature_dictionary.data.local.WordInfoDao
import com.plcoding.dictionary.feature_dictionary.data.remote.DictionaryApi
import com.plcoding.dictionary.feature_dictionary.domain.model.WordInfo
import com.plcoding.dictionary.feature_dictionary.domain.repository.WordInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class WordInfoRepositoryImpl(
    private val api: DictionaryApi,
    private val dao: WordInfoDao
): WordInfoRepository {

    // CACHING LOGIC
    override fun getWordInfo(word: String): Flow<Resource<List<WordInfo>>> = flow {

        // Display progress bar
        emit(Resource.Loading())

        val wordInfos = dao.getWordInfos(word).map { it.toWordInfo() }
        // we are still loading data from api but we can display cached data
        emit(Resource.Loading(data = wordInfos))

        try {
            val remoteWordInfos = api.getWordInfo(word)
            // Delete repeating words
            dao.deleteWordInfos(remoteWordInfos.map { it.word })
            dao.insertWordInfos(remoteWordInfos.map { it.toWordInfoEntity() })
        } catch(e: HttpException) { // Invalid Response
            emit(Resource.Error(
                message = "Oops, something went wrong!",
                data = wordInfos
            ))
        } catch(e: IOException) { // Parsing Error, Server not reachable, No Internet connection
            emit(Resource.Error(
                message = "Couldn't reach server, check your internet connection.",
                data = wordInfos
            ))
        }

        val newWordInfos = dao.getWordInfos(word).map { it.toWordInfo() }
        emit(Resource.Success(newWordInfos))
    }
}