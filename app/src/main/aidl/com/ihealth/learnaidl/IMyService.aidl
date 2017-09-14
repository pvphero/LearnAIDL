// IMyService.aidl
package com.ihealth.learnaidl;

// Declare any non-default types here with import statements
import com.ihealth.learnaidl.Student;
interface IMyService {
   List<Student> getStudent();
   void addStudent(in Student student);
}
