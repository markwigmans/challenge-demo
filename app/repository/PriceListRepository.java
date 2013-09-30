package repository;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

import models.PriceList;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class PriceListRepository {

    private final ConcurrentMap<String, PriceList> priceLists = Maps.newConcurrentMap();

    public List<PriceList> getAll() {
        return Lists.newArrayList(priceLists.values());
    }

    public PriceList get(final String id) {
        return priceLists.get(id);
    }

    public PriceList add(final PriceList stadium) {
        return priceLists.put(stadium.id, stadium);
    }

    public int count() {
        return priceLists.size();
    }

    public void reset() {
        priceLists.clear();
    }
}
