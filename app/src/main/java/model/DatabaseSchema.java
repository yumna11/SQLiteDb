package model;

import android.provider.BaseColumns;

/**
 * Created by YumnaAsim on 2/2/2018.
 */

public final class DatabaseSchema {

    private DatabaseSchema() {
    }

    public static final class Patient implements BaseColumns{

        private Patient() {
        }

        public static final String PATIENT_TABLE_NAME = "Patient";
        public static final String PATIENT_NAME = "Patient_Name";
        public static final String PATIENT_AGE = "Patient_Age";
    }

    public static final class Report implements BaseColumns{

        public static final String REPORT_TABLE_NAME = "Report";
        public static final String PATIENT_ID = "Patient_ID";
        public static final String REPORT_COLLECTOR_NAME = "Collector_Name";

    }
}
