package com.thundermoose.bio.managers;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.thundermoose.bio.model.Homologue;
import com.thundermoose.bio.model.HomologueData;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by Thundermoose on 4/28/2014.
 */
public class HomologueManagerTest {

  @InjectMocks
  HomologueManager sut = new HomologueManager();

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    sut.homologues = new HashMap<String, String>() {{
      put("1", "101");
      put("2", "102");
      put("3", "103");
      put("11", "101");
      put("12", "102");
      put("13", "103");
      put("14", "104");
      put("23", "103");
      put("27", "107");
    }};
  }

  @Test
  public void testIntersect() {
    Map<HomologueData, HomologueData> setA = ImmutableMap.of(
            new HomologueData("1", "101", 10), new HomologueData("1", "101", 10),
            new HomologueData("2", "102", 20), new HomologueData("2", "102", 20),
            new HomologueData("3", "103", 30), new HomologueData("3", "103", 30)
    );
    Map<HomologueData, HomologueData> setB = ImmutableMap.of(
            new HomologueData("12", "102", 11), new HomologueData("12", "102", 11),
            new HomologueData("13", "103", 21), new HomologueData("13", "103", 21)
    );
    Map<HomologueData, HomologueData> setC = ImmutableMap.of(
            new HomologueData("22", "102", 22), new HomologueData("22", "102", 22),
            new HomologueData("23", "103", 23), new HomologueData("23", "103", 23),
            new HomologueData("27", "107", 13), new HomologueData("27", "107", 13)
    );

    Set<List<HomologueData>> data = sut.intersection(ImmutableList.of(setA, setB, setC));

    assertEquals(2, data.size());
    List<HomologueData> row1 = Iterables.find(data, new Predicate<List<HomologueData>>() {
      @Override
      public boolean apply(List<HomologueData> homologueDatas) {
        return homologueDatas.get(0).getHomologueId().equals("102");
      }
    });
    assertEquals(3,row1.size());

    List<HomologueData> row2 = Iterables.find(data, new Predicate<List<HomologueData>>() {
      @Override
      public boolean apply(List<HomologueData> homologueDatas) {
        return homologueDatas.get(0).getHomologueId().equals("103");
      }
    });
  }
}
