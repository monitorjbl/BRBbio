package com.thundermoose.bio.managers;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.thundermoose.bio.model.Homologue;
import com.thundermoose.bio.model.HomologueData;
import com.thundermoose.bio.model.HomologueJoin;
import com.thundermoose.bio.model.HomologueSet;
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
    Map<String, HomologueData> setA = ImmutableMap.of(
            "101", new HomologueData("1", "101", 10),
            "102", new HomologueData("2", "102", 20),
            "103", new HomologueData("3", "103", 30)
    );
    Map<String, HomologueData> setB = ImmutableMap.of(
            "102", new HomologueData("12", "102", 11),
            "103", new HomologueData("13", "103", 21)
    );
    Map<String, HomologueData> setC = ImmutableMap.of(
            "102", new HomologueData("22", "102", 22),
            "103", new HomologueData("23", "103", 23),
            "107", new HomologueData("27", "107", 13)
    );

    HomologueJoin data = sut.intersection(ImmutableList.of(
            new HomologueSet("file1",setA),
            new HomologueSet("file2",setB),
            new HomologueSet("file3",setC)));

    assertEquals(2, data.getData().size());
    List<HomologueData> row1 = Iterables.find(data.getData(), new Predicate<List<HomologueData>>() {
      @Override
      public boolean apply(List<HomologueData> homologueDatas) {
        return homologueDatas.get(0).getHomologueId().equals("102");
      }
    });
    assertEquals(3,row1.size());

    List<HomologueData> row2 = Iterables.find(data.getData(), new Predicate<List<HomologueData>>() {
      @Override
      public boolean apply(List<HomologueData> homologueDatas) {
        return homologueDatas.get(0).getHomologueId().equals("103");
      }
    });
  }
}
