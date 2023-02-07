package com.plcoding.dictionary.feature_dictionary.domain.use_case

import com.plcoding.dictionary.core.util.Resource
import com.plcoding.dictionary.feature_dictionary.domain.model.WordInfo
import com.plcoding.dictionary.feature_dictionary.domain.repository.WordInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetWordInfo(
    private val repository: WordInfoRepository
) {

    operator fun invoke(word: String): Flow<Resource<List<WordInfo>>> {
        // FOR VALIDATION LOGIC
        if(word.isBlank()) {
            return flow {  } // empty flow, no API call
        }
        return repository.getWordInfo(word)
    }
}