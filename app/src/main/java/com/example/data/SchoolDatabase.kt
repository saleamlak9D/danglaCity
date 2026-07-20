package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        User::class,
        Student::class,
        Teacher::class,
        ParentEntity::class,
        ClassEntity::class,
        Section::class,
        Subject::class,
        TeacherSubject::class,
        Attendance::class,
        Exam::class,
        ExamResult::class,
        Assignment::class,
        AssignmentSubmission::class,
        Fee::class,
        Payment::class,
        Announcement::class,
        Event::class,
        Message::class,
        Notification::class
    ],
    version = 1,
    exportSchema = false
)
abstract class SchoolDatabase : RoomDatabase() {
    abstract fun schoolDao(): SchoolDao

    companion object {
        @Volatile
        private var INSTANCE: SchoolDatabase? = null

        fun getDatabase(context: Context): SchoolDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SchoolDatabase::class.java,
                    "highschool"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
