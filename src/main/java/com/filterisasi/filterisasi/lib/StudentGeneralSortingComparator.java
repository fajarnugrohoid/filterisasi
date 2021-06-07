package com.filterisasi.filterisasi.lib;

import com.filterisasi.filterisasi.dto.PpdbRegistration;

import java.util.Comparator;
import org.apache.commons.lang3.builder.CompareToBuilder;

/**
  * This comparator sorts a list of Employees by job title, age and salary
  * into ascending order.
  * @author www.codejava.net
  *
  */

public class StudentGeneralSortingComparator implements Comparator<PpdbRegistration> {

    @Override
    public int compare(PpdbRegistration o1, PpdbRegistration o2) {
        return new CompareToBuilder()
                .append(o2.getSkorPeserta(), o1.getSkorPeserta())
                .append(o1.getScoreDistanceFinal(), o2.getScoreDistanceFinal())
                .append(o1.getScoreAge(), o2.getScoreAge()).toComparison();
    }

}
