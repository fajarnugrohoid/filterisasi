package com.filterisasi.filterisasi.lib;

import com.filterisasi.filterisasi.dto.PpdbRegistration;
import org.apache.commons.lang3.builder.CompareToBuilder;

import java.util.Comparator;

/**
  * This comparator sorts a list of Employees by job title, age and salary
  * into ascending order.
  * @author www.codejava.net
  *
  */

public class StudentAnakGuruSortingComparator implements Comparator<PpdbRegistration> {

    @Override
    public int compare(PpdbRegistration o1, PpdbRegistration o2) {
        return new CompareToBuilder()
                .append(o2.getPriority(), o1.getPriority())
                .append(o1.getScoreDistanceFinal(), o2.getScoreDistanceFinal())
                .append(o1.getBirthDate(), o2.getBirthDate()).toComparison();
    }

}
