package com.example.VecLiteraryClubApp;

import com.google.firebase.database.DataSnapshot;

public class StudentData {

    String Fname = "Hatsune";
    String Lname = "Miku";
    String Course = "CSE";
    String Section = "A";
    String Email = "Hatsune@Miku.com";
    String Password = "password";
    String Gender = "Female";
    String ProfilePicture = "none";
    String LCA = "393-939";
    String UID = "Daisuke";
    String Year = "1";

    StudentData(String Values[]) {
        Fname = Values[0];
        Lname = Values[1];
        Course = Values[2];
        Section = Values[3];
        Year = Values[4];
        Email = Values[5];
        Password = Values[6];
        Gender = Values[7];
        ProfilePicture = Values[8];
        LCA = Values[9];
        UID = Values[10];
    }

    public static int generateLCA() {
        int LCA = 0;

        while (LCA < 100000) {
            LCA = (int) (Math.random() * 999999);
        }
        return LCA;
    }

}
