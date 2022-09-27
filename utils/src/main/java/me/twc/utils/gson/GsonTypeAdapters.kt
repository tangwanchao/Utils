package me.twc.utils.gson

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.math.BigDecimal
import java.math.BigInteger

/**
 * @author 唐万超
 * @date 2020/09/29
 */
class LongTypeAdapter : TypeAdapter<Number>() {
    override fun write(out: JsonWriter?, value: Number?) {
        out?.value(value)
    }

    override fun read(read: JsonReader?): Number {
        return try {
            read?.nextLong() ?: 0L
        } catch (th: Throwable) {
            read?.skipValue()
            0L
        }
    }
}

class FloatTypeAdapter : TypeAdapter<Number>() {
    override fun write(out: JsonWriter?, value: Number?) {
        out?.value(value)
    }

    override fun read(read: JsonReader?): Number {
        return try {
            read?.nextDouble()?.toFloat() ?: 0F
        } catch (th: Throwable) {
            read?.skipValue()
            0F
        }
    }

}

class DoubleTypeAdapter : TypeAdapter<Number>() {
    override fun write(out: JsonWriter?, value: Number?) {
        out?.value(value)
    }

    override fun read(read: JsonReader?): Number {
        return try {
            read?.nextDouble() ?: 0.0
        } catch (th: Throwable) {
            read?.skipValue()
            0.0
        }
    }
}

class IntTypeAdapter : TypeAdapter<Number>() {
    override fun write(out: JsonWriter?, value: Number?) {
        out?.value(value)
    }

    override fun read(read: JsonReader?): Int {
        return try {
            read?.nextInt() ?: 0
        } catch (th: Throwable) {
            read?.skipValue()
            0
        }
    }
}

class StringTypeAdapter : TypeAdapter<String>() {
    override fun write(out: JsonWriter?, value: String?) {
        out?.value(value)
    }

    override fun read(read: JsonReader?): String? {
        if (read?.peek() == JsonToken.NULL) {
            read.nextNull()
            return ""
        } else {
            return try {
                val str = read?.nextString()
                if (str == "null" || str == "NULL") {
                    ""
                } else {
                    str
                }
            } catch (th: Throwable) {
                read?.skipValue()
                ""
            }
        }
    }
}

class BooleanTypeAdapter : TypeAdapter<Boolean>() {
    override fun write(out: JsonWriter?, value: Boolean?) {
        out?.value(value)
    }

    override fun read(read: JsonReader?): Boolean {
        return when (read?.peek()) {
            JsonToken.NULL -> {
                read.nextNull()
                false
            }
            JsonToken.STRING -> {
                read.nextString()?.toBoolean() ?: false
            }
            else -> {
                try {
                    read?.nextBoolean() ?: false
                } catch (th: Throwable) {
                    read?.skipValue()
                    false
                }
            }
        }
    }

}

class BigDecimalTypeAdapter : TypeAdapter<BigDecimal>() {
    override fun write(out: JsonWriter?, value: BigDecimal?) {
        out?.value(value)
    }

    override fun read(read: JsonReader): BigDecimal {
        val defaultValue = BigDecimal.valueOf(0.0)
        return when (read.peek()) {
            JsonToken.NULL -> {
                read.nextNull()
                defaultValue
            }
            else -> {
                try {
                    BigDecimal(read.nextString())
                } catch (th: Throwable) {
                    if (th !is NumberFormatException) {
                        read.skipValue()
                    }
                    defaultValue
                }
            }
        }
    }
}

class BigIntegerTypeAdapter : TypeAdapter<BigInteger>() {
    override fun write(out: JsonWriter, value: BigInteger?) {
        out.value(value)
    }

    override fun read(read: JsonReader): BigInteger {
        val defaultValue = BigInteger.valueOf(0)
        return when (read.peek()) {
            JsonToken.NULL -> {
                read.nextNull()
                defaultValue
            }
            else -> {
                try {
                    BigInteger(read.nextString())
                } catch (th: Throwable) {
                    if (th !is NumberFormatException) {
                        read.skipValue()
                    }
                    defaultValue
                }
            }
        }
    }

}
