package com.ubiehealth.capacitor.healthconnect

import androidx.health.connect.client.changes.Change
import androidx.health.connect.client.changes.DeletionChange
import androidx.health.connect.client.changes.UpsertionChange
import androidx.health.connect.client.impl.converters.datatype.RECORDS_CLASS_NAME_MAP
import androidx.health.connect.client.records.ActiveCaloriesBurnedRecord
import androidx.health.connect.client.records.BasalBodyTemperatureRecord
import androidx.health.connect.client.records.BasalMetabolicRateRecord
import androidx.health.connect.client.records.BloodGlucoseRecord
import androidx.health.connect.client.records.BloodPressureRecord
import androidx.health.connect.client.records.BodyFatRecord
import androidx.health.connect.client.records.BodyTemperatureRecord
import androidx.health.connect.client.records.DistanceRecord
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.FloorsClimbedRecord
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.HeightRecord
import androidx.health.connect.client.records.HydrationRecord
import androidx.health.connect.client.records.NutritionRecord
import androidx.health.connect.client.records.OxygenSaturationRecord
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.records.RespiratoryRateRecord
import androidx.health.connect.client.records.RestingHeartRateRecord
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.records.SpeedRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.health.connect.client.records.WeightRecord
import androidx.health.connect.client.records.WheelchairPushesRecord
import androidx.health.connect.client.records.BodyTemperatureMeasurementLocation.MEASUREMENT_LOCATION_INT_TO_STRING_MAP
import androidx.health.connect.client.records.BodyTemperatureMeasurementLocation.MEASUREMENT_LOCATION_STRING_TO_INT_MAP
import androidx.health.connect.client.records.metadata.DataOrigin
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.time.TimeRangeFilter
import androidx.health.connect.client.units.BloodGlucose
import androidx.health.connect.client.units.Energy
import androidx.health.connect.client.units.Length
import androidx.health.connect.client.units.Mass
import androidx.health.connect.client.units.MealType
import androidx.health.connect.client.units.Percentage
import androidx.health.connect.client.units.Power
import androidx.health.connect.client.units.Pressure
import androidx.health.connect.client.units.Speed
import androidx.health.connect.client.units.Temperature
import androidx.health.connect.client.units.Volume
import com.getcapacitor.JSObject
import org.json.JSONArray
import org.json.JSONObject
import java.lang.RuntimeException
import java.time.Instant
import java.time.ZoneOffset

internal fun <T> JSONArray.toList(): List<T> {
    return (0 until this.length()).map {
        @Suppress("UNCHECKED_CAST")
        this.get(it) as T
    }
}

internal fun <T> List<T>.toJSONArray(): JSONArray {
    return JSONArray(this)
}

internal fun JSONObject.toRecord(): Record {
    return when (val type = this.get("type")) {
        "ActiveCaloriesBurned" -> ActiveCaloriesBurnedRecord(
            startTime = this.getInstant("startTime"),
            startZoneOffset = this.getZoneOffsetOrNull("startZoneOffset"),
            endTime = this.getInstant("endTime"),
            endZoneOffset = this.getZoneOffsetOrNull("endZoneOffset"),
            energy = this.getEnergy("energy"),
        )
        "BasalBodyTemperature" -> BasalBodyTemperatureRecord(
            time = this.getInstant("time"),
            zoneOffset = this.getZoneOffsetOrNull("zoneOffset"),
            temperature = this.getTemperature("temperature"),
            measurementLocation = this.getBodyTemperatureMeasurementLocationInt("measurementLocation"),
        )
        "BasalMetabolicRate" -> BasalMetabolicRateRecord(
            time = this.getInstant("time"),
            zoneOffset = this.getZoneOffsetOrNull("zoneOffset"),
            basalMetabolicRate = this.getPower("basalMetabolicRate"),
        )
        "BloodGlucose" -> BloodGlucoseRecord(
            time = this.getInstant("time"),
            zoneOffset = this.getZoneOffsetOrNull("zoneOffset"),
            level = this.getBloodGlucose("level"),
            specimenSource = BloodGlucoseRecord.SPECIMEN_SOURCE_STRING_TO_INT_MAP
                .getOrDefault(this.getString("specimenSource"), BloodGlucoseRecord.SPECIMEN_SOURCE_UNKNOWN),
            mealType = MealType.MEAL_TYPE_STRING_TO_INT_MAP
                .getOrDefault(this.getString("mealType"), MealType.MEAL_TYPE_UNKNOWN),
            relationToMeal = BloodGlucoseRecord.RELATION_TO_MEAL_STRING_TO_INT_MAP
                .getOrDefault(this.getString("relationToMeal"), BloodGlucoseRecord.RELATION_TO_MEAL_UNKNOWN),
        )
        "BloodPressure" -> BloodPressureRecord(
            time = this.getInstant("time"),
            zoneOffset = this.getZoneOffsetOrNull("zoneOffset"),
            systolic = this.getPressure("systolic"),
            diastolic = this.getPressure("diastolic"),
            bodyPosition = BloodPressureRecord.BODY_POSITION_STRING_TO_INT_MAP
                .getOrDefault(this.getString("bodyPosition"), BloodPressureRecord.BODY_POSITION_UNKNOWN),
            measurementLocation = BloodPressureRecord.MEASUREMENT_LOCATION_STRING_TO_INT_MAP
                .getOrDefault(this.getString("measurementLocation"), BloodPressureRecord.MEASUREMENT_LOCATION_UNKNOWN),
        )
        "BodyFat" -> BodyFatRecord(
            time = this.getInstant("time"),
            zoneOffset = this.getZoneOffsetOrNull("zoneOffset"),
            percentage = this.getPercentage("percentage"),
        )
        "BodyTemperature" -> BodyTemperatureRecord(
            time = this.getInstant("time"),
            zoneOffset = this.getZoneOffsetOrNull("zoneOffset"),
            temperature = this.getTemperature("temperature"),
            measurementLocation = this.getBodyTemperatureMeasurementLocationInt("measurementLocation"),
        )
        "HeartRateSeries" -> HeartRateRecord(
            startTime = this.getInstant("startTime"),
            startZoneOffset = this.getZoneOffsetOrNull("startZoneOffset"),
            endTime = this.getInstant("endTime"),
            endZoneOffset = this.getZoneOffsetOrNull("endZoneOffset"),
            samples = this.getHeartRateRecordSamplesList("samples")
        )
        "Height" -> HeightRecord(
            time = this.getInstant("time"),
            zoneOffset = this.getZoneOffsetOrNull("zoneOffset"),
            height = this.getLength("height"),
        )
        "OxygenSaturation" -> OxygenSaturationRecord(
            time = this.getInstant("time"),
            zoneOffset = this.getZoneOffsetOrNull("zoneOffset"),
            percentage = this.getPercentage("percentage"),
        )
        "RespiratoryRate" -> RespiratoryRateRecord(
            time = this.getInstant("time"),
            zoneOffset = this.getZoneOffsetOrNull("zoneOffset"),
            rate = this.getDouble("rate"),
        )
        "RestingHeartRate" -> RestingHeartRateRecord(
            time = this.getInstant("time"),
            zoneOffset = this.getZoneOffsetOrNull("zoneOffset"),
            beatsPerMinute = this.getLong("beatsPerMinute"),
        )
        "Steps" -> StepsRecord(
            startTime = this.getInstant("startTime"),
            startZoneOffset = this.getZoneOffsetOrNull("startZoneOffset"),
            endTime = this.getInstant("endTime"),
            endZoneOffset = this.getZoneOffsetOrNull("endZoneOffset"),
            count = this.getLong("count"),
        )
        "Weight" -> WeightRecord(
            time = this.getInstant("time"),
            zoneOffset = this.getZoneOffsetOrNull("zoneOffset"),
            weight = this.getMass("weight"),
        )
        "Distance" -> DistanceRecord(
            startTime = this.getInstant("startTime"),
            startZoneOffset = this.getZoneOffsetOrNull("startZoneOffset"),
            endTime = this.getInstant("endTime"),
            endZoneOffset = this.getZoneOffsetOrNull("endZoneOffset"),
            distance = this.getLength("distance"),
        )
        "ExerciseSession" -> ExerciseSessionRecord(
            startTime = this.getInstant("startTime"),
            startZoneOffset = this.getZoneOffsetOrNull("startZoneOffset"),
            endTime = this.getInstant("endTime"),
            endZoneOffset = this.getZoneOffsetOrNull("endZoneOffset"),
            exerciseType = this.getString("exerciseType"),
            title = this.optString("title"),
            notes = this.optString("notes"),
        )
        "FloorsClimbed" -> FloorsClimbedRecord(
            startTime = this.getInstant("startTime"),
            startZoneOffset = this.getZoneOffsetOrNull("startZoneOffset"),
            endTime = this.getInstant("endTime"),
            endZoneOffset = this.getZoneOffsetOrNull("endZoneOffset"),
            floors = this.getLong("floors"),
        )
        "Hydration" -> HydrationRecord(
            startTime = this.getInstant("startTime"),
            startZoneOffset = this.getZoneOffsetOrNull("startZoneOffset"),
            endTime = this.getInstant("endTime"),
            endZoneOffset = this.getZoneOffsetOrNull("endZoneOffset"),
            volume = this.getVolume("volume"),
        )
        "Nutrition" -> NutritionRecord(
            startTime = this.getInstant("startTime"),
            startZoneOffset = this.getZoneOffsetOrNull("startZoneOffset"),
            endTime = this.getInstant("endTime"),
            endZoneOffset = this.getZoneOffsetOrNull("endZoneOffset"),
            name = this.optString("name"),
            mealType = MealType.MEAL_TYPE_STRING_TO_INT_MAP.getOrDefault(this.optString("mealType"), MealType.MEAL_TYPE_UNKNOWN),
            energy = if (this.has("energy")) this.getEnergy("energy") else null,
            protein = if (this.has("protein")) this.getMass("protein") else null,
            totalFat = if (this.has("totalFat")) this.getMass("totalFat") else null,
            saturatedFat = if (this.has("saturatedFat")) this.getMass("saturatedFat") else null,
            unsaturatedFat = if (this.has("unsaturatedFat")) this.getMass("unsaturatedFat") else null,
            carbohydrates = if (this.has("carbohydrates")) this.getMass("carbohydrates") else null,
            sugar = if (this.has("sugar")) this.getMass("sugar") else null,
            fiber = if (this.has("fiber")) this.getMass("fiber") else null,
            sodium = if (this.has("sodium")) this.getMass("sodium") else null,
            potassium = if (this.has("potassium")) this.getMass("potassium") else null,
            cholesterol = if (this.has("cholesterol")) this.getMass("cholesterol") else null,
        )
        "Sleep" -> SleepSessionRecord(
            startTime = this.getInstant("startTime"),
            startZoneOffset = this.getZoneOffsetOrNull("startZoneOffset"),
            endTime = this.getInstant("endTime"),
            endZoneOffset = this.getZoneOffsetOrNull("endZoneOffset"),
            stage = SleepSessionRecord.STAGE_STRING_TO_INT_MAP.getOrDefault(this.getString("stage"), SleepSessionRecord.STAGE_TYPE_UNKNOWN),
        )
        "Speed" -> SpeedRecord(
            startTime = this.getInstant("startTime"),
            startZoneOffset = this.getZoneOffsetOrNull("startZoneOffset"),
            endTime = this.getInstant("endTime"),
            endZoneOffset = this.getZoneOffsetOrNull("endZoneOffset"),
            speed = this.getSpeed("speed"),
        )
        "TotalCaloriesBurned" -> TotalCaloriesBurnedRecord(
            startTime = this.getInstant("startTime"),
            startZoneOffset = this.getZoneOffsetOrNull("startZoneOffset"),
            endTime = this.getInstant("endTime"),
            endZoneOffset = this.getZoneOffsetOrNull("endZoneOffset"),
            energy = this.getEnergy("energy"),
        )
        "WheelchairPushes" -> WheelchairPushesRecord(
            startTime = this.getInstant("startTime"),
            startZoneOffset = this.getZoneOffsetOrNull("startZoneOffset"),
            endTime = this.getInstant("endTime"),
            endZoneOffset = this.getZoneOffsetOrNull("endZoneOffset"),
            count = this.getLong("count"),
        )
        else -> throw IllegalArgumentException("Unexpected record type: $type")
    }
}

internal fun Record.toJSONObject(): JSONObject {
    return JSONObject().also { obj ->
        obj.put("type", RECORDS_CLASS_NAME_MAP[this::class])
        obj.put("metadata", this.metadata.toJSONObject())

        when (this) {
            is ActiveCaloriesBurnedRecord -> {
                obj.put("startTime", this.startTime)
                obj.put("startZoneOffset", this.startZoneOffset?.toJSONValue())
                obj.put("endTime", this.endTime)
                obj.put("endZoneOffset", this.endZoneOffset?.toJSONValue())
                obj.put("energy", this.energy.toJSONObject())
            }
            is BasalBodyTemperatureRecord -> {
                obj.put("time", this.time)
                obj.put("zoneOffset", this.zoneOffset?.toJSONValue())
                obj.put("temperature", this.temperature.toJSONObject())
                obj.put("measurementLocation", this.measurementLocation.toBodyTemperatureMeasurementLocationString())
            }
            is BasalMetabolicRateRecord -> {
                obj.put("time", this.time)
                obj.put("zoneOffset", this.zoneOffset?.toJSONValue())
                obj.put("basalMetabolicRate", this.basalMetabolicRate.toJSONObject())
            }
            is BloodGlucoseRecord -> {
                obj.put("time", this.time)
                obj.put("zoneOffset", this.zoneOffset?.toJSONValue())
                obj.put("level", this.level.toJSONObject())
                obj.put("specimenSource", BloodGlucoseRecord.SPECIMEN_SOURCE_INT_TO_STRING_MAP.getOrDefault(this.specimenSource, "unknown"))
                obj.put("mealType", MealType.MEAL_TYPE_INT_TO_STRING_MAP.getOrDefault(this.mealType, "unknown"))
                obj.put("relationToMeal", BloodGlucoseRecord.RELATION_TO_MEAL_INT_TO_STRING_MAP.getOrDefault(this.relationToMeal, "unknown"))
            }
            is BloodPressureRecord -> {
                obj.put("time", this.time)
                obj.put("zoneOffset", this.zoneOffset?.toJSONValue())
                obj.put("systolic", this.systolic.toJSONObject())
                obj.put("diastolic", this.diastolic.toJSONObject())
                obj.put("bodyPosition", BloodPressureRecord.BODY_POSITION_INT_TO_STRING_MAP.getOrDefault(this.bodyPosition, "unknown"))
                obj.put("measurementLocation", BloodPressureRecord.MEASUREMENT_LOCATION_INT_TO_STRING_MAP.getOrDefault(this.measurementLocation, "unknown"))
            }
            is BodyFatRecord -> {
                obj.put("time", this.time)
                obj.put("zoneOffset", this.zoneOffset?.toJSONValue())
                obj.put("percentage", this.percentage.toJSONObject())
            }
            is BodyTemperatureRecord -> {
                obj.put("time", this.time)
                obj.put("zoneOffset", this.zoneOffset?.toJSONValue())
                obj.put("temperature", this.temperature.toJSONObject())
                obj.put("measurementLocation", this.measurementLocation.toBodyTemperatureMeasurementLocationString())
            }
            is HeartRateRecord -> {
                obj.put("startTime", this.startTime)
                obj.put("startZoneOffset", this.startZoneOffset?.toJSONValue())
                obj.put("endTime", this.endTime)
                obj.put("endZoneOffset", this.endZoneOffset?.toJSONValue())
                obj.put("samples", this.samples.toHeartRateRecordSamplesJSONArray())
            }
            is HeightRecord -> {
                obj.put("time", this.time)
                obj.put("zoneOffset", this.zoneOffset?.toJSONValue())
                obj.put("height", this.height.toJSONObject())
            }
            is OxygenSaturationRecord -> {
                obj.put("time", this.time)
                obj.put("zoneOffset", this.zoneOffset?.toJSONValue())
                obj.put("percentage", this.percentage.toJSONObject())
            }
            is RespiratoryRateRecord -> {
                obj.put("time", this.time)
                obj.put("zoneOffset", this.zoneOffset?.toJSONValue())
                obj.put("rate", this.rate)
            }
            is RestingHeartRateRecord -> {
                obj.put("time", this.time)
                obj.put("zoneOffset", this.zoneOffset?.toJSONValue())
                obj.put("beatsPerMinute", this.beatsPerMinute)
            }
            is StepsRecord -> {
                obj.put("startTime", this.startTime)
                obj.put("startZoneOffset", this.startZoneOffset?.toJSONValue())
                obj.put("endTime", this.endTime)
                obj.put("endZoneOffset", this.endZoneOffset?.toJSONValue())
                obj.put("count", this.count)
            }
            is DistanceRecord -> {
                obj.put("startTime", this.startTime)
                obj.put("startZoneOffset", this.startZoneOffset?.toJSONValue())
                obj.put("endTime", this.endTime)
                obj.put("endZoneOffset", this.endZoneOffset?.toJSONValue())
                obj.put("distance", this.distance.toJSONObject())
            }
            is ExerciseSessionRecord -> {
                obj.put("startTime", this.startTime)
                obj.put("startZoneOffset", this.startZoneOffset?.toJSONValue())
                obj.put("endTime", this.endTime)
                obj.put("endZoneOffset", this.endZoneOffset?.toJSONValue())
                obj.put("exerciseType", this.exerciseType)
                obj.put("title", this.title)
                obj.put("notes", this.notes)
            }
            is FloorsClimbedRecord -> {
                obj.put("startTime", this.startTime)
                obj.put("startZoneOffset", this.startZoneOffset?.toJSONValue())
                obj.put("endTime", this.endTime)
                obj.put("endZoneOffset", this.endZoneOffset?.toJSONValue())
                obj.put("floors", this.floors)
            }
            is HydrationRecord -> {
                obj.put("startTime", this.startTime)
                obj.put("startZoneOffset", this.startZoneOffset?.toJSONValue())
                obj.put("endTime", this.endTime)
                obj.put("endZoneOffset", this.endZoneOffset?.toJSONValue())
                obj.put("volume", this.volume.toJSONObject())
            }
            is NutritionRecord -> {
                obj.put("startTime", this.startTime)
                obj.put("startZoneOffset", this.startZoneOffset?.toJSONValue())
                obj.put("endTime", this.endTime)
                obj.put("endZoneOffset", this.endZoneOffset?.toJSONValue())
                obj.put("name", this.name)
                obj.put("mealType", MealType.MEAL_TYPE_INT_TO_STRING_MAP.getOrDefault(this.mealType, "unknown"))
                obj.put("energy", this.energy?.toJSONObject())
                obj.put("protein", this.protein?.toJSONObject())
                obj.put("totalFat", this.totalFat?.toJSONObject())
                obj.put("saturatedFat", this.saturatedFat?.toJSONObject())
                obj.put("unsaturatedFat", this.unsaturatedFat?.toJSONObject())
                obj.put("carbohydrates", this.carbohydrates?.toJSONObject())
                obj.put("sugar", this.sugar?.toJSONObject())
                obj.put("fiber", this.fiber?.toJSONObject())
                obj.put("sodium", this.sodium?.toJSONObject())
                obj.put("potassium", this.potassium?.toJSONObject())
                obj.put("cholesterol", this.cholesterol?.toJSONObject())
            }
            is SleepSessionRecord -> {
                obj.put("startTime", this.startTime)
                obj.put("startZoneOffset", this.startZoneOffset?.toJSONValue())
                obj.put("endTime", this.endTime)
                obj.put("endZoneOffset", this.endZoneOffset?.toJSONValue())
                obj.put("stage", SleepSessionRecord.STAGE_INT_TO_STRING_MAP.getOrDefault(this.stage, "unknown"))
            }
            is SpeedRecord -> {
                obj.put("startTime", this.startTime)
                obj.put("startZoneOffset", this.startZoneOffset?.toJSONValue())
                obj.put("endTime", this.endTime)
                obj.put("endZoneOffset", this.endZoneOffset?.toJSONValue())
                obj.put("speed", this.speed.toJSONObject())
            }
            is TotalCaloriesBurnedRecord -> {
                obj.put("startTime", this.startTime)
                obj.put("startZoneOffset", this.startZoneOffset?.toJSONValue())
                obj.put("endTime", this.endTime)
                obj.put("endZoneOffset", this.endZoneOffset?.toJSONValue())
                obj.put("energy", this.energy.toJSONObject())
            }
            is WeightRecord -> {
                obj.put("time", this.time)
                obj.put("zoneOffset", this.zoneOffset?.toJSONValue())
                obj.put("weight", this.weight.toJSONObject())
            }
            is WheelchairPushesRecord -> {
                obj.put("startTime", this.startTime)
                obj.put("startZoneOffset", this.startZoneOffset?.toJSONValue())
                obj.put("endTime", this.endTime)
                obj.put("endZoneOffset", this.endZoneOffset?.toJSONValue())
                obj.put("count", this.count)
            }
            else -> throw IllegalArgumentException("Unexpected record class: ${this::class.qualifiedName}")
        }
    }
}

internal fun Metadata.toJSONObject(): JSONObject {
    return JSONObject().also { obj ->
        obj.put("id", this.id)
        obj.put("clientRecordId", this.clientRecordId)
        obj.put("clientRecordVersion", this.clientRecordVersion)
        obj.put("lastModifiedTime", this.lastModifiedTime)
        obj.put("dataOrigin", this.dataOrigin.packageName)
    }
}

internal fun Change.toJSObject(): JSObject {
    return JSObject().also { obj ->
        when (this) {
            is UpsertionChange -> {
                obj.put("type", "Upsert")
                obj.put("record", this.record.toJSONObject())
            }

            is DeletionChange -> {
                obj.put("type", "Delete")
                obj.put("recordId", this.recordId)
            }
        }
    }
}

internal fun JSONObject.getInstant(name: String): Instant {
    return Instant.parse(this.getString(name))
}

internal fun JSONObject.getZoneOffsetOrNull(name: String): ZoneOffset? {
    return if (this.has(name))
        ZoneOffset.of(this.getString(name))
    else
        null
}

internal fun ZoneOffset.toJSONValue(): String {
    return this.id
}

internal fun JSONObject.getLength(name: String): Length {
    val obj = requireNotNull(this.getJSONObject(name))
    val unit = obj.getString("unit")
    val value = obj.getDouble("value")
    return when (unit) {
        "meter" -> Length.meters(value)
        "kilometer" -> Length.kilometers(value)
        "mile" -> Length.miles(value)
        "inch" -> Length.inches(value)
        "feet" -> Length.feet(value)
        else -> throw IllegalArgumentException("Unexpected mass unit: $unit")
    }
}

internal fun Length.toJSONObject(): JSONObject {
    return JSONObject().also { obj ->
        obj.put("unit", "meter") // TODO: support other units
        obj.put("value", this.inMeters)
    }
}

internal fun JSONObject.getMass(name: String): Mass {
    val obj = requireNotNull(this.getJSONObject(name))
    val unit = obj.getString("unit")
    val value = obj.getDouble("value")
    return when (unit) {
        "gram" -> Mass.grams(value)
        "kilogram" -> Mass.kilograms(value)
        "milligram" -> Mass.milligrams(value)
        "microgram" -> Mass.micrograms(value)
        "ounce" -> Mass.ounces(value)
        "pound" -> Mass.pounds(value)
        else -> throw IllegalArgumentException("Unexpected mass unit: $unit")
    }
}

internal fun Mass.toJSONObject(): JSONObject {
    return JSONObject().also { obj ->
        obj.put("unit", "gram") // TODO: support other units
        obj.put("value", this.inGrams)
    }
}

internal fun BloodGlucose.toJSONObject(): JSONObject {
    return JSONObject().also { obj ->
        obj.put("unit", "milligramsPerDeciliter") // TODO: support other units
        obj.put("value", this.inMilligramsPerDeciliter)
    }
}

internal fun JSONObject.getBloodGlucose(name: String): BloodGlucose {
    val obj = requireNotNull(this.getJSONObject(name))

    val value = obj.getDouble("value")
    return when (val unit = obj.getString("unit")) {
        "milligramsPerDeciliter" -> BloodGlucose.milligramsPerDeciliter(value)
        "millimolesPerLiter" -> BloodGlucose.millimolesPerLiter(value)
        else -> throw RuntimeException("Invalid BloodGlucose unit: $unit")
    }
}

internal fun Energy.toJSONObject(): JSONObject {
    return JSONObject().also { obj ->
        obj.put("unit", "calories") // TODO: support other units
        obj.put("value", this.inCalories)
    }
}

internal fun JSONObject.getEnergy(name: String): Energy {
    val obj = requireNotNull(this.getJSONObject(name))

    val value = obj.getDouble("value")
    return when (val unit = obj.getString("unit")) {
        "calories" -> Energy.calories(value)
        "kilocalories" -> Energy.kilocalories(value)
        "joules" -> Energy.joules(value)
        "kilojoules" -> Energy.kilojoules(value)
        else -> throw RuntimeException("Invalid Energy unit: $unit")
    }
}

internal fun Temperature.toJSONObject(): JSONObject {
    return JSONObject().also { obj ->
        obj.put("unit", "celsius") // TODO: support other units
        obj.put("value", this.inCelsius)
    }
}

internal fun JSONObject.getTemperature(name: String): Temperature {
    val obj = requireNotNull(this.getJSONObject(name))

    val value = obj.getDouble("value")
    return when (val unit = obj.getString("unit")) {
        "celsius" -> Temperature.celsius(value)
        "fahrenheit" -> Temperature.fahrenheit(value)
        else -> throw RuntimeException("Invalid Temperature unit: $unit")
    }
}

internal fun Int.toBodyTemperatureMeasurementLocationString(): String {
    return MEASUREMENT_LOCATION_INT_TO_STRING_MAP.getOrDefault(this, "unknown")
}

internal fun JSONObject.getBodyTemperatureMeasurementLocationInt(name: String): Int {
    val str = requireNotNull(this.getString(name))
    return MEASUREMENT_LOCATION_STRING_TO_INT_MAP.getOrDefault(str, BodyTemperatureMeasurementLocation.MEASUREMENT_LOCATION_UNKNOWN)
}

internal fun Power.toJSONObject(): JSONObject {
    return JSONObject().also { obj ->
        obj.put("unit", "kilocaloriesPerDay") // TODO: support other units
        obj.put("value", this.inKilocaloriesPerDay)
    }
}

internal fun JSONObject.getPower(name: String): Power {
    val obj = requireNotNull(this.getJSONObject(name))

    val value = obj.getDouble("value")
    return when (val unit = obj.getString("unit")) {
        "kilocaloriesPerDay" -> Power.kilocaloriesPerDay(value)
        "watts" -> Power.watts(value)
        else -> throw RuntimeException("Invalid Power unit: $unit")
    }
}

internal fun Pressure.toJSONObject(): JSONObject {
    return JSONObject().also { obj ->
        obj.put("unit", "millimetersOfMercury") // TODO: support other units
        obj.put("value", this.inMillimetersOfMercury)
    }
}

internal fun JSONObject.getPressure(name: String): Pressure {
    val obj = requireNotNull(this.getJSONObject(name))

    val value = obj.getDouble("value")
    return when (val unit = obj.getString("unit")) {
        "millimetersOfMercury" -> Pressure.millimetersOfMercury(value)
        else -> throw RuntimeException("Invalid Pressure unit: $unit")
    }
}

internal fun JSONObject.getTimeRangeFilter(name: String): TimeRangeFilter {
    val obj = requireNotNull(this.getJSONObject(name))
    return when (val type = obj.getString("type")) {
        "before" -> TimeRangeFilter.before(obj.getInstant("time"))
        "after" -> TimeRangeFilter.after(obj.getInstant("time"))
        "between" -> TimeRangeFilter.between(obj.getInstant("startTime"), obj.getInstant("endTime"))
        else -> throw IllegalArgumentException("Unexpected TimeRange type: $type")
    }
}

internal fun JSObject.getDataOriginFilter(name: String): Set<DataOrigin> {
    return this.optJSONArray(name)?.toList<String>()?.map { DataOrigin(it) }?.toSet() ?: emptySet()
}

internal fun HeartRateRecord.Sample.toJSONObject(): JSONObject {
    return JSONObject().also { jsonObject ->
        jsonObject.put("time", this.time)
        jsonObject.put("beatsPerMinute", this.beatsPerMinute)
    }
}

internal fun List<HeartRateRecord.Sample>.toHeartRateRecordSamplesJSONArray(): JSONArray {
    return JSONArray().also { jsonArray ->
        this.forEach { sample ->
            jsonArray.put(sample.toJSONObject())
        }
    }
}

internal fun JSONObject.getHeartRateRecordSamplesList(name: String): List<HeartRateRecord.Sample> {
    val jsonArray = this.getJSONArray(name)
    return jsonArray.toList<JSONObject>().map { jsonObj ->
        HeartRateRecord.Sample(
            time = jsonObj.getInstant("time"),
            beatsPerMinute = jsonObj.getLong("beatsPerMinute")
        )
    }
}

internal fun Percentage.toJSONObject(): JSONObject {
    return JSONObject().also { obj ->
        obj.put("value", this.value)
    }
}

internal fun JSONObject.getPercentage(name: String): Percentage {
    val obj = requireNotNull(this.getJSONObject(name))
    val value = obj.getDouble("value")
    return Percentage(value)
}

internal fun Volume.toJSONObject(): JSONObject {
    return JSONObject().also { obj ->
        obj.put("unit", "milliliters") // TODO: support other units
        obj.put("value", this.inMilliliters)
    }
}

internal fun JSONObject.getVolume(name: String): Volume {
    val obj = requireNotNull(this.getJSONObject(name))
    val value = obj.getDouble("value")
    return when (val unit = obj.getString("unit")) {
        "milliliters" -> Volume.milliliters(value)
        "fluidOunces" -> Volume.fluidOunces(value)
        else -> throw RuntimeException("Invalid Volume unit: $unit")
    }
}

internal fun androidx.health.connect.client.units.Speed.toJSONObject(): JSONObject {
    return JSONObject().also { obj ->
        obj.put("unit", "metersPerSecond") // TODO: support other units
        obj.put("value", this.inMetersPerSecond)
    }
}

internal fun JSONObject.getSpeed(name: String): androidx.health.connect.client.units.Speed {
    val obj = requireNotNull(this.getJSONObject(name))
    val value = obj.getDouble("value")
    return when (val unit = obj.getString("unit")) {
        "metersPerSecond" -> androidx.health.connect.client.units.Speed.metersPerSecond(value)
        "kilometersPerHour" -> androidx.health.connect.client.units.Speed.kilometersPerHour(value)
        "milesPerHour" -> androidx.health.connect.client.units.Speed.milesPerHour(value)
        else -> throw RuntimeException("Invalid Speed unit: $unit")
    }
}
