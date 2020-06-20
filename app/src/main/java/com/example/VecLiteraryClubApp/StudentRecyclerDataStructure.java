package com.example.VecLiteraryClubApp;

import android.graphics.Bitmap;

public class StudentRecyclerDataStructure {

    String Name, LCA, YCS, Fname, Lname, Course, Year, Section, Registrations, Submissions, Email;
    String Vals[];
    byte ProfileArray[];
    Bitmap Profile;

    StudentRecyclerDataStructure(String in_name, String lca, String ycs, String Values[]) {
        Name = in_name;
        LCA = lca;
        YCS = ycs;
        ProfileArray = new byte[1];
        Fname = Values[0];
        Lname = Values[1];
        Course = Values[2];
        Year = Values[4];
        Section = Values[3];
        Registrations = Values[11];
        Submissions = Values[12];
        Email = Values[5];
        Vals = Values;
    }

}
