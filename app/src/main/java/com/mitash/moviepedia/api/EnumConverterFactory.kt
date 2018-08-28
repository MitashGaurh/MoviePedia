package com.mitash.moviepedia.api

import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class EnumConverterFactory : Converter.Factory() {

    override fun stringConverter(type: Type?, annotations: Array<Annotation>?, retrofit: Retrofit?): Converter<*, String>? {
        var converter: Converter<*, String>? = null
        if (type is Class<*> && type.isEnum) {
            converter = Converter<Enum<*>, String> { value -> value.toString() }
        }

        return converter
    }

}