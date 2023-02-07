package com.plcoding.dictionary.feature_dictionary.data.util

import java.lang.reflect.Type

interface JsonParser {

    // interface aby som mohol serializovat typy ktore room nepodporuje
    // vytvoria sa implentacie aby som mohol zamienat kniznice seriliazacne kniznice

    fun <T> fromJson(json: String, type: Type): T?

    fun <T> toJson(obj: T, type: Type): String?
}