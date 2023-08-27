package com.gaba.CompanieStatistics.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.gaba.CompanieStatistics.deserializers.StockDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Comparator;

@Table("stocks")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonDeserialize(using = StockDeserializer.class)
public class Stock {

    @Id
    private String id;

    private long volume;

    @Column("latest_volume")
    private long latestVolume;

    @Column("company_name")
    private String companyName;

    @Column("delta_volume")
    private long delta;
    public int compareByVolumeAndCompany(Stock s1, Stock s2) {

        if(s1.getVolume() > s2.getVolume())
            return -1;
        else if(s1.getVolume() < s2.getVolume())
            return 1;

        return s1.getCompanyName().compareTo(s2.getCompanyName());
    }

    public int compareByDelta(Stock s1, Stock s2) {

        if(s1.getDelta() > s2.getDelta())
            return -1;
        else if(s1.getVolume() < s2.getVolume())
            return 1;

        return 0;
    }
}
