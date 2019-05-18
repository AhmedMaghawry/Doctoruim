package com.ezzat.doctoruim;

import com.ezzat.doctoruim.Control.DatabaseController;
import com.ezzat.doctoruim.Control.Utils.Utils;
import com.ezzat.doctoruim.Control.onEvent;
import com.ezzat.doctoruim.Model.UserType;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.ezzat.doctoruim.Control.Utils.Constants.USER_TABLE;
import static org.junit.Assert.assertEquals;

public class PatientUnitTest {

    @Test
    public void testDoctors() {
        DatabaseController.getAllUsersType(USER_TABLE, UserType.Doctor, new onEvent() {
            @Override
            public void onStart(Object object) {
            }

            @Override
            public void onProgress(Object object) {

            }

            @Override
            public void onEnd(Object object) {
                int expected = 1;
                int res = (int) object;
                assertEquals(expected,res);
            }
        });
    }

    @Test
    public void testStringsToList() {
        ArrayList<String> res = Utils.getList("1,2,3,4");
        ArrayList<String> expected = new ArrayList<>();
        expected.add("1");
        expected.add("2");
        expected.add("3");
        expected.add("4");
        assertEquals(expected, res);
    }

    @Test
    public void testListToString() {
        List<String> te = new ArrayList<>();
        te.add("1");
        te.add("2");
        te.add("3");
        te.add("4");
        String res = Utils.getStrings(te);
        String expected = "1, 2, 3, 4";
        assertEquals(expected, res);
    }



}
