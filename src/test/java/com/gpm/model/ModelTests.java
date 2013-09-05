package com.gpm.model;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ModelTests {
  @Test
  public void postageForWeight() {
    List<PostageBandCost> pbcs = new ArrayList<PostageBandCost>(3);
    PostageBandCost pbc1 = new PostageBandCost();
    pbc1.setWeightBand(250);
    pbc1.setWeightCost(170);
    pbcs.add(pbc1);
    PostageBandCost pbc3 = new PostageBandCost();
    pbc3.setWeightBand(750);
    pbc3.setWeightCost(280);
    pbcs.add(pbc3);
    PostageBandCost pbc2 = new PostageBandCost();
    pbc2.setWeightBand(500);
    pbc2.setWeightCost(210);
    pbcs.add(pbc2);

    Postage p = new Postage();
    p.setBandCosts(pbcs);

    Assert.assertEquals(p.getCostForBand(0), 0);
    Assert.assertEquals(p.getCostForBand(1), 170);
    Assert.assertEquals(p.getCostForBand(123), 170);
    Assert.assertEquals(p.getCostForBand(500), 210);
    Assert.assertEquals(p.getCostForBand(501), 280);
    Assert.assertEquals(p.getCostForBand(800), -1);
  }
}
