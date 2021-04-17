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

public class StudentComparator implements Comparator<PpdbRegistration> {

    @Override
    public int compare(PpdbRegistration o1, PpdbRegistration o2) {
        return new CompareToBuilder()
                .append(o2.getSkorPeserta(), o1.getSkorPeserta())
                .append(o1.getSkorJarak1(), o2.getSkorJarak1()).toComparison();
    }
}
