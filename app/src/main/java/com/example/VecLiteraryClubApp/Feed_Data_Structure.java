package com.example.VecLiteraryClubApp;

import java.util.ArrayList;

public class Feed_Data_Structure {

    String[] Values;
    byte[] Image;

    Feed_Data_Structure(ArrayList<String> Data) {
        if (Data == null) {
            return;
        }
        Values = new String[Data.size()];
        Values = Data.toArray(Values);
        Image = new byte[1];
    }

}
