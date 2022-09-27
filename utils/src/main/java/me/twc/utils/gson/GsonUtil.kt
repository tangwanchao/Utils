package me.twc.utils.gson

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.internal.ConstructorConstructor
import me.twc.utils.gson.GsonUtil.GSON
import java.lang.reflect.Type
import java.math.BigDecimal
import java.math.BigInteger

/**
 * @author 唐万超
 * @date 2020/04/24
 */

/**
 * 排除有该注解的字段的序列化/反序列化
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class NoGson

private class NoGsonExclusionStrategy : ExclusionStrategy {
    override fun shouldSkipField(f: FieldAttributes): Boolean {
        return f.getAnnotation(NoGson::class.java) != null
    }

    override fun shouldSkipClass(clazz: Class<*>?): Boolean {
        return false
    }
}


/**
 * 返回实例的 json 格式
 */
object GsonUtil {

    private val LONG_TYPE_ADAPTER = LongTypeAdapter()
    private val FLOAT_TYPE_ADAPTER = FloatTypeAdapter()
    private val DOUBLE_TYPE_ADAPTER = DoubleTypeAdapter()
    private val INT_TYPE_ADAPTER = IntTypeAdapter()
    private val BOOLEAN_TYPE_ADAPTER = BooleanTypeAdapter()
    private val STRING_TYPE_ADAPTER = StringTypeAdapter()

    @JvmStatic
    val GSON: Gson = GsonBuilder()
        // Long
        .registerTypeAdapter(Long::class.java, LONG_TYPE_ADAPTER)
        .registerTypeAdapter(java.lang.Long::class.java, LONG_TYPE_ADAPTER)
        .registerTypeAdapter(java.lang.Long.TYPE, LONG_TYPE_ADAPTER)
        // Float
        .registerTypeAdapter(Float::class.java, FLOAT_TYPE_ADAPTER)
        .registerTypeAdapter(java.lang.Float::class.java, FLOAT_TYPE_ADAPTER)
        .registerTypeAdapter(java.lang.Float.TYPE, FLOAT_TYPE_ADAPTER)
        // Double
        .registerTypeAdapter(Double::class.java, DOUBLE_TYPE_ADAPTER)
        .registerTypeAdapter(java.lang.Double::class.java, DOUBLE_TYPE_ADAPTER)
        .registerTypeAdapter(java.lang.Double.TYPE, DOUBLE_TYPE_ADAPTER)
        // Int
        .registerTypeAdapter(Int::class.java, INT_TYPE_ADAPTER)
        .registerTypeAdapter(java.lang.Integer::class.java, INT_TYPE_ADAPTER)
        .registerTypeAdapter(Integer.TYPE, INT_TYPE_ADAPTER)
        // Boolean
        .registerTypeAdapter(Boolean::class.java, BOOLEAN_TYPE_ADAPTER)
        .registerTypeAdapter(java.lang.Boolean::class.java, BOOLEAN_TYPE_ADAPTER)
        .registerTypeAdapter(java.lang.Boolean.TYPE, BOOLEAN_TYPE_ADAPTER)
        // String
        .registerTypeAdapter(String::class.java, STRING_TYPE_ADAPTER)
        // bit number
        .registerTypeAdapter(BigDecimal::class.java, BigDecimalTypeAdapter())
        .registerTypeAdapter(BigInteger::class.java, BigIntegerTypeAdapter())
        // Collection
        .registerTypeAdapterFactory(CollectionTypeAdapterFactory(ConstructorConstructor(mapOf(), true, listOf())))
        .setExclusionStrategies(NoGsonExclusionStrategy())
        .create()
}

fun Any.toJson(): String {
    return GSON.toJson(this)
}

inline fun <reified T> fromJson(
    json: String,
    type: Type = T::class.java
): T {
    return GSON.fromJson(json, type)
}

inline fun <reified T> fromJsonNull(json: String?): T? {
    if (json == null) {
        return null
    }
    return fromJson(json)
}