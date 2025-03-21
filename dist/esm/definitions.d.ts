export interface HealthConnectPlugin {
    checkAvailability(): Promise<{
        availability: HealthConnectAvailability;
    }>;
    insertRecords(options: {
        records: Record[];
    }): Promise<{
        recordIds: string[];
    }>;
    readRecord(options: {
        type: RecordType;
        recordId: string;
    }): Promise<{
        record: StoredRecord;
    }>;
    readRecords(options: {
        type: RecordType;
        timeRangeFilter: TimeRangeFilter;
        dataOriginFilter?: string[];
        ascendingOrder?: boolean;
        pageSize?: number;
        pageToken?: string;
    }): Promise<{
        records: StoredRecord[];
        pageToken?: string;
    }>;
    getChangesToken(options: {
        types: RecordType[];
    }): Promise<{
        token: string;
    }>;
    getChanges(options: {
        token: string;
    }): Promise<{
        changes: Change[];
        nextToken: string;
    }>;
    requestHealthPermissions(options: {
        read: RecordType[];
        write: RecordType[];
    }): Promise<{
        grantedPermissions: string[];
        hasAllPermissions: boolean;
    }>;
    checkHealthPermissions(options: {
        read: RecordType[];
        write: RecordType[];
    }): Promise<{
        grantedPermissions: string[];
        hasAllPermissions: boolean;
    }>;
    revokeHealthPermissions(): Promise<void>;
    openHealthConnectSetting(): Promise<void>;
}
export type HealthConnectAvailability = 'Available' | 'NotInstalled' | 'NotSupported';
export type RecordType = 'ActiveCaloriesBurned' | 'BasalBodyTemperature' | 'BasalMetabolicRate' | 'BloodGlucose' | 'BloodPressure' | 'BodyFat' | 'BodyTemperature' | 'Distance' | 'ExerciseSession' | 'FloorsClimbed' | 'HeartRateSeries' | 'Height' | 'Hydration' | 'Nutrition' | 'OxygenSaturation' | 'RespiratoryRate' | 'RestingHeartRate' | 'Sleep' | 'Speed' | 'Steps' | 'TotalCaloriesBurned' | 'Weight' | 'WheelchairPushes';
type RecordBase = {
    metadata: RecordMetadata;
};
type StoredRecord = RecordBase & Record;
export type Volume = {
    unit: 'milliliter' | 'liter' | 'fluid_ounce';
    value: number;
};
export type Distance = {
    unit: 'kilometers' | 'meters' | 'miles' | 'feet';
    value: number;
};
export type Speed = {
    unit: 'metersPerSecond' | 'kilometersPerHour' | 'milesPerHour';
    value: number;
};
export type Record = {
    type: 'ActiveCaloriesBurned';
    startTime: Date;
    startZoneOffset?: string;
    endTime: Date;
    endZoneOffset?: string;
    energy: Energy;
} | {
    type: 'BasalBodyTemperature';
    time: Date;
    zoneOffset?: string;
    temperature: Temperature;
    measurementLocation: 'unknown' | 'armpit' | 'finger' | 'forehead' | 'mouth' | 'rectum' | 'temporal_artery' | 'toe' | 'ear' | 'wrist' | 'vagina';
} | {
    type: 'BasalMetabolicRate';
    time: Date;
    zoneOffset?: string;
    basalMetabolicRate: Power;
} | {
    type: 'BloodGlucose';
    time: Date;
    zoneOffset?: string;
    level: BloodGlucose;
    specimenSource: 'unknown' | 'interstitial_fluid' | 'capillary_blood' | 'plasma' | 'serum' | 'tears' | 'whole_blood';
    mealType: 'unknown' | 'breakfast' | 'lunch' | 'dinner' | 'snack';
    relationToMeal: 'unknown' | 'general' | 'fasting' | 'before_meal' | 'after_meal';
} | {
    type: 'BloodPressure';
    time: Date;
    zoneOffset?: string;
    systolic: Pressure;
    diastolic: Pressure;
    bodyPosition: 'unknown' | 'standing_up' | 'sitting_down' | 'lying_down' | 'reclining';
    measurementLocation: 'unknown' | 'left_wrist' | 'right_wrist' | 'left_upper_arm' | 'right_upper_arm';
} | {
    type: 'BodyFat';
    time: Date;
    zoneOffset?: string;
    percentage: Percentage;
} | {
    type: 'BodyTemperature';
    time: Date;
    zoneOffset?: string;
    temperature: Temperature;
    measurementLocation: 'unknown' | 'armpit' | 'finger' | 'forehead' | 'mouth' | 'rectum' | 'temporal_artery' | 'toe' | 'ear' | 'wrist' | 'vagina';
} | {
    type: 'HeartRateSeries';
    startTime: Date;
    startZoneOffset?: string;
    endTime: Date;
    endZoneOffset?: string;
    samples: HeartRateSample[];
} | {
    type: 'Height';
    time: Date;
    zoneOffset?: string;
    height: Length;
} | {
    type: 'OxygenSaturation';
    time: Date;
    zoneOffset?: string;
    percentage: Percentage;
} | {
    type: 'RespiratoryRate';
    time: Date;
    zoneOffset?: string;
    rate: number;
} | {
    type: 'RestingHeartRate';
    time: Date;
    zoneOffset?: string;
    beatsPerMinute: number;
} | {
    type: 'Steps';
    startTime: Date;
    startZoneOffset?: string;
    endTime: Date;
    endZoneOffset?: string;
    count: number;
} | {
    type: 'Distance';
    startTime: Date;
    startZoneOffset?: string;
    endTime: Date;
    endZoneOffset?: string;
    distance: Distance;
} | {
    type: 'ExerciseSession';
    startTime: Date;
    startZoneOffset?: string;
    endTime: Date;
    endZoneOffset?: string;
    exerciseType: string;
    title?: string;
    notes?: string;
} | {
    type: 'FloorsClimbed';
    startTime: Date;
    startZoneOffset?: string;
    endTime: Date;
    endZoneOffset?: string;
    floors: number;
} | {
    type: 'Hydration';
    startTime: Date;
    startZoneOffset?: string;
    endTime: Date;
    endZoneOffset?: string;
    volume: Volume;
} | {
    type: 'Nutrition';
    startTime: Date;
    startZoneOffset?: string;
    endTime: Date;
    endZoneOffset?: string;
    name?: string;
    mealType?: 'unknown' | 'breakfast' | 'lunch' | 'dinner' | 'snack';
    energy?: Energy;
    protein?: Mass;
    totalFat?: Mass;
    saturatedFat?: Mass;
    unsaturatedFat?: Mass;
    carbohydrates?: Mass;
    sugar?: Mass;
    fiber?: Mass;
    sodium?: Mass;
    potassium?: Mass;
    cholesterol?: Mass;
} | {
    type: 'Sleep';
    startTime: Date;
    startZoneOffset?: string;
    endTime: Date;
    endZoneOffset?: string;
    title?: string;
    notes?: string;
    stages?: Array<{
        startTime: Date;
        endTime: Date;
        stage: 'unknown' | 'awake' | 'sleeping' | 'out_of_bed' | 'light' | 'deep' | 'rem' | 'awake_in_bed';
    }>;
} | {
    type: 'Speed';
    startTime: Date;
    startZoneOffset?: string;
    endTime: Date;
    endZoneOffset?: string;
    speed: Speed;
} | {
    type: 'TotalCaloriesBurned';
    startTime: Date;
    startZoneOffset?: string;
    endTime: Date;
    endZoneOffset?: string;
    energy: Energy;
} | {
    type: 'Weight';
    time: Date;
    zoneOffset?: string;
    weight: Mass;
} | {
    type: 'WheelchairPushes';
    startTime: Date;
    startZoneOffset?: string;
    endTime: Date;
    endZoneOffset?: string;
    count: number;
};
export type RecordMetadata = {
    id: string;
    clientRecordId?: string;
    clientRecordVersion: number;
    lastModifiedTime: Date;
    dataOrigin: string;
};
export type Change = {
    type: 'Upsert';
    record: Record;
} | {
    type: 'Delete';
    recordId: string;
};
export type TimeRangeFilter = {
    type: 'before' | 'after';
    time: Date;
} | {
    type: 'between';
    startTime: Date;
    endTime: Date;
};
export type Energy = {
    unit: 'calories' | 'kilocalories' | 'joules' | 'kilojoules';
    value: number;
};
export type HeartRateSample = {
    time: Date;
    beatsPerMinute: number;
};
export type Temperature = {
    unit: 'celsius' | 'fahrenheit';
    value: number;
};
export type Percentage = {
    value: number;
};
export type Power = {
    unit: 'kilocaloriesPerDay' | 'watts';
    value: number;
};
export type Pressure = {
    unit: 'millimetersOfMercury';
    value: number;
};
export type Length = {
    unit: 'meter' | 'kilometer' | 'mile' | 'inch' | 'feet';
    value: number;
};
export type Mass = {
    unit: 'gram' | 'kilogram' | 'milligram' | 'microgram' | 'ounce' | 'pound';
    value: number;
};
export type BloodGlucose = {
    unit: 'milligramsPerDeciliter' | 'millimolesPerLiter';
    value: number;
};
export {};
